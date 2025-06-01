package com.example.med_classification.controller;

import com.example.med_classification.model.dto.request.PillDetectionRequestDto;
import com.example.med_classification.model.dto.request.PillInfoRequestDto;
import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.entity.Drug;
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


    @GetMapping("/getpill/{id}")
    public ResponseEntity<Object> getPill(@PathVariable Integer id) {
        try {
            PillLookupResponseDto pillDto = pillService.findById(id);
            return ResponseEntity.ok(pillDto); // ✅ 단일 객체 그대로 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "알약 정보를 찾을 수 없습니다.")
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
            List<PillLookupResponseDto> pillDtoList = pillService.findByInfo(dto);
            return ResponseEntity.ok(
                    Map.of("status", "2", "pill", pillDtoList)  // ✅ status: "2"
            );
        } catch (RuntimeException e) {
            return ResponseEntity.ok(  // ❗ error 응답도 status = "2"
                    Map.of("status", "0", "pill", List.of())   // 빈 리스트 반환
            );
        }
    }




    @PostMapping("/lookup")
    public ResponseEntity<Object> lookup(@RequestBody PillDetectionRequestDto request) {
        PillLookupResponseDto result = pillService.findBestMatch(request);
        return ResponseEntity.ok(result);
    }






}
