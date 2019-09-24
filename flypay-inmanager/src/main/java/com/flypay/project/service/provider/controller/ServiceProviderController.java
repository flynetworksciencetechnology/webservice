package com.flypay.project.service.provider.controller;

import com.flypay.common.constant.UserConstants;
import com.flypay.framework.aspectj.lang.annotation.Log;
import com.flypay.framework.aspectj.lang.enums.BusinessType;
import com.flypay.framework.web.controller.BaseController;
import com.flypay.framework.web.domain.AjaxResult;
import com.flypay.framework.web.page.TableDataInfo;
import com.flypay.project.service.provider.domain.Provider;
import com.flypay.project.service.provider.service.IProviderService;
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
@RequestMapping("/service/provider")
public class ServiceProviderController extends BaseController {
    private String prefix = "service/provider";

    @Autowired
    private IProviderService providerService;
    @RequiresPermissions("service:provider:view")
    @GetMapping()
    public String provider()
    {
        return prefix + "/provider";
    }

    @RequiresPermissions("service:provider:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Provider provider)
    {
        startPage();
        List<Provider> list = providerService.selectProviderList(provider);
        return getDataTable(list);
    }
    /**
     * 服务商状态修改
     */
    @Log(title = "服务商管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:provider:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Provider provider) {
        return toAjax(providerService.changeStatus(provider));
    }
    /**
     * 删除服务商
     */
    @Log(title = "服务商管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:provider:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        try{
            return toAjax(providerService.deleteProviderByIds(ids));
        }catch (Exception e){
            return error(e.getMessage());
        }
    }
    /**
     * 新增服务商
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 校验服务商名称
     */
    @PostMapping("/checkProviderNameUnique")
    @ResponseBody
    public String checkProviderNameUnique(Provider provider){
        return providerService.checkProviderNameUnique(provider);
    }

    /**
     * 校验服务商APPID
     */
    @PostMapping("/checkProviderAppIdUnique")
    @ResponseBody
    public String checkProviderAppIdUnique(Provider provider) {
        return providerService.checkProviderAppIdUnique(provider);
    }
    /**
     * 新增保存服务商
     */
    @RequiresPermissions("service:provider:add")
    @Log(title = "服务商管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Provider provider)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(providerService.checkProviderNameUnique(provider))) {
            return error("新增服务商'" + provider.getProviderName() + "'失败，服务商名称已存在");
        }else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(providerService.checkProviderAppIdUnique(provider))) {
            return error("新增服务商'" + provider.getAppId() + "'失败，服务商APPID已存在");
        }
        return toAjax(providerService.insertProvider(provider));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{providerId}")
    public String edit(@PathVariable("providerId") Long providerId, ModelMap mmap)
    {
        mmap.put("provider", providerService.selectProviderById(providerId));
        return prefix + "/edit";
    }

    /**
     * 新增保存服务商
     */
    @RequiresPermissions("service:provider:edit")
    @Log(title = "服务商管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult update(@Validated Provider provider)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(providerService.checkProviderNameUnique(provider))) {
            return error("新增服务商'" + provider.getProviderName() + "'失败，服务商名称已存在");
        }
        return toAjax(providerService.updateProvider(provider));

    }
}
