package com.company.service;


import com.company.dto.CommentFilterDTO;
import com.company.dto.CommentResponseDTO;
import com.company.entity.CommentEntity;
import com.company.repository.custom.CustomCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommetFilterService {

    @Autowired
    private CustomCommentRepository customCommentRepository;


    public List<CommentResponseDTO> filter(CommentFilterDTO dto) {

        List<CommentEntity> filter = customCommentRepository.filter(dto);
        List<CommentResponseDTO> dtoList = new LinkedList<>();

        filter.forEach(commentEntity -> {
            CommentResponseDTO responseDTO = new CommentResponseDTO();
            responseDTO.setId(commentEntity.getId());
            responseDTO.setCreatedDate(commentEntity.getCreatedDate());
            responseDTO.setUpdateDate(commentEntity.getUpdateDate());
            responseDTO.setArticleId(commentEntity.getArticle().getId());
            responseDTO.setContent(commentEntity.getContent());
            responseDTO.setProfileId(commentEntity.getProfile().getId());
            responseDTO.setReplyId(commentEntity.getReplaseId());

            dtoList.add(responseDTO);
        });
        return dtoList;
    }

}
