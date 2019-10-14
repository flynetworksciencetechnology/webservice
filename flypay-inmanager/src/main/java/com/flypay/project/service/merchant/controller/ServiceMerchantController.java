package com.flypay.project.service.merchant.controller;

import com.flypay.common.constant.UserConstants;
import com.flypay.framework.aspectj.lang.annotation.Log;
import com.flypay.framework.aspectj.lang.enums.BusinessType;
import com.flypay.framework.web.controller.BaseController;
import com.flypay.framework.web.domain.AjaxResult;
import com.flypay.framework.web.page.TableDataInfo;
import com.flypay.project.service.merchant.domain.Merchant;
import com.flypay.project.service.merchant.service.IMerchantService;
import com.flypay.project.service.store.service.StoreInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author flypay
 */
@Controller
@RequestMapping("/service/merchant")
public class ServiceMerchantController extends BaseController {
    private String prefix = "service/merchant";

    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private StoreInterface storeInterface;
    @RequiresPermissions("service:merchant:view")
    @GetMapping()
    public String merchant(){

        return prefix + "/merchant";
    }

    @RequiresPermissions("service:merchant:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Merchant merchant)
    {
        startPage();
        List<Merchant> list = merchantService.selectMerchantList(merchant);
        return getDataTable(list);
    }
    /**
     * 商户状态修改
     */
    @Log(title = "商户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:merchant:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Merchant merchant) {
        return toAjax(merchantService.changeStatus(merchant));
    }
    /**
     * 删除商户
     */
    @Log(title = "商户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:merchant:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        try{
            return toAjax(merchantService.deleteMerchantByIds(ids));
        }catch (Exception e){
            return error(e.getMessage());
        }
    }
    /**
     * 新增商户
     */
    @GetMapping("/add")
    public String add(){
        return prefix + "/add";
    }

    /**
     * 校验商户名称
     */
    @PostMapping("/checkMerchantNameUnique")
    @ResponseBody
    public String checkMerchantNameUnique(Merchant merchant){
        return merchantService.checkMerchantNameUnique(merchant);
    }

    /**
     * 校验商户APPID
     */
    @PostMapping("/checkMerchantAppIdUnique")
    @ResponseBody
    public String checkMerchantAppIdUnique(Merchant merchant) {
        return merchantService.checkMerchantAppIdUnique(merchant);
    }
    /**
     * 新增保存商户
     */
    @RequiresPermissions("service:merchant:add")
    @Log(title = "商户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Merchant merchant)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(merchantService.checkMerchantNameUnique(merchant))) {
            return error("新增商户'" + merchant.getMerchantName() + "'失败，商户名称已存在");
        }else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(merchantService.checkMerchantAppIdUnique(merchant))) {
            return error("新增商户'" + merchant.getAppId() + "'失败，商户APPID已存在");
        }
        return toAjax(merchantService.insertMerchant(merchant));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{merchantId}")
    public String edit(@PathVariable("merchantId") Long merchantId, ModelMap mmap)
    {
        mmap.put("merchant", merchantService.selectMerchantById(merchantId));
        return prefix + "/edit";
    }

    /**
     * 新增保存商户
     */
    @RequiresPermissions("service:merchant:edit")
    @Log(title = "商户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult update(@Validated Merchant merchant)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(merchantService.checkMerchantNameUnique(merchant))) {
            return error("新增商户'" + merchant.getMerchantName() + "'失败，商户名称已存在");
        }
        return toAjax(merchantService.updateMerchant(merchant));

    }
    /**
     * 修改前校验
     */
    @GetMapping("/editValidata")
    @ResponseBody
    public AjaxResult editValidate(@RequestParam("merchantId") Long merchantId){
        if( storeInterface.getRunningEquipmentCount(null,merchantId,null,null) > 0){
            return error("修改商户'" + merchantId + "'失败，该商户下有正在运行的设备,此时无法修改");
        }

        return success("正在查询商户,请稍等... ...");
    }
}
