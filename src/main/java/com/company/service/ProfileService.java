package com.company.service;


import com.company.dto.ProfileDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exps.AlreadyExist;
import com.company.exps.AlreadyExistPhone;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.AttachRepository;
import com.company.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AttachService attachService;

    @Autowired
    private AttachRepository attachRepository;

    //register by admin
    public ProfileDTO create(ProfileDTO profileDto) {

        Optional<ProfileEntity> entity = profileRepository.findByEmail(profileDto.getEmail());
        if (entity.isPresent()) {
            throw new AlreadyExistPhone("Already exist email");
        }

        isValid(profileDto);

        ProfileEntity profile = new ProfileEntity();
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurname());
        profile.setEmail(profileDto.getEmail());
        profile.setPassword(profileDto.getPassword());
        profile.setRole(profileDto.getRole());
        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setId(profile.getId());
        responseDTO.setName(profileDto.getName());
        responseDTO.setSurname(profileDto.getSurname());
        responseDTO.setEmail(profileDto.getEmail());
        responseDTO.setPassword(null);
        responseDTO.setRole(profileDto.getRole());
        return responseDTO;
    }

    public List<ProfileDTO> getList() {

        Iterable<ProfileEntity> all = profileRepository.findAll();
        List<ProfileDTO> dtoList = new LinkedList<>();

        all.forEach(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurname(profileEntity.getSurname());
            dto.setEmail(profileEntity.getEmail());
            dtoList.add(dto);
        });
        return dtoList;
    }

    public void update(Integer pId, ProfileDTO dto) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()) {
            throw new ItemNotFoundEseption("not found profile");
        }

        isValidUpdate(dto);

        ProfileEntity profileEntity = profile.get();


        if (profileEntity.getPhoto() == null && dto.getPhotoId() != null) {
            profileEntity.setPhoto(new AttachEntity(dto.getPhotoId()));
        } else if (profileEntity.getPhoto() != null && dto.getPhotoId() == null) {
            profileRepository.updateByPhotoNull(null, pId);
            attachService.delete(profileEntity.getPhoto().getId());
        } else if (profileEntity.getPhoto() != null && dto.getPhotoId() != null &&
                !profileEntity.getPhoto().getId().equals(dto.getPhotoId())) {


            profileEntity.setPhoto(new AttachEntity(dto.getPhotoId()));
            profileRepository.updateByPhoto(dto.getPhotoId(), pId);
            attachService.delete(profileEntity.getPhoto().getId());
        }


        profileEntity.setName(dto.getName());
        profileEntity.setSurname(dto.getSurname());
        profileEntity.setEmail(dto.getEmail());
        profileRepository.save(profileEntity);

    }

    public void delete(Integer id) {
        Optional<ProfileEntity> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            throw new ItemNotFoundEseption("not found profile");
        }
        if (!profile.get().getVisible()) {
            throw new AlreadyExist("IsVisible False edi");
        }

        profile.get().setVisible(Boolean.FALSE);

        profileRepository.save(profile.get());
    }

    private void isValidUpdate(ProfileDTO dto) {

        if (dto.getName() == null || dto.getName().length() < 3) {
            throw new BadRequestException("wrong name");
        }

        if (dto.getSurname() == null || dto.getSurname().length() < 4) {
            throw new BadRequestException("surname required.");
        }

        if (dto.getEmail() == null || dto.getEmail().length() < 3) {
            throw new BadRequestException("email required.");
        }


    }

    private void isValid(ProfileDTO dto) {

        if (dto.getName() == null || dto.getName().length() < 3) {
            throw new BadRequestException("wrong name");
        }

        if (dto.getSurname() == null || dto.getSurname().length() < 4) {
            throw new BadRequestException("surname required.");
        }

        if (dto.getEmail() == null || dto.getEmail().length() < 3) {
            throw new BadRequestException("email required.");
        }


    }

    public PageImpl pagination(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProfileEntity> all = profileRepository.findAll(pageable);

        List<ProfileEntity> list = all.getContent();

        List<ProfileDTO> dtoList = new LinkedList<>();

        list.forEach(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurname(profileEntity.getSurname());
            dto.setEmail(profileEntity.getEmail());
            dtoList.add(dto);
        });


        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public ProfileRole getRole(Integer id) {
        Optional<ProfileEntity> entity = profileRepository.findById(id);
        return entity.get().getRole();
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findByIdAndStatusAndVisibleTrue(id, ProfileStatus.ACTIVE).orElseThrow(() -> {
            throw new ItemNotFoundEseption("Profile not found");
        });
    }



}

