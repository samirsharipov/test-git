package com.company.repository;


import com.company.entity.RegionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends PagingAndSortingRepository<RegionEntity, Integer> {

    Optional<RegionEntity> findByKey(String key);

    Optional<RegionEntity> findByIdAndVisibleTrue(Integer id);

    List<RegionEntity> findAllByVisible(Boolean b);


}
