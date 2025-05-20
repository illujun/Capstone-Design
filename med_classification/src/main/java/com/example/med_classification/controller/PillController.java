package com.example.med_classification.controller;

import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.dto.response.ResponseDto;
import com.example.med_classification.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pill")
@RequiredArgsConstructor
public class PillController {

    private final PillService pillService;

    @PostMapping("/lookup")
    public ResponseDto<PillLookupResponseDto> lookup(@RequestBody PillLookupRequestDto dto) {
        PillLookupResponseDto result = pillService.findPillByDetectionResult(dto);
        return new ResponseDto<>(true,result);
    }
}
