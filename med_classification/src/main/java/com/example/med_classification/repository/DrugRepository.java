package com.example.med_classification.repository;

import com.example.med_classification.model.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Integer> {

    List<Drug> findByNameContainingOrImprintFrontContaining(String name, String imprintFront);

    @Query("SELECT d FROM Drug d " +
            "WHERE (:shape IS NULL OR d.shape = :shape) " +
            "AND (:color IS NULL OR d.color = :color) " +
            "AND (:company IS NULL OR d.company LIKE %:company%)")
    List<Drug> findByShapeAndColorAndCompany(
            @Param("shape") String shape,
            @Param("color") String color,
            @Param("company") String company
    );

}
