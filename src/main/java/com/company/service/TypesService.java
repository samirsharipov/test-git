package com.company.service;

import com.company.dto.TypesDTO;
import com.company.entity.TypesEntity;
import com.company.enums.LangEnum;
import com.company.exps.AlreadyExist;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;

import com.company.repository.TypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TypesService {

    @Autowired
    private TypesRepository typesRepository;


    public TypesDTO create(TypesDTO typeDto) {

        Optional<TypesEntity> articleTypeEntity = typesRepository.findByKey(typeDto.getKey());

        if (articleTypeEntity.isPresent()) {
            throw new AlreadyExist("Already exist");
        }

        isValid(typeDto);
        TypesEntity entity = toEntity(typeDto);
        typesRepository.save(entity);
        typeDto.setId(entity.getId());
        return typeDto;
    }

    //public

    public List<TypesDTO> getList(LangEnum lang) {

        Iterable<TypesEntity> all = typesRepository.findAllByVisible(true);
        List<TypesDTO> dtoList = new LinkedList<>();

        all.forEach(typesEntity -> {
            dtoList.add(langTypes(typesEntity, lang));
        });
        return dtoList;
    }
    //admin

    public List<TypesDTO> getListOnlyForAdmin(LangEnum lang) {

        Iterable<TypesEntity> all = typesRepository.findAll();
        List<TypesDTO> dtoList = new LinkedList<>();

        all.forEach(typesEntity -> {
            dtoList.add(langTypes(typesEntity, lang));
        });
        return dtoList;
    }

    public TypesDTO update(Integer id, TypesDTO dto) {
        Optional<TypesEntity> articleTypeEntity = typesRepository.findById(id);

        if (articleTypeEntity.isEmpty()) {
            throw new ItemNotFoundEseption("not found type");
        }

        if (articleTypeEntity.get().getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("iloji yoq ");
        }

        TypesEntity entity = articleTypeEntity.get();

        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        typesRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void delete(Integer id) {

        Optional<TypesEntity> entity = typesRepository.findById(id);

        if (entity.isEmpty()) {
            throw new ItemNotFoundEseption("not found articleType");
        }

        if (entity.get().getVisible().equals(Boolean.FALSE)) {

            throw new AlreadyExist("this articleType already visible false");
        }

        TypesEntity articleType = entity.get();

        articleType.setVisible(Boolean.FALSE);

        typesRepository.save(articleType);
    }

    public PageImpl pagination(int page, int size, LangEnum lang) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TypesEntity> all = typesRepository.findAll(pageable);

        List<TypesEntity> list = all.getContent();

        List<TypesDTO> dtoList = new LinkedList<>();

        list.forEach(typesEntity -> {
            dtoList.add(langTypes(typesEntity, lang));
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public TypesDTO getTypeDTO(TypesEntity typesEntity) {

        TypesDTO typesDTO = new TypesDTO();
        typesDTO.setId(typesEntity.getId());
        typesDTO.setKey(typesEntity.getKey());
        typesDTO.setVisible(typesEntity.getVisible());
        typesDTO.setNameEn(typesEntity.getNameEn());
        typesDTO.setNameRu(typesEntity.getNameRu());
        typesDTO.setNameUz(typesEntity.getNameUz());
        typesDTO.setVisible(typesEntity.getVisible());
        return typesDTO;
    }

    private TypesEntity toEntity(TypesDTO dto) {
        TypesEntity entity = new TypesEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

    private TypesDTO langTypes(TypesEntity typesEntity, LangEnum lang) {
        TypesDTO dto = new TypesDTO();
        dto.setId(typesEntity.getId());
        dto.setKey(typesEntity.getKey());
        switch (lang) {
            case ru:
                dto.setName(typesEntity.getNameRu());
                break;
            case en:
                dto.setName(typesEntity.getNameEn());
                break;
            case uz:
                dto.setName(typesEntity.getNameUz());
                break;
            default:
                dto.setName(typesEntity.getNameUz());
                break;
        }
        return dto;
    }

    private void isValid(TypesDTO dto) {
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

}
