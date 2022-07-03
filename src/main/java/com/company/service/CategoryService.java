package com.company.service;


import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.enums.LangEnum;
import com.company.exps.AlreadyExist;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO categoryDto) {

        Optional<CategoryEntity> category = categoryRepository.findByKey(categoryDto.getKey());

        if (category.isPresent()) {
            throw new AlreadyExist("Already exist");
        }

        isValid(categoryDto);
        CategoryEntity entity = categoryRepository.save(getCategoryEntity(categoryDto));
        categoryDto.setId(entity.getId());
        return categoryDto;
    }

    //public
    public List<CategoryDTO> getList(LangEnum lang) {

        Iterable<CategoryEntity> all = categoryRepository.findAllByVisible(true);
        List<CategoryDTO> dtoList = new LinkedList<>();

        all.forEach(categoryEntity -> {
           dtoList.add(toDTO(categoryEntity, lang));
        });
        return dtoList;
    }

    //admin
    public List<CategoryDTO> getListOnlyForAdmin(LangEnum lang) {

        Iterable<CategoryEntity> all = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();

        all.forEach(categoryEntity -> {
          dtoList.add(toDTO(categoryEntity, lang));
        });
        return dtoList;
    }

    public CategoryDTO update(Integer id, CategoryDTO dto) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);

        if (categoryEntity.isEmpty()) {
            throw new ItemNotFoundEseption("not found category");
        }else if (categoryEntity.get().getVisible().equals(Boolean.FALSE)){
            throw new BadRequestException("visible false");
        }

        CategoryEntity entity = categoryEntity.get();
        CategoryEntity category = getCategoryEntity(entity, dto);
        categoryRepository.save(category);
        dto.setId(category.getId());
        return dto;
    }

    public void delete(Integer id) {

        Optional<CategoryEntity> entity = categoryRepository.findById(id);

        if (entity.isEmpty()) {
            throw new ItemNotFoundEseption("not found category");
        }

        if (entity.get().getVisible().equals(Boolean.FALSE)) {
            throw new AlreadyExist("this category already visible false");
        }

        CategoryEntity category = entity.get();

        category.setVisible(Boolean.FALSE);

        categoryRepository.save(category);
    }

    public CategoryEntity get(Integer categoryId) {
        return categoryRepository.findByIdAndVisibleTrue(categoryId).orElseThrow(() -> {
            throw new ItemNotFoundEseption("Category not found or visible false");
        });
    }

    public CategoryDTO toDTO(CategoryEntity categoryEntity, LangEnum lang) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(categoryEntity.getId());
        dto.setKey(categoryEntity.getKey());
        switch (lang) {
            case ru:
                dto.setName(categoryEntity.getNameRu());
                break;
            case en:
                dto.setName(categoryEntity.getNameEn());
                break;
            case uz:
                dto.setName(categoryEntity.getNameUz());
                break;
            default:
                dto.setName(categoryEntity.getNameUz());
                break;
        }
        return dto;
    }

    public CategoryDTO toDTO(CategoryEntity categoryEntity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(categoryEntity.getId());
        dto.setKey(categoryEntity.getKey());
        dto.setName(categoryEntity.getNameRu());
        dto.setName(categoryEntity.getNameEn());
        dto.setName(categoryEntity.getNameUz());
        return dto;
    }

    private void isValid(CategoryDTO dto) {
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

    private CategoryEntity getCategoryEntity(CategoryEntity entity, CategoryDTO dto) {
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

    private CategoryEntity getCategoryEntity( CategoryDTO dto) {
        CategoryEntity entity=new CategoryEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        return entity;
    }

    public CategoryDTO get1(CategoryEntity entity, LangEnum lang) {
        CategoryDTO dto = new CategoryDTO();
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
