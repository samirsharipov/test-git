package com.company.service;


import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.enums.LangEnum;
import com.company.exps.AlreadyExist;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO regionDto) {

        Optional<RegionEntity> region = regionRepository.findByKey(regionDto.getKey());

        if (region.isPresent()) {
            throw new AlreadyExist("Already exist");
        }

        isValid(regionDto);

        RegionEntity regionEntity = getRegionEntity(regionDto);
        regionRepository.save(regionEntity);
        regionDto.setId(regionEntity.getId());
        return regionDto;

    }

    public List<RegionDTO> getList(LangEnum lang) {

        Iterable<RegionEntity> all = regionRepository.findAllByVisible(true);
        List<RegionDTO> dtoList = new LinkedList<>();

        all.forEach(regionEntity -> {
            dtoList.add(langRegion(regionEntity, lang));
        });
        return dtoList;
    }

    public List<RegionDTO> getListOnlyForAdmin(LangEnum lang) {

        Iterable<RegionEntity> all = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();

        all.forEach(regionEntity -> {
            dtoList.add(langRegion(regionEntity, lang));
        });
        return dtoList;
    }

    public RegionDTO update(Integer id, RegionDTO dto) {
        Optional<RegionEntity> regionEntity = regionRepository.findById(id);

        if (regionEntity.isEmpty()) {
            throw new ItemNotFoundEseption("not found region");
        }

        if (regionEntity.get().getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("is visible false");
        }

       RegionEntity entity = regionEntity.get();
        /*entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());*/
        getRegionEntity(entity, dto);
        regionRepository.save(entity);
        return dto;
    }

    public void delete(Integer id) {

        Optional<RegionEntity> regionEntity = regionRepository.findById(id);

        if (regionEntity.isEmpty()) {
            throw new ItemNotFoundEseption("not found region");
        }

        if (regionEntity.get().getVisible().equals(Boolean.FALSE)) {
            throw new AlreadyExist("this region already visible false");
        }

        RegionEntity region = regionEntity.get();

        region.setVisible(Boolean.FALSE);

        regionRepository.save(region);
    }

    public RegionEntity get(Integer regionId) {
        return regionRepository.findByIdAndVisibleTrue(regionId).orElseThrow(() -> {
            throw new ItemNotFoundEseption("Region not found");
        });
    }

    public PageImpl pagination(int page, int size, LangEnum lang) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RegionEntity> all = regionRepository.findAll(pageable);
        List<RegionEntity> list = all.getContent();
        List<RegionDTO> dtoList = new LinkedList<>();
        list.forEach(regionEntity -> {
            dtoList.add(langRegion(regionEntity, lang));
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public RegionDTO toDTO(RegionEntity regionEntity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(regionEntity.getId());
        dto.setKey(regionEntity.getKey());
        dto.setNameUz(regionEntity.getNameUz());
        dto.setNameRu(regionEntity.getNameRu());
        dto.setNameEn(regionEntity.getNameEn());
        return dto;
    }

    private void isValid(RegionDTO dto) {
        if (dto.getKey().length() < 5) {
            throw new BadRequestException("key to short");
        }

        if (dto.getNameUz() == null || dto.getNameUz().length() < 3) {
            throw new BadRequestException("wrong name uz");
        }

        if (dto.getNameRu() == null || dto.getNameRu().length() < 3) {
            throw new BadRequestException("wrong name ru");
        }

        if (dto.getNameEn() == null || dto.getNameEn().length() < 3) {
            throw new BadRequestException("wrong name en");
        }
    }

    private RegionEntity getRegionEntity(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

    private RegionEntity getRegionEntity(RegionEntity entity, RegionDTO dto) {
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

    private RegionDTO langRegion(RegionEntity regionEntity, LangEnum lang) {

        RegionDTO dto = new RegionDTO();
        dto.setId(regionEntity.getId());
        dto.setKey(regionEntity.getKey());
        switch (lang) {
            case ru:
                dto.setName(regionEntity.getNameRu());
                break;
            case en:
                dto.setName(regionEntity.getNameEn());
                break;
            case uz:
                dto.setName(regionEntity.getNameUz());
                break;
            default:
                dto.setName(regionEntity.getNameUz());
                break;
        }
        return dto;
    }



    public RegionDTO get1(RegionEntity entity, LangEnum lang) {
        RegionDTO dto = new RegionDTO();
        dto.setKey(entity.getKey());
        switch (lang) {
            case ru:
                dto.setName(entity.getNameRu());
                break;
            case en:
                dto.setName(entity.getNameEn());
                break;
            case uz:
                dto.setName(entity.getNameUz());
                break;
        }
        return dto;
    }
}
