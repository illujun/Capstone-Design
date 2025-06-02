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

        String front = request.getFront();
        String back = request.getBack();

        // 2. color + shape 일치하는 약 리스트
        List<Drug> filtered = drugRepository.findByColorAndShape(color, shape);

        List<Drug> searchTarget;

        if (!filtered.isEmpty()) {
            // 필터링된 약에서 먼저 찾기
            searchTarget = filtered;
        } else {
            // color+shape 일치 약 없으면 전체에서 찾기
            searchTarget = drugRepository.findAll();
        }

        // 3. front + back 모두 일치
        for (Drug drug : searchTarget) {
            boolean frontMatch = front != null && front.equals(drug.getPrintFront());
            boolean backMatch = back != null && back.equals(drug.getPrintBack());

            if (frontMatch || backMatch) {
                return PillLookupResponseDto.from(drug, label);
            }
        }


        // 4. 둘 중 하나 일치
        for (Drug drug : searchTarget) {
            if (Objects.equals(drug.getPrintFront(), front) ||
                    Objects.equals(drug.getPrintBack(), back)) {
                return PillLookupResponseDto.from(drug, label);
            }
        }

        // 5. 유사도 기반
        LevenshteinDistance distance = new LevenshteinDistance();
        Drug bestMatch = null;
        int bestScore = Integer.MAX_VALUE;

        for (Drug drug : searchTarget) {
            int frontScore = front != null ? distance.apply(front, Optional.ofNullable(drug.getPrintFront()).orElse("")) : 100;
            int backScore = back != null ? distance.apply(back, Optional.ofNullable(drug.getPrintBack()).orElse("")) : 100;
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
