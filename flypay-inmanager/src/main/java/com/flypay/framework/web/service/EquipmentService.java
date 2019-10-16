package com.flypay.framework.web.service;

import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.service.IEquipmentService;
import com.flypay.project.service.equipment.vo.EquipmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("equipment")
public class EquipmentService {
    @Autowired
    private IEquipmentService equipmentService;

    public List<EquipmentVo> searchAllEquipment(String merchantId){
        System.out.println("merchantId :" + merchantId);
        Equipment equipment = new Equipment();
        equipment.setStatus("0");
        //查找未绑定的
        equipment.setIsBand("0");
        List<Equipment> es = equipmentService.selectEquipmentList(equipment);
        List<EquipmentVo> evs = null;
        if( es != null && !es.isEmpty()){
            evs = new ArrayList<>();
            for(Equipment e : es){
                EquipmentVo ev = new EquipmentVo();
                ev.deviceId = e.getDeviceId();
                ev.equipmentId = e.getEquipmentId();
                evs.add(ev);
            }
        }
        return evs;
    }
}
