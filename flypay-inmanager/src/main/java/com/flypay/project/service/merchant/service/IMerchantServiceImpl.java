package com.flypay.project.service.merchant.service;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.common.utils.StringUtils;
import com.flypay.common.utils.security.ShiroUtils;
import com.flypay.common.utils.text.Convert;
import com.flypay.framework.aspectj.lang.annotation.DataScope;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.mapper.MerchantMapper;
import com.flypay.project.service.merchant.task.MerchantTaskJob;
import com.flypay.project.service.provider.vo.ProviderVo;
import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.mapper.ProviderMapper;
import com.flypay.project.service.store.domain.ServiceStore;
import com.flypay.project.service.store.service.IServiceStoreService;
import com.flypay.project.service.store.service.StoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色 业务层处理
 * 
 * @author flypay
 */
@Service
public class IMerchantServiceImpl implements IMerchantService
{
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private MerchantTaskJob merchantTaskJob;
    @Autowired
    private StoreInterface storeInterface;
    @Autowired
    private IServiceStoreService storeService;
    @Override
    @DataScope(deptAlias = "d")
    public List<Merchant> selectMerchantList(Merchant merchant) {
        return merchantMapper.selectMerchantList(merchant);
    }

    /**
     * 修改服务商状态
     *
     * @param merchant
     * @return
     */
    @Override
    public int changeStatus(Merchant merchant) {

        if( merchant != null && "1".equals(merchant.getStatus())) {
            //如果是关闭则需要把与商户相关的门店全部关闭
            if( merchant.getProviderId() != null && merchant.getMerchantId() == null){
                //服务商关闭,则查询服务商下是否有运行中的设备
                Integer count = storeInterface.getRunningEquipmentCount(merchant.getProviderId(),null,null,null);
                if( !Integer.valueOf(0).equals(count)){
                    //关闭策略1,等设备运行完毕立即关闭设备
                    //启动线程执行定时任务,轮询设备状态,设备处于闲,只执行策略2置就直接关闭(暂时不执行)
                    //关闭策略2,不关闭服务商及设备
                    //如果是关闭则需要把与服务商相关的商户和门店全部关闭
                    throw new BusinessException("该服务商有正在运行的设备,无法停用");
                }
                //如果没有则关闭商户下所有门店
                //同步关闭门店
                ServiceStore store = new ServiceStore();
                store.setStatus(merchant.getStatus());
                store.setProviderId(merchant.getProviderId());
                storeService.changeStatus(store);
            }else if( merchant.getProviderId() == null && merchant.getMerchantId() != null){
                Merchant info = merchantMapper.selectMerchantById(merchant.getMerchantId());
                if( info == null ){
                    throw new BusinessException(String.format("%1$s无此商户,请确认", info.getMerchantName()));
                }
                //查看是否有运行中的设备
                Integer count = storeInterface.getRunningEquipmentCount(info.getProviderId(),info.getMerchantId(),null,null);
                if( !Integer.valueOf(0).equals(count)){
                    //关闭策略1,等设备运行完毕立即关闭设备
                    //启动线程执行定时任务,轮询设备状态,设备处于闲,只执行策略2置就直接关闭(暂时不执行)
                    //关闭策略2,不关闭服务商及设备
                    //如果是关闭则需要把与服务商相关的商户和门店全部关闭
                    throw new BusinessException(String.format("%1$s有正在运行的设备,无法停用", info.getProviderName()));
                }
                ServiceStore store = new ServiceStore();
                store.setStatus(merchant.getStatus());
                store.setMerchantId(merchant.getMerchantId());
                storeService.changeStatus(store);
            }
        }else if(merchant != null && "0".equals(merchant.getStatus())){
            //开启的时候查看父级服务商是否开启如果是停用则不允许开启
            //查询商户
            Merchant info = selectMerchantById(merchant.getMerchantId());
            if( info == null || ServiceConstansts.DEL_FLAG.equals(info.getDelFlag())){
                throw new BusinessException("错误:此商户不存在或者已删除,请联系");
            }
            if( checkProviderStatus(info.getProviderId()) != 0 ){
                throw new BusinessException("错误:此商户服务商未启用,请联系管理员进行服务商启用");
            }
        }
        return merchantMapper.updateMerchant(merchant);
    }

    /**
     * 查询父级服务商
     * @param providerId
     * @return
     */
    private int checkProviderStatus(Long providerId) {
        Provider provider = providerMapper.selectProviderById(providerId);
        if( provider == null) return Integer.valueOf(ServiceConstansts.STOP_STATUS);
        if( ServiceConstansts.DEL_FLAG.equals(provider.getDelFlag())) return Integer.valueOf(ServiceConstansts.STOP_STATUS);
        if( ServiceConstansts.STOP_STATUS.equals(provider.getStatus())) return Integer.valueOf(ServiceConstansts.STOP_STATUS);
        return  Integer.valueOf(ServiceConstansts.USING_STATUS);
    }

    /**
     * 根据id批量删除服务商
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteMerchantByIds(String ids) {
        //解析ids
        Long[] merchantIds = Convert.toLongArray(ids);
        List<Merchant> ms = new ArrayList<>();
        for (Long merchantId : merchantIds){
            Merchant merchant = selectMerchantById(merchantId);
            if( merchant == null ) {
                throw new BusinessException(String.format("错误:%1$s找不到此商戶,不能删除", merchantId));
            }
            //查看商戶是否已經綁定門店,如果綁定則無法刪除
            if (countStoreByMerchantId(merchantId) > 0){
                throw new BusinessException(String.format("错误:%1$s有下属门店,不能删除", merchant.getMerchantName()));
            }
            ms.add(merchant);
        }
        int i = merchantMapper.deleteMerchantByIds(merchantIds);
        if( i >= 0){
            //将删除的商户状态改成关闭
            merchantTaskJob.closeMerchantStatus(ms);
        }
        return i;
    }

    /**
     * 根据id查询商户
     * @param merchantId
     * @return
     */
    @Override
    public Merchant selectMerchantById(Long merchantId) {
        return merchantMapper.selectMerchantById(merchantId);
    }

    /**
     * 校验服务商名称
     *
     * @param merchant
     * @return
     */
    @Override
    public String checkMerchantNameUnique(Merchant merchant) {
        Long merchantId = StringUtils.isNull(merchant.getMerchantId()) ? -1L : merchant.getMerchantId();
        Merchant info = merchantMapper.checkMerchantNameUnique(merchant.getMerchantName());
        if (StringUtils.isNotNull(info) && info.getMerchantId().longValue() != merchantId.longValue()) {
            return ServiceConstansts.MERCHANT_NAME_NOT_UNIQUE;
        }
        return ServiceConstansts.MERCHANT_NAME_UNIQUE;
    }

    /**
     * 校验服务商appid
     *
     * @param merchant
     * @return
     */
    @Override
    public String checkMerchantAppIdUnique(Merchant merchant) {
        Long merchantId = StringUtils.isNull(merchant.getMerchantId()) ? -1L : merchant.getMerchantId();
        Merchant info = merchantMapper.checkMerchantAppIdUnique(merchant.getAppId());
        if (StringUtils.isNotNull(info) && info.getMerchantId().longValue() != merchantId.longValue()) {
            return ServiceConstansts.MERCHANT_APPID_NOT_UNIQUE;
        }
        return ServiceConstansts.MERCHANT_APPID_UNIQUE;
    }

    /**
     * 添加服务商
     *
     * @param merchant
     * @return
     */
    @Override
    public int insertMerchant(Merchant merchant) {
        merchant.setCreateBy(ShiroUtils.getLoginName());
        // 新增服务商信息
        return merchantMapper.insertMerchant(merchant);
    }

    /**
     * 更新服务商
     *
     * @param merchant
     * @return
     */
    @Override
    public int updateMerchant(Merchant merchant) {
        merchant.setUpdateBy(ShiroUtils.getLoginName());
        // 修改角色信息
        return merchantMapper.updateMerchant(merchant);
    }


    /**
     * 根据服务商id查询是否有下属商户,必须是非删除状态的
     *
     * @param providerId
     * @return
     */
    @Override
    public int countMerchantByProviderId(Long providerId) {

        return merchantMapper.countMerchantByProviderId(providerId);
    }

    /**
     * 根据门店id查询商户信息
     *
     * @param storeId
     * @return
     */
    @Override
    public Merchant selectMerchantByStoreId(Long storeId) {

        return merchantMapper.selectMerchantByStoreId(storeId);
    }

    /**
     * 根据商户id查询门店数量
     * @param merchantId
     * @return
     */
    private int countStoreByMerchantId(Long merchantId) {
        ServiceStore store = new ServiceStore();
        store.setMerchantId(merchantId);
        return storeService.countStoreByMerchantId(store);
    }
}
