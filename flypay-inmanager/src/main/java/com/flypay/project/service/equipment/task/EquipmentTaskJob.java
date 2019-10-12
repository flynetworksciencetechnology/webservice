package com.flypay.project.service.equipment.task;

import com.flypay.common.constant.ServiceConstansts;
import com.flypay.project.service.equipment.domain.Equipment;
import com.flypay.project.service.equipment.mapper.EquipmentMapper;
import com.flypay.project.service.store.service.StoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquipmentTaskJob {
    @Autowired
    EquipmentMapper providerMapper;
    @Autowired
    StoreInterface storeInterface;
    @Async
    public void closeEquipmentStatus(List<Equipment> es){
        if( es != null && !es.isEmpty()){
            for (Equipment e : es) {
                e.setStatus(ServiceConstansts.STOP_STATUS);
                storeInterface.closeEquipment(null,null,null,e.getEquipmentId());
                providerMapper.updateEquipment(e);
            }
        }
    }

}
