package com.flypay.project.service.equipment.service;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.common.exception.BusinessException;
import com.flypay.common.utils.StringUtils;
import com.flypay.common.utils.security.ShiroUtils;
import com.flypay.common.utils.text.Convert;
import com.flypay.framework.aspectj.lang.annotation.DataScope;
import com.flypay.framework.web.service.EquipmentService;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.mapper.EquipmentMapper;
import com.flypay.project.service.equipment.task.EquipmentTaskJob;
import com.flypay.project.service.equipment.vo.EquipmentVo;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.merchant.task.MerchantTaskJob;
import com.flypay.project.service.store.domain.ServiceStore;
import com.flypay.project.service.store.service.StoreInterface;
import com.flypay.project.service.store.service.impl.ServiceStoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EquipmentServiceImpl implements IEquipmentService{
    private static final Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentTaskJob equipmentTaskJob;
    @Autowired
    private StoreInterface storeInterface;
    @Autowired
    private ServiceStoreServiceImpl storeService;
    @Override
    @DataScope(deptAlias = "d")
    public List<Equipment> selectEquipmentList(Equipment equipment) {
        return equipmentMapper.selectEquipmentList(equipment);
    }

    /**
     * 修改设备状态
     *
     * @param equipment
     * @return
     */
    @Override
    public int changeStatus(Equipment equipment) {
        if( equipment == null){
            return 0;
        }
        //同步删除正在使用中的设备缓存
        if( equipment != null && "1".equals(equipment.getStatus())) {
            Integer count = storeInterface.getRunningEquipmentCount(null,null,null,equipment.getEquipmentId());
            if( !Integer.valueOf(0).equals(count)){
                throw new BusinessException(equipment.getEquipmentId() + "该设备正在运行,无法停用");
            }
            //关闭所有设备
            storeInterface.closeEquipment(null, null, null,equipment.getEquipmentId());
            //判断设备的运行状态,如果是闲置则可以直接删除缓存如果非闲置则不可停用设备
            //equipmentTaskJob.changeStatus(equipment.getStatus(),equipment.getEquipmentId());
        }else{
            //如果是启动
            //查看绑定的门店状态,如果是关闭则无法启动
            ServiceStore store = storeService.selectServiceStoreByEquipmentId(equipment.getEquipmentId());
            if( store == null){
                //未绑定
                equipment.setIsBand("0");
                equipment.setStatus("");
                equipmentMapper.updateEquipment(equipment);
                throw new BusinessException("错误:设备未绑定,无法启用");
            }
            if( ServiceConstansts.STOP_STATUS.equals(store.getStatus())){
                throw new BusinessException("错误:所属门店已经停用,无法启用");
            }
            //则将本设备改成闲置
            storeInterface.openEquipment(null, null, null,equipment.getEquipmentId());
        }
        return equipmentMapper.updateEquipment(equipment);
    }

    /**
     * 修改设备状态
     *
     * @param eIds
     * @return
     */
    @Override
    public int changeStatus(List<Long> eIds, String status) {

        if( eIds == null || eIds.isEmpty()){
            //可以关闭
            return 0;
        }
        if( status != null && "1".equals(status)) {
            //设备是否在运行
            for (Long eId:eIds) {
                Integer count = storeInterface.getRunningEquipmentCount(null,null,null,eId);
                if( !Integer.valueOf(0).equals(count)){
                    throw new BusinessException(eId + "该设备正在运行,无法停用");
                }
            }
        }
        //关闭设备
        int i = 0;
        for (Long eId:eIds) {
            if( status != null && "1".equals(status)){
                storeInterface.closeEquipment(null, null, null,eId);
            }
            Equipment equipment = new Equipment();
            equipment.setEquipmentId(eId);
            equipment.setStatus(status);
            changeStatus(equipment);
            i++;
        }

        return  i;
    }

    /**
     * 根据id批量删除设备
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteEquipmentByIds(String ids) {
        //解析ids
        Long[] equipmentIds = Convert.toLongArray(ids);
        List<Equipment> es = new ArrayList<>();
        for (Long equipmentId : equipmentIds){
            Equipment equipment = selectEquipmentById(equipmentId);
            if( equipment == null ) {
                throw new BusinessException(String.format("%1$s找不到此设备,不能删除", equipmentId));
            }
            //查看设备是否已经绑定商户,如果绑定则无法删除
            if (ServiceConstansts.STOP_STATUS.equals(equipment.getIsBand())){
                throw new BusinessException(String.format("%1$s已绑定,不能删除", equipment.getDeviceId()));
            }
            es.add(equipment);
        }
        int i = equipmentMapper.deleteEquipmentByIds(equipmentIds);
        if( i >= 0){
            //异步停用设备
            equipmentTaskJob.closeEquipmentStatus(es);
        }
        return i;
    }

    /**
     * 根据id查询商户
     * @param equipmentId
     * @return
     */
    @Override
    public Equipment selectEquipmentById(Long equipmentId) {
        return equipmentMapper.selectEquipmentById(equipmentId);
    }

    /**
     * 校验设备名称
     *
     * @param equipment
     * @return
     */
    @Override
    public String checkEquipmentDeviceIdUnique(Equipment equipment) {
        Long equipmentId = StringUtils.isNull(equipment.getEquipmentId()) ? -1L : equipment.getEquipmentId();
        Equipment info = equipmentMapper.checkEquipmentDeviceIdUnique(equipment.getDeviceId());
        if (StringUtils.isNotNull(info) && info.getEquipmentId().longValue() != equipmentId.longValue()) {
            return ServiceConstansts.PROVIDER_NAME_NOT_UNIQUE;
        }
        return ServiceConstansts.PROVIDER_NAME_UNIQUE;
    }

    
    /**
     * 添加设备
     *
     * @param equipment
     * @return
     */
    @Override
    public int insertEquipment(Equipment equipment) {
        equipment.setCreateBy(ShiroUtils.getLoginName());
        // 新增设备信息
        return equipmentMapper.insertEquipment(equipment);
    }

    /**
     * 更新设备
     *
     * @param equipment
     * @return
     */
    @Override
    public int updateEquipment(Equipment equipment) {
        equipment.setUpdateBy(ShiroUtils.getLoginName());
        // 修改角色信息
        return equipmentMapper.updateEquipment(equipment);
    }

    /**
     * 根据商户id查询设备列表
     *
     * @param equipment
     * @return
     */
    @Override
    public List<Equipment> selectEquipmentListByMerchantId(Equipment equipment) {

        return equipmentMapper.selectEquipmentListByMerchantId(equipment);
    }

    /**
     * 根据商户id查询设备列表(连动用)
     *
     * @param merchantId
     * @param equipmentId
     * @return
     */
    @Override
    public List<EquipmentVo> selectEquipmentListByMerchantId(Long merchantId, Long equipmentId) {
        //参数校验
        if( merchantId == null){
            throw new BusinessException("错误:商户id为空,请联系开发人员!!!");
        }
        Equipment equipment = new Equipment();
        List<EquipmentVo> evs = new ArrayList<>();
        //查询未绑定且未启用的,否则查询全部的
        if( equipmentId != null){

            //根据id查询设备信息,查询已绑定信息
            Equipment e = equipmentMapper.selectEquipmentById(equipmentId);
            if( e == null){
                throw new BusinessException("错误:该绑定设备异常,请联系管理员");
            }
            EquipmentVo ev = new EquipmentVo();
            ev.deviceId = e.getDeviceId();
            ev.equipmentId = e.getEquipmentId();
            ev.selected = true;
            evs.add(ev);
        }
        equipment.setStatus(ServiceConstansts.STOP_STATUS);
        equipment.setIsBand(ServiceConstansts.USING_STATUS);
        equipment.setProviderId(merchantId);
        //获取所有设备
        List<Equipment> es = selectEquipmentListByMerchantId(equipment);
        if( (evs == null || evs.isEmpty()) && (es == null || es.isEmpty())){
            throw new BusinessException("错误:商户下无可用设备,请联系管理员添加设备");
        }

        for(Equipment e : es){
            EquipmentVo ev = new EquipmentVo();
            ev.deviceId = e.getDeviceId();
            ev.equipmentId = e.getEquipmentId();
            if( equipmentId != null && equipmentId.equals(e.getEquipmentId())){
                ev.selected = true;
            }else{
                ev.selected = false;
            }
            evs.add(ev);
        }
        return evs;
    }

    @Override
    public boolean isCanRun(Equipment e) {
        logger.error("判定设备是否可运行");
        if( e == null){
            logger.error("设备不可运行,原因: 传入设备为空");
            return false;
        }
        if( ServiceConstansts.DEL_FLAG.equals(e.getDelFlag())){
            logger.error("设备不可运行,原因: 设备已被删除");
            return false;
        }
        if( e.getDeviceId() == null || "".equals(e.getDeviceId())){
            logger.error("设备不可运行,原因: 设备编号为空");
            return false;
        }
        logger.info("设备准备完毕可运行");
        return true;
    }

}
