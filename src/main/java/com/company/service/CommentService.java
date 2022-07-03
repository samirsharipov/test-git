package com.company.service;


import com.company.dto.CommentDTO;
import com.company.dto.CommentProfileArticleDTO;
import com.company.dto.CommentProfileDTO;
import com.company.dto.ProfileByCommentDTO;
import com.company.dto.article.ArticleCommentDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.ProfileRole;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.mapper.CommentShortInfoByArticle;
import com.company.mapper.CommentShortInfoByArticlePagination;
import com.company.repository.ArticleRepository;
import com.company.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProfileService profileService;

    public CommentDTO create(CommentDTO dto, Integer profileId) {

        Optional<ArticleEntity> article = articleRepository.findById(dto.getArticle_id());

        if (article.isEmpty()) {
            throw new ItemNotFoundEseption("not found article");
        } else if (!article.get().getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new BadRequestException("not publish");
        }

        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticle(article.get());
        entity.setProfile(new ProfileEntity(profileId));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(Boolean.TRUE);
        commentRepository.save(entity);

        return toDTOCom(entity);

    }

    public List<CommentDTO> getListOnlyForAdmin() {

        Iterable<CommentEntity> all = commentRepository.findAll();
        List<CommentDTO> dtoList = new LinkedList<>();

        all.forEach(commentEntity -> {
            dtoList.add(toDTOCom(commentEntity));
        });
        return dtoList;
    }

    public CommentDTO update(Integer id, Integer pId, CommentDTO dto) {
        Optional<CommentEntity> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ItemNotFoundEseption("not found comment");
        } else if (!comment.get().getProfile().getId().equals(pId)) {
            throw new BadRequestException("not acses");
        }

        CommentEntity entity = comment.get();
        entity.setContent(dto.getContent());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);

        return toDTOCom(entity);

    }

    public void delete(Integer pId, Integer id) {

        Optional<CommentEntity> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ItemNotFoundEseption("not found comment");
        }else if (comment.get().getVisible().equals(Boolean.FALSE)){
            throw new BadRequestException("Alredy visible false");
        }else if (!comment.get().getProfile().getId().equals(pId) || !profileService.getRole(pId).equals(ProfileRole.ADMIN)){
            CommentEntity entity = comment.get();
            entity.setVisible(Boolean.FALSE);
            commentRepository.save(entity);
            return;
        }

        throw new ItemNotFoundEseption("not acsess");


    }

    private CommentDTO toDTOCom(CommentEntity entity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(entity.getId());
        commentDTO.setArticle_id(entity.getArticle().getId());
        commentDTO.setContent(entity.getContent());
        commentDTO.setProfile_id(entity.getProfile().getId());
        return commentDTO;
    }

    public boolean get(Integer comId) {

        Optional<CommentEntity> entity = commentRepository.findById(comId);
        if (entity.get().getVisible().equals(Boolean.TRUE)) {
            return true;
        }
        return false;
    }

    public List<CommentProfileDTO> getArticleCommentListByArticleId(String article_id) {

        List<CommentShortInfoByArticle> list = commentRepository.getArticleCommentListByArticleId(article_id);
        List<CommentProfileDTO> dtoList = new LinkedList<>();

        list.forEach(commentShortInfoByArticle -> {
            CommentProfileDTO dto=new CommentProfileDTO();
            dto.setId(commentShortInfoByArticle.getId());
            dto.setCreatedDate(commentShortInfoByArticle.getCreatedDate());
            dto.setUpdatedDate(commentShortInfoByArticle.getUpdateDate());

            ProfileByCommentDTO profileByCommentDTO=new ProfileByCommentDTO();
            profileByCommentDTO.setId(commentShortInfoByArticle.getprofileId());
            profileByCommentDTO.setName(commentShortInfoByArticle.getName());
            profileByCommentDTO.setSurname(commentShortInfoByArticle.getSurname());

            dto.setProfile(profileByCommentDTO);

            dtoList.add(dto);
        });
        return dtoList;
    }

    public List<CommentProfileArticleDTO> commentListPagination() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<CommentShortInfoByArticlePagination> page = commentRepository.commentListPagination(pageable);
        List<CommentProfileArticleDTO> dtoList = new LinkedList<>();

        page.forEach(commentShortInfoByArticlePagination -> {
            CommentProfileArticleDTO dto=new CommentProfileArticleDTO();
            dto.setId(commentShortInfoByArticlePagination.getId());
            dto.setCreatedDate(commentShortInfoByArticlePagination.getCreatedDate());
            dto.setUpdatedDate(commentShortInfoByArticlePagination.getUpdateDate());
            dto.setContent(commentShortInfoByArticlePagination.getContent());
            dto.setReplaseId(commentShortInfoByArticlePagination.getReplaseId());

            ProfileByCommentDTO profileByCommentDTO=new ProfileByCommentDTO();
            profileByCommentDTO.setId(commentShortInfoByArticlePagination.getprofileId());
            profileByCommentDTO.setName(commentShortInfoByArticlePagination.getName());
            profileByCommentDTO.setSurname(commentShortInfoByArticlePagination.getSurname());

            ArticleCommentDTO articleCommentDTO =new ArticleCommentDTO();
            articleCommentDTO.setId(commentShortInfoByArticlePagination.getArticleId());
            articleCommentDTO.setTitle(commentShortInfoByArticlePagination.getTitle());

            dto.setProfile(profileByCommentDTO);
            dto.setArticle(articleCommentDTO);

            dtoList.add(dto);
        });
        return dtoList;

    }
}
