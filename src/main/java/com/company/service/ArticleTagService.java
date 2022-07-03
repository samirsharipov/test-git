package com.company.service;



import com.company.dto.TagDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTagEntity;
import com.company.entity.TagEntity;
import com.company.repository.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleTagService {

    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Autowired
    private TagService tagService;

    public void create(ArticleEntity article, List<String> taglist){
        for (String tagName : taglist) {
            TagEntity tag = tagService.createIfNotExsists(tagName);

            ArticleTagEntity articleTagEntity=new ArticleTagEntity();
            articleTagEntity.setArticle(article);
            articleTagEntity.setTag(tag);
            articleTagRepository.save(articleTagEntity);
        }
    }

    public List<TagDTO> getTagListByArticle(ArticleEntity article) {

        List<ArticleTagEntity> list = articleTagRepository.findAllByArticle(article);

        List<TagDTO> tagDTOList = new ArrayList<>();
        list.forEach(articleTagEntity -> {
            TagDTO tagDTO = tagService.getTagDTO(articleTagEntity.getTag());
            tagDTOList.add(tagDTO);
        });

        return tagDTOList;
    }


    public List<TagDTO> getTagListNameByArticle(ArticleEntity article) {

        List<ArticleTagEntity> list = articleTagRepository.findAllByArticle(article);

        List<TagDTO> tagDTOList = new ArrayList<>();
        list.forEach(articleTagEntity -> {
            TagDTO tagDTO = tagService.getTagDTONAME(articleTagEntity.getTag());
            tagDTOList.add(tagDTO);
        });

        return tagDTOList;
    }

}
