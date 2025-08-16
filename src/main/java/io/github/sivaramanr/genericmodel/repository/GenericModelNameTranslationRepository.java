package io.github.sivaramanr.genericmodel.repository;

import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelNameTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GenericModelNameTranslationRepository extends JpaRepository<GenericModelNameTranslation, String> {

    Optional<GenericModelNameTranslation> findByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    void deleteByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    @Query(value = """
                   SELECT gmnt
                   FROM GenericModelNameTranslation gmnt
                   WHERE gmnt.tenantId = :tenantId
                     AND gmnt.genericModel = :genericModel
                   """,
            countQuery = """
                       SELECT COUNT(gmnt)
                       FROM GenericModelNameTranslation gmnt
                       WHERE gmnt.tenantId = :tenantId
                         AND gmnt.genericModel = :genericModel
                        """)
    Page<GenericModelNameTranslation> findAllByTenantIdAndGenericModel(@Param("tenantId") String tenantId,
                                                       @Param("genericModel") GenericModel genericModel,
                                                       Pageable pageable);

}
