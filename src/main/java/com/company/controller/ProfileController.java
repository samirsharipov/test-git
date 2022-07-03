package com.company.controller;


import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "Profile")
@RequestMapping("/profile")
@RestController
public class ProfileController {


    @Autowired
    private ProfileService profileService;

    @ApiOperation(value = "Profile", notes = "Method create profile only admin")
    @PostMapping("")
    public ResponseEntity<ProfileDTO> create(@RequestBody  ProfileDTO profileDto,
                                             HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ProfileDTO dto = profileService.create(profileDto);
        return ResponseEntity.ok().body(dto);
    }


    @ApiOperation(value = "Profile get List", notes = "Method get List profile only admin")
    @GetMapping("")
    public ResponseEntity<List<ProfileDTO>> getProfileList(HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<ProfileDTO> list = profileService.getList();
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Profile updete", notes = "Method update profile only owner")
    @PutMapping("/detail")
    public ResponseEntity<String> update(@RequestBody ProfileDTO dto,
                                    HttpServletRequest request) {
        Integer profileId = HttpHeaderUtil.getId(request);
        profileService.update(profileId, dto);
        return ResponseEntity.ok().body("Sucsessfully updated");
    }


    @ApiOperation(value = "Profile updete", notes = "Method update profile only admin")
    @PutMapping("/{id}")
    private ResponseEntity<?> update(@PathVariable("id") Integer id,
                                     @RequestBody  ProfileDTO dto,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        profileService.update(id, dto);
        return ResponseEntity.ok().body("Succsessfully updated");
    }

    @ApiOperation(value = "Profile delete", notes = "Method delete profile only admin")
    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        profileService.delete(id);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }

    @ApiOperation(value = "Profile get list pagination", notes = "Method get list profile only admin")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = profileService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }

/*
    @PutMapping("/updatePhoto")
    public ResponseEntity<?> updatePhoto(@RequestBody UpdateAttacheDTO dto,
                                     HttpServletRequest request) {
        Integer pId = HttpHeaderUtil.getId(request, ProfileRole.USER);
        profileService.updatePhoto(dto, pId);
        return ResponseEntity.ok().body("Succsessfully updated");
    }*/

}
