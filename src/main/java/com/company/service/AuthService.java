package com.company.service;


import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.VerificationDTO;
import com.company.entity.AttachEntity;
import com.company.entity.EmailHistoryEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SmsEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.ProfileRepository;
import com.company.repository.SmsRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AttachService attachService;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EmailService emailService;

    @Value("${attach.folder}")
    private String attachFolder;


    @Value("${server.url}")
    private String serverUrl;

    public ProfileDTO login(AuthDTO authDTO) {

        Optional<ProfileEntity> optional = profileRepository.findByEmail(authDTO.getEmail());

        if (optional.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        ProfileEntity entity = optional.get();

        if (!entity.getEmail().equals(authDTO.getEmail())) {
            throw new ItemNotFoundEseption("User not found");
        }

        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new ItemNotFoundEseption("No ruxsat");
        }

        ProfileDTO dto = new ProfileDTO();

        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setJwt(JwtUtil.encode1(entity.getId(), entity.getRole()));

        return dto;
    }

    public String registiration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());

        if (dto.getPhotoId() != null) {
            AttachEntity attachEntity = attachService.get(dto.getPhotoId());
            entity.setPhoto(attachEntity);
        } else if (dto.getPhotoId() == null) {
            entity.setPhoto(null);
        }


        entity.setStatus(ProfileStatus.NO_ACTIVE);
        entity.setRole(ProfileRole.USER);
        profileRepository.save(entity);

        String token = JwtUtil.encode(entity.getId());


        emailService.sendRegistrationEmail(entity.getEmail(), token);


        return "Message was send";

/*        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setJwt(JwtUtil.encode1(entity.getId(), entity.getRole()));

        if (entity.getPhoto()!=null){
            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setUrl(serverUrl + "" + "attache/open/" + entity.getPhoto().getId());

            responseDTO.setPhotoId(attachDTO.getId());
        }else if (entity.getPhoto()==null){
            responseDTO.setPhotoId(null);
        }*/



        /* return responseDTO;*/


    }

    public String verification(VerificationDTO dto) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(dto.getPhone());
        if (optional.isEmpty()) {
            return "Phone Not Found";
        }

        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (!sms.getCode().equals(dto.getCode())) {
            return "Code Invalid";
        }
        if (validDate.isBefore(LocalDateTime.now())) {
            return "Time is out";
        }

        profileRepository.updateStatusByPhone(dto.getPhone(), ProfileStatus.ACTIVE);
        return "Verification Done";
    }

    public String verificationEmail(Integer pId) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()){
            throw new BadRequestException("Profile not found!");
        }

        ProfileEntity profileEntity = profile.get();
        EmailHistoryEntity email = emailService.getEmail(profileEntity.getEmail());

        if (!email.getCreatedDate().plusMinutes(1).isAfter(LocalDateTime.now())){
            throw new BadRequestException("time out!");
        }

        profileRepository.updateStatusById(ProfileStatus.ACTIVE, pId);

        return "sucsess";

    }
}
