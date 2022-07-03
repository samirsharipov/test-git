package com.company.repository;


import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends PagingAndSortingRepository<ProfileEntity, Integer> {


    Optional<ProfileEntity> findByEmail(String email);

    List<ProfileEntity> findAllByVisible(Boolean b);

    @Override
    Iterable<ProfileEntity> findAll();


    @Modifying
    @Transactional
    @Query("update  ProfileEntity p set p.status=?2 where p.phone=?1")
    void updateStatusByPhone(String phone, ProfileStatus status);

    Optional<ProfileEntity> findByIdAndStatusAndVisibleTrue(Integer id, ProfileStatus status);

    Optional<ProfileEntity> findByIdAndPhoto(Integer id, AttachEntity photo);

    @Modifying
    @Transactional
    @Query("update  ProfileEntity p set p.photo.id=?1 where p.id=?2")
    void updateByPhoto(String id, Integer pid);

    @Modifying
    @Transactional
    @Query("update  ProfileEntity p set p.photo.id=?1 where p.id=?2")
    void updateByPhotoNull(String id, Integer pid);

    @Modifying
    @Transactional
    @Query("update  ProfileEntity p set p.status=?1 where p.id=?2")
    void updateStatusById(ProfileStatus status, Integer pId);
}
