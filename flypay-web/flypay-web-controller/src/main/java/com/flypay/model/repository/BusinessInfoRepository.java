package com.flypay.model.repository;

import com.flypay.model.pojo.BusinessInformationPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessInfoRepository  extends JpaRepository<BusinessInformationPO, Long>{

    BusinessInformationPO findByUuidEquals(String  uuid);
}
