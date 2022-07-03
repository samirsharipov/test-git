package com.company.controller;


import com.company.dto.SavedArticleDTO;
import com.company.enums.ProfileRole;
import com.company.service.SavedArticleService;
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
@Api(tags = "Saved Article")
@RequestMapping("/saved_article")
@RestController
public class SavedArticleController {

    @Autowired
    private SavedArticleService savedArticleService;


    @ApiOperation(value = "Saved Article", notes = "Method create Saved Article")
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody SavedArticleDTO dto, HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.USER);
        SavedArticleDTO responseDTO = savedArticleService.create(dto, profileId);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ApiOperation(value = "Saved Article delete", notes = "Method delete Saved Article")
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.USER);
        String delete = savedArticleService.delete(id, profileId);
        return ResponseEntity.ok().body(delete);
    }


    @ApiOperation(value = "Saved Article get list", notes = "Method get list Saved Article only admin")
    @GetMapping("/adm/list")
    public ResponseEntity<?> getlist(HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.USER);
        List<SavedArticleDTO> list = savedArticleService.getList(profileId);
        return ResponseEntity.ok().body(list);
    }


}
