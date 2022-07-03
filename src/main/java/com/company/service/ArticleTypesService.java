package com.company.service;


import com.company.dto.TypesDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTypeEntity;
import com.company.entity.TypesEntity;

import com.company.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTypesService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    @Autowired
    private TypesService typesService;

    public void create(ArticleEntity article, List<Integer> typesList){

        for (Integer typesId : typesList) {
            ArticleTypeEntity articleTypeEntity =new ArticleTypeEntity();

            articleTypeEntity.setArticle(article);
            articleTypeEntity.setTypes(new TypesEntity(typesId));
            articleTypeRepository.save(articleTypeEntity);
        }

    }

//    public void create(ArticleEntity article, List<Integer> typeList){
//
//        typeList.forEach(integer -> {
//            ArticleTypeEntity articleTypeEntity = new ArticleTypeEntity();
//            articleTypeEntity.setArticle(article);
//            Optional<TypesEntity> optional = typeRepository.findById(integer);
//            if (optional.isEmpty()){
//                throw new ItemNotFoundException("Type not found");
//            }
//            articleTypeEntity.setTypes(optional.get());
//            articleTypeRepository.save(articleTypeEntity);
//        });
//    }

    public List<TypesDTO> getTypeByArticle(ArticleEntity entity) {

        List<ArticleTypeEntity> list = articleTypeRepository.findAllByArticle(entity);

        List<TypesDTO> typeDTOList = new ArrayList<>();

        list.forEach(articleTypeEntity -> {
            TypesDTO typeDTO = typesService.getTypeDTO(articleTypeEntity.getTypes());
            typeDTOList.add(typeDTO);
        });

        return typeDTOList;

    }

}
