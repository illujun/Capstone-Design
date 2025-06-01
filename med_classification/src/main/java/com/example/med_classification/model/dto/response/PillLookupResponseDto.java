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
    private Integer idx;
    private String name;
    private String color;
    private String material;
    private String company;
    private String shape;
    private String print_front;
    private String print_back;
    private String effect;
    private String dosage;
    private String warning;
    private String image;

    public PillLookupResponseDto(Drug drug) {
        this.idx = drug.getIdx();
        this.name = drug.getName();
        this.color = drug.getColor();
        this.material = drug.getMaterial();
        this.company = drug.getCompany();
        this.shape = drug.getShape();
        this.print_front = drug.getPrintFront();
        this.print_back = drug.getPrintBack();
        this.effect = drug.getEffect();
        this.dosage = drug.getDosage();
        this.warning = drug.getWarning();
        this.image = drug.getImage();
    }

    public static PillLookupResponseDto fromEntity(Drug drug) {
        return new PillLookupResponseDto(drug);
    }
}
