package com.example.med_classification.model.dto.response;

import com.example.med_classification.model.entity.Drug;
import lombok.Getter;

@Getter
public class PillLookupResponseDto {

    private Integer id;
    private String name;
    private String imprintFront;
    private String imprintBack;
    private String shape;
    private String color;
    private String imageUrl;

    public PillLookupResponseDto(Drug drug) {
        this.id = drug.getDrugId();
        this.name = drug.getName();
        this.imprintFront = drug.getImprintFront();
        this.imprintBack = drug.getImprintBack();
        this.shape = drug.getShape();
        this.color = drug.getColor();
        this.imageUrl = drug.getImageUrl();
    }
}

