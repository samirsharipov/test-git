package com.company.repository;


import com.company.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomUserDetailRepository extends CrudRepository<ProfileEntity, Integer> {




}
