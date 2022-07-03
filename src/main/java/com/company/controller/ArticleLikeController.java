package com.company.controller;



import com.company.dto.article.ArticleLikeDTO;
import com.company.service.ArticleLikeService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Api(tags = "Article Like DisLike")
@RequestMapping("/article_like")
@RestController
public class ArticleLikeController {

    @Autowired
    private ArticleLikeService articleLikeService;


    @ApiOperation(value = "Like", notes = "Method for create like")
    @PostMapping("/like")
    public ResponseEntity<Void> like(@RequestBody ArticleLikeDTO dto,
                                     HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.articleLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Dislike", notes = "Method for create dislike")
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(@RequestBody ArticleLikeDTO dto,
                                        HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.articleDisLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Like or Dislike delete", notes = "Method for create like and dislike")
    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody ArticleLikeDTO dto,
                                       HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        articleLikeService.removeLike(dto.getArticleId(), profileId);
        return ResponseEntity.ok().build();
    }




}
