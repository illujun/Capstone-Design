package com.example.med_classification.controller;

import com.example.med_classification.model.dto.request.PillInfoRequestDto;
import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.dto.response.ResponseDto;
import com.example.med_classification.service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pill")
@RequiredArgsConstructor
public class PillController {

    private final PillService pillService;

//    @PostMapping("/lookup")
//    public ResponseDto<PillLookupResponseDto> lookup(@RequestBody PillLookupRequestDto dto) {
//        PillLookupResponseDto result = pillService.findPillByDetectionResult(dto);
//        return new ResponseDto<>(true,result);
//    }

    @GetMapping("/getpill")
    public ResponseEntity<Object> getPill(@RequestParam("id") String id) {
        try {
            PillLookupResponseDto pillDto = pillService.findById(id);
            return ResponseEntity.ok(
                    Map.of(
                            "status", "ok",
                            "pill", pillDto
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "status", "error",
                            "message", "알약 정보를 찾을 수 없습니다."
                    )
            );
        }
    }


//    @GetMapping("/getpills")
//    public ResponseDto<List<PillLookupResponseDto>> getPills(@RequestParam("ids") List<Integer> ids) {
//        List<PillLookupResponseDto> result = pillService.findByIds(ids);
//        return new ResponseDto<>(true, result);
//    }

    @PostMapping("/getpillbyinfo")
    public ResponseEntity<Object> getPillByInfo(@RequestBody PillInfoRequestDto dto) {
        try {
            PillLookupResponseDto pillDto = pillService.findByInfo(dto);
            return ResponseEntity.ok(
                    Map.of("status", "ok", "pill", pillDto)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("status", "error", "message", "알약 정보를 찾을 수 없습니다.")
            );
        }
    }





}
