package com.example.med_classification.controller;

import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.dto.response.ResponseDto;
import com.example.med_classification.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getpill")
    public ResponseDto<PillLookupResponseDto> getPill(@RequestParam("id") Integer id) {
        PillLookupResponseDto result = pillService.findById(id);
        return new ResponseDto<>(true, result);
    }

    @GetMapping("/getpills")
    public ResponseDto<List<PillLookupResponseDto>> getPills(@RequestParam("ids") List<Integer> ids) {
        List<PillLookupResponseDto> result = pillService.findByIds(ids);
        return new ResponseDto<>(true, result);
    }

    @GetMapping("/getpillbyinfo")
    public ResponseDto<List<PillLookupResponseDto>> getPillByInfo(
            @RequestParam(required = false) String shape,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String company) {

        List<PillLookupResponseDto> result = pillService.findByInfo(shape, color, company);
        return new ResponseDto<>(true, result);
    }




}
