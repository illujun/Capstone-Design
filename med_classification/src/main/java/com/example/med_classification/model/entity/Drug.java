package com.example.med_classification.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "drug")
@Getter
@Setter
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer drugId;

    @Column(name = "dl_name")
    private String name;

    @Column(name = "dl_company")
    private String company;

    @Column(name = "print_front")
    private String imprintFront;

    @Column(name = "print_back")
    private String imprintBack;

    @Column(name = "drug_shape")
    private String shape;

    @Column(name = "color_class1")
    private String color;

    @Column(name = "mark_code_front_img")
    private String imageUrl;
}

