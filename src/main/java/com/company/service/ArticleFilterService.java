package com.company.service;


import com.company.dto.ArticleFilterDTO;
import com.company.dto.article.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.repository.custom.CustomArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleFilterService {

    @Autowired
    private CustomArticleRepository customArticleRepository;


    @Autowired
    private ArticleService articleService;

    public List<ArticleDTO> filter(ArticleFilterDTO dto) {

        List<ArticleEntity> filter = customArticleRepository.filter(dto);

        List<ArticleDTO> dtoList = new LinkedList<>();

        filter.forEach(entity -> {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(entity.getId());
            articleDTO.setTitle(entity.getTitle());
            articleDTO.setDescription(entity.getDescription());
            articleDTO.setPublishDate(entity.getPublishDate());
            dtoList.add(articleDTO);
        });
        return dtoList;


    }


}
