package com.flypay.project.service.equipment.controller;

import com.flypay.common.constant.UserConstants;
import com.flypay.framework.aspectj.lang.annotation.Log;
import com.flypay.framework.aspectj.lang.enums.BusinessType;
import com.flypay.framework.web.controller.BaseController;
import com.flypay.framework.web.domain.AjaxResult;
import com.flypay.framework.web.page.TableDataInfo;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import com.flypay.project.service.store.service.StoreInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ServiceEquipmentController.class);
    @Autowired
    private IEquipmentService equipmentService;
    @Autowired
    private StoreInterface storeInterface;
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
        try{
            return toAjax(equipmentService.changeStatus(equipment));
        }catch (Exception e){
            return error(e.getMessage());
        }

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
            logger.error("删除设备失败 :" + e.getMessage());
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

    /**
     * 修改前校验
     */
    @GetMapping("/editValidata")
    @ResponseBody
    public AjaxResult editValidate(@RequestParam("equipmentId") Long equipmentId){
        if( storeInterface.getRunningEquipmentCount(null,null,null,equipmentId) > 0){
            return error("修改设备'" + equipmentId + "'失败，该设备正在运行的设备,此时无法修改");
        }

        return success("正在查询设备,请稍等... ...");
    }
}
