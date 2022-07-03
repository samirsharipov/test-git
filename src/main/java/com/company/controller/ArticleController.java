package com.company.controller;



import com.company.dto.ArticleFilterDTO;
import com.company.dto.TypeAndRegionDTO;
import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleRequestDTO;
import com.company.dto.article.ArticleUpdateDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleFilterService;
import com.company.service.ArticleService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "Article.....")
@RequestMapping("/article")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleFilterService filterService;


    @ApiOperation(value = "Create", notes = "Method for create")
    @PostMapping("/adm")
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleCreateDTO dto,
                                             HttpServletRequest request) {
        log.info("Request for login {}", dto);
        log.warn("Request for login {}", dto);

        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        ArticleDTO response = articleService.create(dto, profileId);
        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(value = "Update", notes = "Method for update")
    @PutMapping("/adm/{id}")
    private ResponseEntity<?> update(@PathVariable("id") String id,
                                     @RequestBody ArticleUpdateDTO dto,
                                     HttpServletRequest request) {

        log.info("Request for login {}", dto);
        log.warn("Request for login {}", dto);
        HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        String update = articleService.update(id, dto);
        return ResponseEntity.ok().body(update);
    }

    @ApiOperation(value = "Article List", notes = "Method for get list")
    @GetMapping("/adm")
    public ResponseEntity<List<ArticleDTO>> getlist(HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<ArticleDTO> list = articleService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Delete", notes = "Method for delete")
    @DeleteMapping("/adm/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") String id,
                                     HttpServletRequest request) {
        log.info("Request for login {}", id);
        log.warn("Request for login {}", id);
        HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        articleService.delete(id);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }

    @ApiOperation(value = "Article Publish", notes = "Method for Article Publish")
    @PutMapping("/adm/publish/{id}")
    private ResponseEntity<?> changeStatusPublishedDate(@PathVariable("id") String id,
                                                        HttpServletRequest request) {
        log.info("Request for login {}", id);
        log.warn("Request for login {}", id);

        Integer moderId = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.updateByStatusPulish(id, moderId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Article Not-Publish", notes = "Method for Article Not-Publish")
    @PutMapping("/adm/notPublish/{id}")
    private ResponseEntity<?> changeStatusNotPublishedDate(@PathVariable("id") String id,
                                                           HttpServletRequest request) {
        log.info("Request for login {}", id);
        log.warn("Request for login {}", id);

        Integer moderId = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.updateByStatusNotPulish(id, moderId);
        return ResponseEntity.ok().build();
    }

    //5
    @ApiOperation(value = "Last by 5 Type article", notes = "Method for Last by 5 Type article get")
    @GetMapping("/typeLast5/{articleTypeKey}")
    public ResponseEntity<List<ArticleDTO>> getLast5ArticleList(@PathVariable("articleTypeKey")
                                                                        String articleTypeKey) {
        log.info("Request for login {}", articleTypeKey);
        log.warn("Request for login {}", articleTypeKey);

        List<ArticleDTO> list = articleService.getLast5ArticleListByArticleTypeKey(articleTypeKey);
        return ResponseEntity.ok().body(list);
    }

    //6
    @ApiOperation(value = "Last by 3 Type article", notes = "Berilgan types bo'yicha oxirgi 3ta pubished bo'lgan article ni return qiladi.")
    @GetMapping("/typeLast3/{articleTypeKey}")
    public ResponseEntity<List<ArticleDTO>> getLast3ArticlePulishList(@PathVariable("articleTypeKey")
                                                                              String articleTypeKey) {

        log.info("Request for login {}", articleTypeKey);
        log.warn("Request for login {}", articleTypeKey);

        List<ArticleDTO> list = articleService.getLast3ArticlePulishTypeKey(articleTypeKey);
        return ResponseEntity.ok().body(list);
    }

    //7
    @ApiOperation(value = "Last by 8  article", notes = "Get Last 8 Added articles witch id not included in given list")
    @GetMapping("/last8List")
    public ResponseEntity<List<ArticleDTO>> getLast8NotIn(@RequestBody ArticleRequestDTO dto) {

        log.info("Request for login {}", dto);
        log.warn("Request for login {}", dto);

        List<ArticleDTO> response = articleService.getLat8ArticleNotIn(dto.getIdList());
        return ResponseEntity.ok().body(response);
    }

    //8
    @ApiOperation(value = "Get Article By Id", notes = "Get Article By Id ")
    @GetMapping("/getByIdFull/{id}")
    public ResponseEntity<ArticleDTO> getByIdFull(@PathVariable("id") String id,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "ru") LangEnum lang) {

        log.info("Request for login {}", id);
        log.warn("Request for login {}", id);

        ArticleDTO response = articleService.getByIdFull(id, lang);
        return ResponseEntity.ok().body(response);
    }

    //9
    @ApiOperation(value = "Get Article By Types", notes = "Get Last 4 Article By Types and except given article id")
    @GetMapping("/gatFourNoTIn/{articleTypeKey}")
    public ResponseEntity<List<ArticleDTO>> gatMostFour(@PathVariable("articleTypeKey")
                                                                String articleTypeKey,
                                                        @RequestBody ArticleRequestDTO dto) {

        log.info("Request for login {}", articleTypeKey);
        log.warn("Request for login {}", articleTypeKey);

        List<ArticleDTO> response = articleService.gatMostFourNotIn(articleTypeKey, dto.getId());
        return ResponseEntity.ok().body(response);
    }

    //10
    @ApiOperation(value = "Get 4 most read articles", notes = "Eng ko'p o'qilgan maqola 4")
    @GetMapping("/getMost4ViewedArticleList")
    public ResponseEntity<?> getMost4ViewedArticleList() {




        List<ArticleDTO> articleDTOS = articleService.findMost4ViewedArticleList();
        return ResponseEntity.ok().body(articleDTOS);
    }

    //11
    @ApiOperation(value = "Get Last 4 Article By TagName", notes = "Bitta article ni eng ohirida chiqib turadi.")
    @GetMapping("/getLast4TagName/{tagName}")
    public ResponseEntity<List<ArticleDTO>> getLast4TagName(@PathVariable("tagName") String tagName) {

        log.info("Request f or login {}", tagName);
        log.warn("Request for login {}", tagName);

        List<ArticleDTO> list = articleService.getTag(tagName);
        return ResponseEntity.ok().body(list);
    }

    //12
    @ApiOperation(value = "Get Last 5 Article By Types  And By Region Key", notes = "Get Last 5 Article By Types  And By Region Key")
    @GetMapping("/getLast5ArticleByTypesAndByRegionKey")
    public ResponseEntity<List<ArticleDTO>> getLast5ArticleByTypesAndByRegion(@RequestBody TypeAndRegionDTO dto) {


        log.info("Request f or login {}", dto);
        log.warn("Request for login {}", dto);

        List<ArticleDTO> list = articleService.getLast5ArticleByTypesAndByRegion(dto);
        return ResponseEntity.ok().body(list);
    }

    //13
    @ApiOperation(value = "Get Article list by Region Key", notes = "Get Article list by Region Key (pagination)")
    @GetMapping("/getLastArticleListRegionKey/{regionKey}")
    public ResponseEntity<List<ArticleDTO>> getLastArticleListRegionKey(@PathVariable("regionKey")
                                                                                String regionKey) {
        log.info("Request f or login {}", regionKey);
        log.warn("Request for login {}", regionKey);

        List<ArticleDTO> list = articleService.getRegionKey(regionKey);
        return ResponseEntity.ok().body(list);
    }

    //14
    @ApiOperation(value = "Get Last 5 Article Category Key", notes = "Oxirgi 5 ta article Categoriya bo'yicha")
    @GetMapping("/getLast5CategoryList/{categoryKey}")
    public ResponseEntity<List<ArticleDTO>> getLast5CategoryList(@PathVariable("categoryKey")
                                                                         String articleTypeKey) {

        log.info("Request f or login {}", articleTypeKey);
        log.warn("Request for login {}", articleTypeKey);

        List<ArticleDTO> list = articleService.getLast5CategoryList(articleTypeKey);
        return ResponseEntity.ok().body(list);
    }

    //15
    @ApiOperation(value = " Get Article By Category Key (Pagination)", notes = "Oxirgi 5 ta article Categoriya bo'yicha(Pagination)")
    @GetMapping("/getLast5CategoryPagination/{categoryKey}")
    public ResponseEntity<List<ArticleDTO>> getLast5CategoryPagination(@PathVariable("categoryKey")
                                                                               String articleTypeKey) {


        log.info("Request f or login {}", articleTypeKey);
        log.warn("Request for login {}", articleTypeKey);

        List<ArticleDTO> list = articleService.getLast5CategoryPagination(articleTypeKey);
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Increase Article View Count by Article Id")
    @GetMapping("/increaseArticleViewCountbyArticleId/{article_id}")
    public ResponseEntity<?> increaseArticleViewCountbyArticleId(@PathVariable("article_id")
                                                                         String article_id) {

        log.info("Request f or login {}", article_id);
        log.warn("Request for login {}", article_id);
        articleService.increaseArticleViewCountbyArticleId(article_id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Filter", notes = "Method for filter")
    @PostMapping("/filter")
    public ResponseEntity<List<ArticleDTO>> filter(@RequestBody @Valid ArticleFilterDTO dto,
                                                   HttpServletRequest request) {

        log.info("Request f or login {}", dto);
        log.warn("Request for login {}", dto);

        List<ArticleDTO> response = filterService.filter(dto);
        return ResponseEntity.ok().body(response);
    }
}
