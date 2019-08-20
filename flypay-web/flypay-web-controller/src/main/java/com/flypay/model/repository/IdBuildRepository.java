package com.flypay.model.repository;

import com.flypay.model.pojo.IdBuildPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdBuildRepository extends JpaRepository<IdBuildPO, Long>{


}
