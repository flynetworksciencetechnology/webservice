package com.flypay.project.service.equipment.controller;

import com.flypay.common.constant.UserConstants;
import com.flypay.framework.aspectj.lang.annotation.Log;
import com.flypay.framework.aspectj.lang.enums.BusinessType;
import com.flypay.framework.web.controller.BaseController;
import com.flypay.framework.web.domain.AjaxResult;
import com.flypay.framework.web.page.TableDataInfo;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 *
 * @author flypay
 */
@Controller
@RequestMapping("/service/equipment")
public class ServiceEquipmentController extends BaseController {
    private String prefix = "service/equipment";

    @Autowired
    private IEquipmentService equipmentService;
    @RequiresPermissions("service:equipment:view")
    @GetMapping()
    public String equipment()
    {
        return prefix + "/equipment";
    }

    @RequiresPermissions("service:equipment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Equipment equipment)
    {
        startPage();
        List<Equipment> list = equipmentService.selectEquipmentList(equipment);
        return getDataTable(list);
    }
    /**
     * 设备状态修改
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:equipment:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Equipment equipment) {
        return toAjax(equipmentService.changeStatus(equipment));
    }
    /**
     * 删除设备
     */
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("service:equipment:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){
        try{
            return toAjax(equipmentService.deleteEquipmentByIds(ids));
        }catch (Exception e){
            return error(e.getMessage());
        }
    }
    /**
     * 新增设备
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 校验设备名称
     */
    @PostMapping("/checkEquipmentDeviceIdUnique")
    @ResponseBody
    public String checkEquipmentDeviceIdUnique(Equipment equipment){
        return equipmentService.checkEquipmentDeviceIdUnique(equipment);
    }

    /**
     * 新增保存设备
     */
    @RequiresPermissions("service:equipment:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Equipment equipment)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(equipmentService.checkEquipmentDeviceIdUnique(equipment))) {
            return error("新增设备'" + equipment.getDeviceId() + "'失败，设备已经存在");
        }
        return toAjax(equipmentService.insertEquipment(equipment));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{equipmentId}")
    public String edit(@PathVariable("equipmentId") Long equipmentId, ModelMap mmap) {
        //获取该设备
        mmap.put("equipment", equipmentService.selectEquipmentById(equipmentId));
        return prefix + "/edit";
    }

    /**
     * 新增保存设备
     */
    @RequiresPermissions("service:equipment:edit")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult update(@Validated Equipment equipment)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(equipmentService.checkEquipmentDeviceIdUnique(equipment))) {
            return error("新增设备'" + equipment.getDeviceId() + "'失败，设备已经存在");
        }
        return toAjax(equipmentService.updateEquipment(equipment));

    }
}
