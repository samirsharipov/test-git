package com.company.repository;


import com.company.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<SmsEntity, Integer> {


    Optional<SmsEntity> findTopByPhoneOrderByCreatedDateDesc(String phone);

}
