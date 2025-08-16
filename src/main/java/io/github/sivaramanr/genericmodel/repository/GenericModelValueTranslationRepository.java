package io.github.sivaramanr.genericmodel.repository;

import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelValueTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GenericModelValueTranslationRepository extends JpaRepository<GenericModelValueTranslation, String> {

    Optional<GenericModelValueTranslation> findByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    void deleteByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    @Query(value = """
                   SELECT gmvt
                   FROM GenericModelValueTranslation gmvt
                   WHERE gmvt.tenantId = :tenantId
                     AND gmvt.genericModel = :genericModel
                   """,
            countQuery = """
                       SELECT COUNT(gmvt)
                       FROM GenericModelValueTranslation gmvt
                       WHERE gmvt.tenantId = :tenantId
                         AND gmvt.genericModel = :genericModel
                        """)
    Page<GenericModelValueTranslation> findAllByTenantIdAndGenericModel(@Param("tenantId") String tenantId,
                                                                       @Param("genericModel") GenericModel genericType,
                                                                       Pageable pageable);

}
