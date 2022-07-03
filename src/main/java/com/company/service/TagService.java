package com.company.service;

import com.company.dto.TagDTO;
import com.company.entity.TagEntity;
import com.company.enums.TagStatus;
import com.company.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagEntity create(String name){
         TagEntity tag=new TagEntity();
         tag.setName(name);
         tag.setStatus(TagStatus.ACTIVE);
         tagRepository.save(tag);
         return tag;
    }

    public TagEntity createIfNotExsists(String tagName){
        Optional<TagEntity> optional = tagRepository.findByName(tagName);
        if (optional.isEmpty()){
            return create(tagName);
        }
        return optional.get();
        //  return tagRepository.findByName(tagName).orElse(create(tagName));
    }


    public boolean isExists(String name){
        return tagRepository.existsByName(name);
    }

    public TagEntity getByName(String name){
        return tagRepository.findByName(name).orElse(null);
    }


    public TagDTO getTagDTO(TagEntity entity) {

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(entity.getId());
        tagDTO.setCreatedDate(entity.getCreatedDate());
        tagDTO.setName(entity.getName());
        tagDTO.setStatus(entity.getStatus());

        return tagDTO;
    }

    public TagDTO getTagDTONAME(TagEntity entity) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(entity.getName());
        return tagDTO;
    }
}
