package com.flypay.project.service.store.controller;

import com.flypay.common.utils.poi.ExcelUtil;
import com.flypay.framework.aspectj.lang.annotation.Log;
import com.flypay.framework.aspectj.lang.enums.BusinessType;
import com.flypay.framework.web.controller.BaseController;
import com.flypay.framework.web.domain.AjaxResult;
import com.flypay.framework.web.page.TableDataInfo;
import com.flypay.project.service.store.domain.ServiceStore;
import com.flypay.project.service.store.service.IServiceStoreService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门店Controller
 * 
 * @author flypay
 * @date 2019-09-27
 */
@Controller
@RequestMapping("/service/store")
public class ServiceStoreController extends BaseController
{
    private String prefix = "service/store";

    @Autowired
    private IServiceStoreService serviceStoreService;

    @RequiresPermissions("service:store:view")
    @GetMapping()
    public String store()
    {
        return prefix + "/store";
    }

    /**
     * 查询门店列表
     */
    @RequiresPermissions("service:store:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ServiceStore serviceStore)
    {
        startPage();
        List<ServiceStore> list = serviceStoreService.selectServiceStoreList(serviceStore);
        return getDataTable(list);
    }

    /**
     * 导出门店列表
     */
    @RequiresPermissions("service:store:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ServiceStore serviceStore)
    {
        List<ServiceStore> list = serviceStoreService.selectServiceStoreList(serviceStore);
        ExcelUtil<ServiceStore> util = new ExcelUtil<ServiceStore>(ServiceStore.class);
        return util.exportExcel(list, "store");
    }

    /**
     * 新增门店
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存门店
     */
    @RequiresPermissions("service:store:add")
    @Log(title = "门店", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ServiceStore serviceStore)
    {
        return toAjax(serviceStoreService.insertServiceStore(serviceStore));
    }

    /**
     * 修改门店
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ServiceStore serviceStore = serviceStoreService.selectServiceStoreById(id);
        mmap.put("serviceStore", serviceStore);
        return prefix + "/edit";
    }

    /**
     * 修改保存门店
     */
    @RequiresPermissions("service:store:edit")
    @Log(title = "门店", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ServiceStore serviceStore)
    {
        return toAjax(serviceStoreService.updateServiceStore(serviceStore));
    }

    /**
     * 删除门店
     */
    @RequiresPermissions("service:store:remove")
    @Log(title = "门店", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        try {
            return toAjax(serviceStoreService.deleteServiceStoreByIds(ids));
        }catch (Exception e){
            return error(e.getMessage());
        }

    }

    /**
     * 服务商状态修改
     */
    @Log(title = "门店", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:store:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(ServiceStore store) {
        return toAjax(serviceStoreService.changeStatus(store));
    }
}
