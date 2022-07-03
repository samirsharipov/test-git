package com.company.repository;


import com.company.entity.TypesEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TypesRepository extends PagingAndSortingRepository<TypesEntity, Integer> {

    Optional<TypesEntity> findByKey(String key);

    List<TypesEntity> findAllByVisible(Boolean b);

     /*   @Query(value = "select * from types order by id limit :limit offset :offset ", nativeQuery = true)
    List<TypesEntity> pagination(@Param("limit") int limit, @Param("offset") int offset );

    long countAllBy();*/



}
