package com.company.entity;

import com.company.enums.CommentLikeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_like")
public class CommentLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "article_like")
    @Enumerated(EnumType.STRING)
    private CommentLikeStatus likeStatus;

    @JoinColumn(name = "commet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity comment;


    @JoinColumn(name = "profile_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile;



    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();


}
