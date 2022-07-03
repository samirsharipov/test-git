package com.company.controller;


import com.company.dto.*;
import com.company.enums.ProfileRole;
import com.company.service.CommentLikeService;
import com.company.service.CommentService;
import com.company.service.CommetFilterService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "Comment")
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private CommetFilterService commetFilterService;

    @ApiOperation(value = "Comment write", notes = "Method for comment write any")
    @PostMapping("/user")
    public ResponseEntity<CommentDTO> create(@RequestBody  CommentDTO dto,
                                             HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        CommentDTO response = commentService.create(dto, profileId);
        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(value = "Comment get List", notes = "Method for comment get list only admin")
    @GetMapping("/adm")
    public ResponseEntity<List<CommentDTO>> getlist( HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<CommentDTO> list = commentService.getListOnlyForAdmin();
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Comment update", notes = "Method for comment updete only own")
    @PutMapping("/user/{id}")
    private ResponseEntity<String> update(@PathVariable("id") Integer id,
                                     @RequestBody  CommentDTO dto,
                                     HttpServletRequest request) {
        Integer pId = HttpHeaderUtil.getId(request);
        commentService.update(id, pId, dto);
        return ResponseEntity.ok().body("Succsessfully updated");
    }

    @ApiOperation(value = "Comment delete", notes = "Method for comment delete only own")
    @DeleteMapping("/adm/{id}")
    private ResponseEntity<String> delete(@PathVariable("id") Integer comId,
                                     HttpServletRequest request) {
        Integer pId = HttpHeaderUtil.getId(request);
        commentService.delete(pId, comId);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }

    @ApiOperation(value = "Comment list article Id", notes = "Method for comment  list article Id")
    @GetMapping("/list/{id}")
    public ResponseEntity<List<CommentProfileDTO>> getArticleCommentListByArticleId(@PathVariable("id") String article_id) {
        List<CommentProfileDTO> list = commentService.getArticleCommentListByArticleId(article_id);
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Comment list article Id Pagination", notes = "Method for comment  list article Id Pagination")
    @GetMapping("/adm/list")
    public ResponseEntity<List<CommentProfileArticleDTO>> commentListPagination(HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<CommentProfileArticleDTO> list = commentService.commentListPagination();
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Comment list article filter", notes = "Method for comment  list article filter")
    @PostMapping("/filter")
    public ResponseEntity<List<CommentResponseDTO>> filter(@RequestBody CommentFilterDTO dto,
                                                           HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        List<CommentResponseDTO>  response = commetFilterService.filter(dto);
        return ResponseEntity.ok().body(response);
    }

}
