package com.company.service;


import com.company.dto.AttachDTO;
import com.company.dto.TypeAndRegionDTO;
import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleLikeDTO;
import com.company.dto.article.ArticleUpdateDTO;
import com.company.entity.*;
import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.exps.AlreadyExist;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.mapper.ArticleShortInfoByCategory;
import com.company.repository.ArticleRepository;
import com.company.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTypesService articleTypesService;

    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleLikeService articleLikeService;

    @Autowired
    private AttachService attachService;

    @Value("${attach.folder}")
    private String attachFolder;


    @Value("${server.url}")
    private String serverUrl;

    public ArticleDTO create(ArticleCreateDTO dto, Integer profileId) {

        Optional<ArticleEntity> articleEntity = articleRepository.findByTitleAndContent(dto.getTitle(), dto.getContent());
        if (articleEntity.isPresent()) {
            throw new AlreadyExist("already exist");
        }

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());

        RegionEntity region = regionService.get(dto.getRegionId());
        entity.setRegion(region);

        CategoryEntity category = categoryService.get(dto.getCategoryId());
        entity.setCategory(category);

        ProfileEntity moderator = new ProfileEntity();
        moderator.setId(profileId);
        entity.setModerator(moderator);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);

        AttachEntity attachEntity = attachService.get(dto.getImageId());
        entity.setImage(attachEntity);

        articleRepository.save(entity);

        articleTypesService.create(entity, dto.getTypesList());

        articleTagService.create(entity, dto.getTagList());

        ArticleDTO responseDTO = new ArticleDTO();
        responseDTO.setTitle(entity.getTitle());
        responseDTO.setContent(entity.getContent());
        responseDTO.setDescription(entity.getDescription());
        responseDTO.setCategory(categoryService.toDTO(entity.getCategory()));
        responseDTO.setRegion(regionService.toDTO(entity.getRegion()));
        responseDTO.setTagList(articleTagService.getTagListByArticle(entity));
        responseDTO.setTypesList(articleTypesService.getTypeByArticle(entity));

        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getImage().getId());
        attachDTO.setUrl(serverUrl + "" + "attache/open/" + entity.getImage().getId());

        responseDTO.setAttachDTO(attachDTO);

        return responseDTO;

    }

    public String update(String id, ArticleUpdateDTO dto) {
        Optional<ArticleEntity> entity = articleRepository.findById(id);

        if (entity.isEmpty()) {
            throw new ItemNotFoundEseption("not found article");
        }

        if (entity.get().getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("is visible false");
        }

        ArticleEntity articleEntity = entity.get();


        if (articleEntity.getImage() == null && dto.getImageId() != null) {
            articleEntity.setImage(new AttachEntity(dto.getImageId()));
            articleRepository.save(articleEntity);
        } else if (articleEntity.getImage() != null && dto.getImageId() == null) {
            articleEntity.setImage(null);
            articleRepository.updateByPhotoNull(null, articleEntity.getId());
            attachService.delete(articleEntity.getImage().getId());
        } else if (articleEntity.getImage() != null && dto.getImageId() != null &&
                !articleEntity.getImage().getId().equals(dto.getImageId())) {

            articleEntity.setImage(new AttachEntity(dto.getImageId()));
            articleRepository.save(articleEntity);
            attachService.delete(articleEntity.getImage().getId());
        }


        articleEntity.setTitle(dto.getTitle());
        articleEntity.setContent(dto.getContent());
        articleEntity.setDescription(dto.getDescription());

        articleRepository.save(articleEntity);

        return "sucssses";
    }


    public List<ArticleDTO> getListOnlyForAdmin() {

        Iterable<ArticleEntity> all = articleRepository.findAll();
        List<ArticleDTO> dtoList = new LinkedList<>();

        all.forEach(articleEntity -> {

            ArticleDTO responseDTO = new ArticleDTO();
            responseDTO.setId(articleEntity.getId());
            responseDTO.setTitle(articleEntity.getTitle());
            responseDTO.setContent(articleEntity.getContent());
            responseDTO.setDescription(articleEntity.getDescription());
            responseDTO.setCategory(categoryService.toDTO(articleEntity.getCategory()));
            responseDTO.setRegion(regionService.toDTO(articleEntity.getRegion()));
            responseDTO.setTagList(articleTagService.getTagListByArticle(articleEntity));
            responseDTO.setTypesList(articleTypesService.getTypeByArticle(articleEntity));
            dtoList.add(responseDTO);

        });
        return dtoList;
    }

    public void delete(String id) {
        Optional<ArticleEntity> entity = articleRepository.findById(id);

        if (entity.isEmpty()) {
            throw new ItemNotFoundEseption("not found article");
        }

        if (entity.get().getVisible().equals(Boolean.FALSE)) {
            throw new AlreadyExist("this article already visible false");
        }

        ArticleEntity articleEntity = entity.get();

        articleEntity.setVisible(Boolean.FALSE);

        articleRepository.save(articleEntity);
    }

    public void updateByStatusPulish(String articleId, Integer pId) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);

        if (optional.isEmpty()) {
            throw new ItemNotFoundEseption("Article not found ");
        }

        ArticleEntity articleEntity = optional.get();
        if (!articleEntity.getStatus().equals(ArticleStatus.NOT_PUBLISHED)) {
            throw new AlreadyExist("alredy publish");
        } else if (articleEntity.getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("visible false");
        }

        articleRepository.changeStatusToPublish(articleId, pId, ArticleStatus.PUBLISHED, LocalDateTime.now());

    }

    public void updateByStatusNotPulish(String articleId, Integer pId) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);

        if (optional.isEmpty()) {
            throw new ItemNotFoundEseption("Article not found ");
        }

        ArticleEntity articleEntity = optional.get();
        if (!articleEntity.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new AlreadyExist("alredy not_publish");
        }
        articleRepository.changeStatusNotPublish(articleId, ArticleStatus.NOT_PUBLISHED);
    }

    public List<ArticleDTO> getLast5ArticleListByArticleTypeKey(String articleTypeKey) {

        List<ArticleShortInfoByCategory> article = articleRepository.findTop5ByArticleNative(articleTypeKey);
        List<ArticleDTO> dtoList = new LinkedList<>();

        article.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLast3ArticlePulishTypeKey(String articleTypeKey) {

        List<ArticleEntity> articleEntityList = articleRepository.findTop3ByArticleNative(articleTypeKey);
        List<ArticleDTO> dtoList = new LinkedList<>();

        articleEntityList.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLat8ArticleNotIn(List<String> articleIdList) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast8NotIn(articleIdList, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;
    }

    public ArticleDTO getByIdFull(String id, LangEnum lang) {

 /*       Optional<ArticleEntity> entity =
                articleRepository.findByIdAndVisibleTrueAndStatus(id, ArticleStatus.PUBLISHED);

        ArticleEntity articleEntity = entity.get();

        articleEntity.setViewCount(articleEntity.getViewCount() +1);

        articleRepository.save(articleEntity);

        return fullDTOInfo(articleEntity);*/

        Optional<ArticleEntity> optional = articleRepository.getPublishedById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundEseption("Article Not Found");
        }

        ArticleEntity entity = optional.get();
        ArticleDTO dto = fullDTO(entity);

        dto.setRegion(regionService.get1(entity.getRegion(), lang));
        dto.setCategory(categoryService.get1(entity.getCategory(), lang));

        ArticleLikeDTO likeDTO = articleLikeService.likeCountAndDislikeCount(entity.getId());
        dto.setLike(likeDTO);

        dto.setPublishDate(entity.getPublishDate());
        dto.setViewCount(entity.getViewCount());

        dto.setTagList(articleTagService.getTagListNameByArticle(entity));

//        dto.setTagList(articleTagService.getTagListNameByArticle(entity));
        return dto;

    }

    public List<ArticleDTO> gatMostFourNotIn(String articleTypeKey, String id) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast4TypeNotIn(articleTypeKey, id, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;

    }

    public List<ArticleDTO> findMost4ViewedArticleList() {

        List<ArticleShortInfoByCategory> list = articleRepository.findMost4ViewedArticleList();

        List<ArticleDTO> dtoList = new LinkedList<>();
        list.forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;


    }

    //ishlamadi yana koraman
/*    public List<ArticleDTO> gatLast4TagName(String tagName) {
        Pageable pageable = PageRequest.of(0, 5);
        String subTag=tagName.concat("#");
        Page<ArticleEntity> articlePage = articleRepository.getLast4ArticleByTagName(subTag, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;

    }*/

    public List<ArticleDTO> getTag(String tagName) {
        String s = "#";
        String subTag = s.concat(tagName);
        List<ArticleShortInfoByCategory> tag = articleRepository.tagName(subTag);
        List<ArticleDTO> dtoList = new LinkedList<>();

        tag.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLast5ArticleByTypesAndByRegion(TypeAndRegionDTO dto) {

        List<ArticleShortInfoByCategory> all = articleRepository.getLast5ArticleByTypesAndByRegionKey(dto.getTypeKey(), dto.getRegionKey());
        List<ArticleDTO> dtoList = new LinkedList<>();

        all.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });
        return dtoList;

    }

    public List<ArticleDTO> getRegionKey(String regionKey) {

        Pageable pageable = PageRequest.of(0, 3);
        Page<ArticleEntity> articlePage = articleRepository.getArticlelistByRegionKey(regionKey, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;


    }

    public List<ArticleDTO> getLast5CategoryList(String categoryKey) {

        List<ArticleShortInfoByCategory> articleCategory = articleRepository.findby5ArticleCategoryKeyNative(categoryKey);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articleCategory.forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLast5CategoryPagination(String categoryKey) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> category = articleRepository.getLastCategory(categoryKey, pageable);
        List<ArticleDTO> dtoList = new LinkedList<>();

        category.forEach(articleEntity -> {
            dtoList.add(shortDTOInfo(articleEntity));
        });
        return dtoList;
    }
/////////////////
/*    public List<ArticleDTO> getLast5ArticleByCategory(String categoryKey) {
        CategoryEntity category = categoryService.get(categoryKey);

        List<ArticleEntity> articleList = articleRepository.findTop5ByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(
                category, ArticleStatus.PUBLISHED);
        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });

        return dtoList;
    }*/
    ///////////////////////////

//yana bir yoli
/*    public List<ArticleDTO> getLast5ArticleByCategory2(String categoryKey) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast5ByCategory(
                categoryKey, ArticleStatus.PUBLISHED, pageable);
        int n =  articlePage.getTotalPages();

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortDTOInfo(article));
        });
        return dtoList;
    }*/

/////////////////////////
    //5
/*public List<ArticleDTO> getLast5ArticleByType(String typeKey) {
    Pageable pageable = PageRequest.of(0, 5);
    Page<ArticleEntity> articlePage = articleRepository.findLast5ByType(
            typeKey, pageable);

    List<ArticleDTO> dtoList = new LinkedList<>();
    articlePage.getContent().forEach(article -> {
        dtoList.add(shortDTOInfo(article));
    });
    return dtoList;
}*/


    private ArticleDTO shortDTOInfo(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setViewCount(entity.getViewCount());
        dto.setDescription(entity.getDescription());
        dto.setPublishDate(entity.getPublishDate());
        // TODO image
        return dto;
    }

    public ArticleDTO shortDTOInfo(ArticleShortInfoByCategory entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishDate(entity.getPublishDate());
        // TODO image
        return dto;
    }

    private ArticleDTO fullDTO(ArticleEntity entity) {
/*        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());

        RegionEntity region = regionService.get(entity.getRegion().getId());
        RegionDTO regionDTO = new RegionDTO(region.getKey(), region.getNameUz());
        dto.setRegion(regionDTO);

        CategoryEntity category = categoryService.get(entity.getCategory().getId());
        CategoryDTO categoryDTO = new CategoryDTO(category.getKey(), category.getNameUz());
        dto.setCategory(categoryDTO);

        dto.setPublishDate(entity.getPublishDate());
        dto.setViewCount(entity.getViewCount());

        Integer likeCount = articleLikeService.likeCount(entity.getId());
        dto.setLikeCount(likeCount);

        dto.setTagList(articleTagService.getTagListNameByArticle(entity));
        return dto;*/

        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setSharedCount(entity.getSharedCount());
        dto.setPublishDate(entity.getPublishDate());
        dto.setViewCount(entity.getViewCount());

        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getImage().getId());
        attachDTO.setUrl(serverUrl + "" + "attache/open/" + entity.getImage().getId());

        dto.setAttachDTO(attachDTO);
        return dto;

    }


    public void increaseArticleViewCountbyArticleId(String article_id) {
        Optional<ArticleEntity> optional = articleRepository.getPublishedById(article_id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundEseption("Article Not Found");
        }

        ArticleEntity articleEntity = optional.get();

        articleRepository.updtecount(article_id, articleEntity.getViewCount() + 1);

    }
}
