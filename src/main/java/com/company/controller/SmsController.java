package com.company.controller;

import com.company.enums.ProfileRole;
import com.company.service.SmsService;
import com.company.util.HttpHeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "Sms")
public class SmsController {


    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "Sms", notes = "email ga qilingan bu test uchun")
    @GetMapping("/adm/pagination")
    public ResponseEntity<PageImpl> pagination(@RequestParam(value = "page" , defaultValue = "1") int page,
                                               @RequestParam(value = "size" ,defaultValue = "5" ) int size,
                                               HttpServletRequest request){
        HttpHeaderUtil.getId(request , ProfileRole.ADMIN);
        PageImpl response = smsService.paginationSms(page , size);
        return ResponseEntity.ok().body(response);
    }


}
