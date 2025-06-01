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
import java.util.stream.Collectors;

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
public List<Drug> findPillByDetection(PillLookupRequestDto dto) {
    List<Drug> drugs = drugRepository.findByDetection(
            dto.getPrintFront(),
            dto.getPrintBack(),
            dto.getColor(),
            dto.getShape()
    );

    if (drugs.isEmpty()) {
        throw new RuntimeException("일치하는 약을 찾을 수 없습니다.");
    }

    return drugs;
}

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

    public List<PillLookupResponseDto> findByInfo(PillInfoRequestDto dto) {
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

        // Drug → PillLookupResponseDto 변환
        return results.stream()
                .map(PillLookupResponseDto::new) // 정적 팩토리 메서드 필요
                .collect(Collectors.toList());
    }




}
