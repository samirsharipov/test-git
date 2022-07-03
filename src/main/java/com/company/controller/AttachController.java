package com.company.controller;


import com.company.dto.AttachDTO;
import com.company.enums.ProfileRole;
import com.company.service.AttachService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "Attach")
@RequestMapping("/attache")
@RestController
public class AttachController {

    @Autowired
    private AttachService attachService;

    @ApiOperation(value = "Upload", notes = "Method for save photo")
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO attachDTO = attachService.saveToSystem(file);
        return ResponseEntity.ok().body(attachDTO);
    }


    @ApiOperation(value = "Open photo general", notes = "Method for open photo")
    @GetMapping(value = "/open_general/{id}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("id") String id) {
        return attachService.open_general(id);
    }


    @ApiOperation(value = "Download photo", notes = "Method for download photo")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id) {
        Resource file = attachService.download(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @ApiOperation(value = "Delete photo", notes = "Method for delete photo")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> upload(@PathVariable("id") String id) {
        String response = attachService.delete(id);
        return ResponseEntity.ok().body(response);
    }


    @ApiOperation(value = "Open photo", notes = "Method for download photo")
    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                log.warn("Request for attach {}");

                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }

    @ApiOperation(value = "List pagination", notes = "Method for get photo List")
    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl> pagination(@RequestParam(value = "page" , defaultValue = "1") int page,
                                               @RequestParam(value = "size" ,defaultValue = "5" ) int size,
                                               HttpServletRequest request){
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        PageImpl response = attachService.paginationAttach(page , size);
        return ResponseEntity.ok().body(response);
    }

}
