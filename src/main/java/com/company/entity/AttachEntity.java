package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attach")
public class AttachEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private  String orginalName;

    @Column(nullable = false)
    private String extensional;

    @Column(nullable = false)
    private  Long size;

    @Column(nullable = false)
    private  String path;

    public AttachEntity(String id) {
        this.id = id;
    }
}
