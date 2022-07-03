package com.company.controller;


import com.company.dto.TypesDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.TypesService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(tags = "Type")
@RequestMapping("/types")
@RestController
public class TypesController {

    @Autowired
    private TypesService typesService;

// PUBLIC

    @ApiOperation(value = "Type List", notes = "Method for get list type all user")
    @GetMapping("/public")
    public ResponseEntity<List<TypesDTO>> getArticleList1(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang) {
        List<TypesDTO> list = typesService.getList(lang);
        return ResponseEntity.ok().body(list);
    }


// SECURED

    @ApiOperation(value = "Type", notes = "Method for create type only admin")
    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody @Valid TypesDTO typeDto,
                                    HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        TypesDTO responseTypeDTO = typesService.create(typeDto);
        return ResponseEntity.ok().body(responseTypeDTO);
    }


    @ApiOperation(value = "Type list", notes = "Method for get list type only admin")
    @GetMapping("/adm")
    public ResponseEntity<List<TypesDTO>> getlist(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<TypesDTO> list = typesService.getListOnlyForAdmin(lang);
        return ResponseEntity.ok().body(list);
    }


    @ApiOperation(value = "Type update", notes = "Method for update type only admin")
    @PutMapping("/adm/{id}")
    private ResponseEntity<?> update(@PathVariable("id") Integer id,
                                     @RequestBody @Valid TypesDTO dto,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        TypesDTO typeDTO = typesService.update(id, dto);
        return ResponseEntity.ok().body(typeDTO);
    }

    @ApiOperation(value = "Type delete", notes = "Method for delete type only admin")
    @DeleteMapping("/adm/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        typesService.delete(id);
        return ResponseEntity.ok().body("Sucsessfully deleted");
    }

    @ApiOperation(value = "Type list Pagionation", notes = "Method for get list type only admin")
    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                                  HttpServletRequest request,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = typesService.pagination(page, size, lang);
        return ResponseEntity.ok().body(response);
    }

}
