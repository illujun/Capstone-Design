
package com.example.med_classification.repository;

import com.example.med_classification.model.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Integer> {
    List<Drug> findByNameContainingOrImprintFrontContaining(String name, String imprintFront);
}

