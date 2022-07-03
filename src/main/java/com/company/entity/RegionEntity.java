package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "region")
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false, name = "name_Uz")
    private  String nameUz;

    @Column(nullable = false, name = "name_Ru")
    private  String nameRu;

    @Column(nullable = false, name = "name_En")
    private  String nameEn;

    @Column(nullable = false)
    private Boolean visible=Boolean.TRUE;


    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();


}
