package com.company.controller;



import com.company.dto.CommetLikeDTO;
import com.company.service.CommentLikeService;
import com.company.util.HttpHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RequestMapping("/comment_like")
@RestController
public class CommentLikeController {


    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> like(@RequestBody CommetLikeDTO dto,
                                     HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        commentLikeService.commnetLike(dto.getCommentId(), profileId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(@RequestBody CommetLikeDTO dto,
                                        HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        commentLikeService.commentDisLike(dto.getCommentId(), profileId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody CommetLikeDTO dto,
                                       HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        commentLikeService.removeLike(dto.getCommentId(), profileId);
        return ResponseEntity.ok().build();
    }

}
