package com.example.med_classification.repository;

import com.example.med_classification.model.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Integer> {
    @Query("SELECT d FROM Drug d WHERE " +
            "(:color IS NULL OR d.color = :color) AND " +
            "(:engraved1 IS NULL OR d.printFront LIKE %:engraved1%) AND " +
            "(:engraved2 IS NULL OR d.printBack LIKE %:engraved2%) AND " +
            "(:shape IS NULL OR d.shape = :shape) AND " +
            "(:form IS NULL OR d.material LIKE %:form%)")
    List<Drug> findByMultipleAttributes(
            @Param("color") String color,
            @Param("engraved1") String engraved1,
            @Param("engraved2") String engraved2,
            @Param("shape") String shape,
            @Param("form") String form
    );

    @Query("SELECT d FROM Drug d WHERE " +
            "(:printFront IS NULL OR d.printFront LIKE %:printFront%) AND " +
            "(:printBack IS NULL OR d.printBack LIKE %:printBack%) AND " +
            "(:color IS NULL OR d.color = :color) AND " +
            "(:shape IS NULL OR d.shape = :shape)")
    List<Drug> findByDetection(
            @Param("printFront") String printFront,
            @Param("printBack") String printBack,
            @Param("color") String color,
            @Param("shape") String shape
    );


//    List<Drug> findByNameContainingOrImprintFrontContaining(String name, String imprintFront);
//
//    @Query("SELECT d FROM Drug d " +
//            "WHERE (:shape IS NULL OR d.shape = :shape) " +
//            "AND (:color IS NULL OR d.color = :color) " +
//            "AND (:company IS NULL OR d.company LIKE %:company%)")
//    List<Drug> findByShapeAndColorAndCompany(
//            @Param("shape") String shape,
//            @Param("color") String color,
//            @Param("company") String company
//    );
}
