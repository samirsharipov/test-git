package com.company.controller;

import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.enums.ProfileRole;
import com.company.service.AuthService;
import com.company.service.EmailService;
import com.company.service.ProfileService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "Authorization and Registeration")
@RestController
@RequestMapping("/auth")
public class AuthController {



    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "Registeration", notes = "Method for registiration")
    @PostMapping("/registration")
    public ResponseEntity<?> createByAdmin(@RequestBody RegistrationDTO dto) {

        log.info("Request for login {}", dto);

        String response = authService.registiration(dto);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "Login", notes = "Method for login")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO dto) {

        log.info("Request for login {}", dto); ///////////log

        ProfileDTO profileDto = authService.login(dto);
        return ResponseEntity.ok(profileDto);
    }

    @ApiOperation(value = "email verification", notes = "Method for verification")
    @GetMapping("/email/verification/{token}")
    public ResponseEntity<?> login(@PathVariable("token") String token) {

        Integer PID = JwtUtil.decode(token);
        String s= authService.verificationEmail(PID);
        return ResponseEntity.ok(s);
    }

    @ApiOperation(value = "email list", notes = "Method for email list admin")
    @GetMapping("/adm/emailList")
    public ResponseEntity<PageImpl> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "3") int size,
                                                  HttpServletRequest request) {
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        PageImpl response = emailService.getList(page, size);
        return ResponseEntity.ok().body(response);
    }

}
