package com.example.med_classification.model.dto.response;

import com.example.med_classification.model.entity.Drug;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PillLookupResponseDto {
    private Integer pill_id;
    private String pill_name;
    private String color;
    private String main_ingredient;
    private String manufacturer;
    private String shape;
    private String ocr_front;
    private String ocr_back;
    private String effect;
    private String dosage;
    private String warning;
    private String image_url;

    public PillLookupResponseDto(Drug drug) {
        this.pill_id = drug.getIdx();
        this.pill_name = drug.getName();
        this.color = drug.getColor();
        this.main_ingredient = drug.getMaterial();
        this.manufacturer = drug.getCompany();
        this.shape = drug.getShape();
        this.ocr_front = drug.getPrintFront();
        this.ocr_back = drug.getPrintBack();
        this.effect = drug.getEffect();
        this.dosage = drug.getDosage();
        this.warning = drug.getWarning();
        this.image_url = drug.getImage();
    }
}
