// 📁 service/PillService.java
package com.example.med_classification.service;

import com.example.med_classification.model.dto.request.PillDetectionRequestDto;
import com.example.med_classification.model.dto.request.PillInfoRequestDto;
import com.example.med_classification.model.dto.request.PillLookupRequestDto;
import com.example.med_classification.model.dto.response.PillLookupResponseDto;
import com.example.med_classification.model.entity.Drug;
import com.example.med_classification.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

@Service
@RequiredArgsConstructor
public class PillService {

    public List<Drug> findByPrintsWithFallback(PillLookupRequestDto dto) {
        String front = dto.getPrintFront();
        String back = dto.getPrintBack();

        List<Drug> allDrugs = drugRepository.findAll();

        // 1. printFront + printBack 완전 일치
        for (Drug drug : allDrugs) {
            if (front != null && back != null &&
                    drug.getPrintFront().equalsIgnoreCase(front) &&
                    drug.getPrintBack().equalsIgnoreCase(back)) {
                return List.of(drug);
            }
        }

        // 2. printFront 또는 printBack 하나만 일치
        for (Drug drug : allDrugs) {
            if ((front != null && drug.getPrintFront().equalsIgnoreCase(front)) ||
                    (back != null && drug.getPrintBack().equalsIgnoreCase(back))) {
                return List.of(drug);
            }
        }

        // 3. 모두 불일치 → 유사도 측정
        LevenshteinDistance distance = new LevenshteinDistance();

        return allDrugs.stream()
                .sorted(Comparator.comparingInt(d ->
                        distance.apply(d.getPrintFront(), front != null ? front : "") +
                                distance.apply(d.getPrintBack(), back != null ? back : "")
                ))
                .limit(4)
                .collect(Collectors.toList());
    }

    private final DrugRepository drugRepository;

    public PillLookupResponseDto findBestMatch(PillDetectionRequestDto request) {
        String label = request.getLabel();
        String clazz = request.getClazz();

        // 1. class 분리 → color, shape
        if (clazz == null || !clazz.contains("_")) {
            throw new RuntimeException("형식이 잘못된 class입니다.");
        }
        String[] parts = clazz.split("_");
        String color = parts[0];
        String shape = parts[1];

        // 2. color, shape가 일치하는 약 리스트
        List<Drug> filtered = drugRepository.findByColorAndShape(color, shape);
        if (filtered.isEmpty()) {
            throw new RuntimeException("일치하는 색상/형태의 알약이 없습니다.");
        }

        // 3. print_front & print_back 모두 일치
        for (Drug drug : filtered) {
            if (Objects.equals(drug.getPrintFront(), request.getFront()) &&
                    Objects.equals(drug.getPrintBack(), request.getBack())) {
                return PillLookupResponseDto.from(drug, label);
            }
        }

        // 4. 둘 중 하나만 일치
        for (Drug drug : filtered) {
            if (Objects.equals(drug.getPrintFront(), request.getFront()) ||
                    Objects.equals(drug.getPrintBack(), request.getBack())) {
                return PillLookupResponseDto.from(drug, label);
            }
        }

        // 5. 유사도 비교 (Levenshtein)
        LevenshteinDistance distance = new LevenshteinDistance();
        Drug bestMatch = null;
        int bestScore = Integer.MAX_VALUE;

        for (Drug drug : filtered) {
            int frontScore = request.getFront() != null ? distance.apply(request.getFront(), Optional.ofNullable(drug.getPrintFront()).orElse("")) : 100;
            int backScore = request.getBack() != null ? distance.apply(request.getBack(), Optional.ofNullable(drug.getPrintBack()).orElse("")) : 100;
            int total = frontScore + backScore;

            if (total < bestScore) {
                bestScore = total;
                bestMatch = drug;
            }
        }

        if (bestMatch == null) {
            throw new RuntimeException("유사한 알약을 찾을 수 없습니다.");
        }

        return PillLookupResponseDto.from(bestMatch, label);
    }



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

    public PillLookupResponseDto findById(Integer id) {
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
                dto.getPrint_front(),
                dto.getPrint_back(),
                dto.getShape(),
                dto.getCompany()
        );

        if (results.isEmpty()) {
            throw new RuntimeException("알약 정보를 찾을 수 없습니다.");
        }

        return results.stream()
                .map(PillLookupResponseDto::new)
                .collect(Collectors.toList());
    }





}
