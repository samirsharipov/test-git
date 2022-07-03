package com.company.repository;


import com.company.entity.EmailHistoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface EmailHistoryRepository extends PagingAndSortingRepository<EmailHistoryEntity, Integer> {


    Optional<EmailHistoryEntity> findByEmail(String s);

}
