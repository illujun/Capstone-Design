// 📁 service/PillService.java
package com.example.med_classification.service;

import com.example.med_classification.model.dto.request.PillInfoRequestDto;
import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.entity.Drug;
import com.example.med_classification.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PillService {

    private final DrugRepository drugRepository;

//    public PillLookupResponseDto findPillByDetectionResult(PillLookupRequestDto dto) {
//        List<Drug> matches = drugRepository.findByNameContainingOrImprintFrontContaining(
//                dto.getDetectedText(), dto.getDetectedText()
//        );
//
//
//        if (matches.isEmpty()) {
//            throw new RuntimeException("약을 찾을 수 없습니다.");
//        }
//
//        return new PillLookupResponseDto(matches.get(0));
//    }

    public PillLookupResponseDto findById(String id) {
        Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("약을 찾을 수 없습니다."));
        return new PillLookupResponseDto(drug);
    }


//    public List<PillLookupResponseDto> findByIds(List<String> ids) {
//        List<Drug> drugs = drugRepository.findAllById(ids);
//        return drugs.stream()
//                .map(PillLookupResponseDto::new)
//                .toList();
//    }

    public PillLookupResponseDto findByInfo(PillInfoRequestDto dto) {
        List<Drug> results = drugRepository.findByMultipleAttributes(
                dto.getColor(),
                dto.getEngraved1(),
                dto.getEngraved2(),
                dto.getShape(),
                dto.getDivided(),
                dto.getForm()
        );

        if (results.isEmpty()) {
            throw new RuntimeException("알약 정보를 찾을 수 없습니다.");
        }

        return new PillLookupResponseDto(results.get(0)); // 가장 첫 결과만 반환
    }




}
