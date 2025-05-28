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
    private String pill_id;
    private String pill_name;
    private String description;
    private String manufacturer;
    private String main_ingredient;
    private String image_url;
    private String ocr_front;
    private String ocr_back;

    public PillLookupResponseDto(Drug drug) {
        this.pill_id = drug.getIdx();
        this.pill_name = drug.getName();
        this.description = drug.getPurpose(); // 추후 DB 확장 또는 추론 필요
        this.manufacturer = drug.getCompany();
        this.main_ingredient = drug.getMaterial();
        this.image_url = drug.getImage();
        this.ocr_front = drug.getPrintFront();
        this.ocr_back = drug.getPrintBack();
    }

    public static PillLookupResponseDto fromEntity(Drug drug) {
        PillLookupResponseDto dto = new PillLookupResponseDto();
        dto.pill_id = drug.getIdx();
        dto.pill_name = drug.getName();
        dto.description = drug.getPurpose();
        dto.manufacturer = drug.getCompany();
        dto.main_ingredient = drug.getMaterial();
        dto.image_url = drug.getImage();
        dto.ocr_front = drug.getPrintFront();
        dto.ocr_back = drug.getPrintBack();
        return dto;
    }
}
