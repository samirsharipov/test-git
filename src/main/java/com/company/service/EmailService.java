package com.company.service;


import com.company.dto.EmailDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.exps.BadRequestException;
import com.company.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.url}")
    private String serverUrl;

    public void sendRegistrationEmail(String toAccount, String token) {
//        String message = "Your Activation lin: adsdasdasdasda";
//        sendSimpleEmail(toAccount, "Registration", message);
        String url = String.format("<a href='%sauth/email/verification/%s'>Verification Link</a>", serverUrl, token);

        StringBuilder builder = new StringBuilder();
        builder.append("<h1 style='align-text:center'>Salom Jiga Qalaysan</h1>");
        builder.append("<b>Mazgi</b>");
        builder.append("<p>");
        builder.append(url);
        builder.append("</p>");

        sendEmail(toAccount, "Registration", builder.toString());
    }

    private void sendEmail(String toAccount, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);

            EmailHistoryEntity entity = new EmailHistoryEntity();
            entity.setEmail(toAccount);
            entity.setCreatedDate(LocalDateTime.now());
            emailHistoryRepository.save(entity);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSimpleEmail(String toAccount, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(fromAccount);
        javaMailSender.send(msg);
    }


    public EmailHistoryEntity getEmail(String email) {

        Optional<EmailHistoryEntity> emailHistory = emailHistoryRepository.findByEmail(email);
        if (emailHistory.isEmpty()) {
            throw new BadRequestException("not found");
        }

        return emailHistory.get();

    }




    public PageImpl getList(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmailHistoryEntity> all = emailHistoryRepository.findAll(pageable);
        List<EmailHistoryEntity> list = all.getContent();
        List<EmailDTO> dtoList = new LinkedList<>();
        list.forEach(emailHistoryEntity -> {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId(emailHistoryEntity.getId());
            emailDTO.setEmail(emailHistoryEntity.getEmail());
            emailDTO.setDateTime(emailHistoryEntity.getCreatedDate());
            dtoList.add(emailDTO);
        });

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

}
