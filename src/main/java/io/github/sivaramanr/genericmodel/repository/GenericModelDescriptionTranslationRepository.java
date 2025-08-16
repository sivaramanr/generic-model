package io.github.sivaramanr.genericmodel.repository;

import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelDescriptionTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GenericModelDescriptionTranslationRepository extends
        JpaRepository<GenericModelDescriptionTranslation, String> {

    Optional<GenericModelDescriptionTranslation> findByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    void deleteByTenantIdAndGenericModelAndLocale(String tenantId, GenericModel genericModel, String locale);

    @Query(value = """
                   SELECT gmdt
                   FROM GenericModelDescriptionTranslation gmdt
                   WHERE gmdt.tenantId = :tenantId
                     AND gmdt.genericModel = :genericModel
                   """,
            countQuery = """
                       SELECT COUNT(gmdt)
                       FROM GenericModelDescriptionTranslation gmdt
                       WHERE gmdt.tenantId = :tenantId
                         AND gmdt.genericModel = :genericModel
                        """)
    Page<GenericModelDescriptionTranslation> findAllByTenantIdAndGenericModel(@Param("tenantId") String tenantId,
                                                                        @Param("genericModel") GenericModel genericType,
                                                                        Pageable pageable);

}
