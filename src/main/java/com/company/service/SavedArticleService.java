package com.company.service;


import com.company.dto.AttachDTO;
import com.company.dto.SavedArticleDTO;
import com.company.dto.article.ArticleDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SavedArticleEntity;
import com.company.enums.ArticleStatus;
import com.company.exps.AlreadyExist;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.ArticleRepository;
import com.company.repository.ProfileRepository;
import com.company.repository.SavedArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SavedArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SavedArticleRepository savedArticleRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Value("${attach.folder}")
    private String attachFolder;


    @Value("${server.url}")
    private String serverUrl;

    public SavedArticleDTO create(SavedArticleDTO dto, Integer pId) {

        Optional<ArticleEntity> article =
                articleRepository.findByIdAndVisibleTrueAndStatus(dto.getArticleId(), ArticleStatus.PUBLISHED);
        if (article.isEmpty()) {
            throw new ItemNotFoundEseption("not found arricle");
        }

        ArticleEntity articleEntity = article.get();

        if (savedArticleRepository.existsByArticleAndProfile(articleEntity, new ProfileEntity(pId))) {
            throw new AlreadyExist("already mazgios");
        }

        SavedArticleEntity entity = new SavedArticleEntity();
        entity.setArticle(article.get());
        entity.setProfile(new ProfileEntity(pId));
        entity.setCreatedDate(LocalDateTime.now());

        savedArticleRepository.save(entity);


        SavedArticleDTO response = new SavedArticleDTO();
        response.setId(entity.getId());
        response.setArticleId(entity.getArticle().getId());
        response.setProfileId(entity.getProfile().getId());


        return response;

    }

    public String delete(Integer id, Integer profileId) {

        Optional<SavedArticleEntity> savedArticleEntity =
                savedArticleRepository.findByIdAndAndProfile(id, new ProfileEntity(profileId));

        if (savedArticleEntity.isEmpty()) {
            throw new ItemNotFoundEseption("not found savedArticle");
        } else if (!savedArticleEntity.get().getProfile().getId().equals(profileId)) {
            throw new BadRequestException("not acsess");
        }

        savedArticleRepository.deleteById(id);
        return "sucsess";

    }

    public List<SavedArticleDTO> getList(Integer profileId) {

        List<SavedArticleEntity> all = savedArticleRepository.findAllByProfileId(profileId);
        List<SavedArticleDTO> dtoList = new LinkedList<>();

        all.forEach(savedArticleEntity -> {
            SavedArticleDTO dto = new SavedArticleDTO();
            dto.setId(savedArticleEntity.getId());

            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(savedArticleEntity.getArticle().getId());
            articleDTO.setTitle(savedArticleEntity.getArticle().getTitle());
            articleDTO.setDescription(savedArticleEntity.getArticle().getDescription());

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(savedArticleEntity.getArticle().getImage().getId());
            attachDTO.setUrl(serverUrl + "" + "attache/open/" + savedArticleEntity.getArticle().getImage().getId());

            articleDTO.setAttachDTO(attachDTO);

            dto.setArticle(articleDTO);
            dtoList.add(dto);
        });

        return dtoList;
    }
}
