package com.company.controller;


import com.company.dto.RegionDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
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
@Api(tags = "Region")
@RequestMapping("/region")
@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    // PUBLIC

    @ApiOperation(value = "Region get list", notes = "Method get list region")
    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getlistRegion(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang) {
        List<RegionDTO> list = regionService.getList(lang);
        return ResponseEntity.ok().body(list);
    }

    // SECURE

    @ApiOperation(value = "Region", notes = "Method for create region")
    @PostMapping("/adm")
    public ResponseEntity<String> create(@RequestBody  RegionDTO regionDto,
                                    HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        regionService.create(regionDto);
        return ResponseEntity.ok().body("SuccsessFully created");
    }


    @ApiOperation(value = "Region get list", notes = "Method get list region only admin")
    @GetMapping("/adm")
    public ResponseEntity<List<RegionDTO>> getlist(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang,
                                                   HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<RegionDTO> list = regionService.getListOnlyForAdmin(lang);
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Region update", notes = "Method for update region only admin")
    @PutMapping("/adm/{id}")
    private ResponseEntity<RegionDTO> update(@PathVariable("id") Integer id,
                                     @RequestBody  RegionDTO dto,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        RegionDTO regionDTO = regionService.update(id, dto);
        return ResponseEntity.ok().body(regionDTO);
    }

    @ApiOperation(value = "Region delete", notes = "Method for delete region only admin")
    @DeleteMapping("/adm/{id}")
    private ResponseEntity<String> delete(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        regionService.delete(id);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }


    @ApiOperation(value = "Region get list pagination", notes = "Method get list region only admin")
    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                                  HttpServletRequest request,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "ru") LangEnum lang) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = regionService.pagination(page, size, lang);
        return ResponseEntity.ok().body(response);
    }

}
