package com.example.med_classification.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PillLookupRequestDto {
    private String detectedText;
    private String shape;
    private String color;

    private String printFront;
    private String printBack;
}
