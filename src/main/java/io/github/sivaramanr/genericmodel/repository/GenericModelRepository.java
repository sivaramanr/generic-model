package io.github.sivaramanr.genericmodel.repository;

import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GenericModelRepository extends JpaRepository<GenericModel, String> {

    @Query("""
           SELECT gm
           FROM GenericModel gm
           WHERE gm.tenantId = :tenantId
             AND gm.id = :id
             AND gm.deleted = false
           """)
    Optional<GenericModel> findActiveByTenantIdAndId(@Param("tenantId") String tenantId,
                                                     @Param("id") String id);

    @Query(value = """
                   SELECT gm
                   FROM GenericModel gm
                   WHERE gm.tenantId = :tenantId
                     AND gm.genericType = :genericType
                     AND gm.deleted = false
                   """,
            countQuery = """
                        SELECT COUNT(gm)
                        FROM GenericModel gm
                        WHERE gm.tenantId = :tenantId
                          AND gm.genericType = :genericType
                          AND gm.deleted = false
                        """)
    Page<GenericModel> findAllByTenantIdAndGenericType(@Param("tenantId") String tenantId,
                                                       @Param("genericType") GenericType genericType,
                                                       Pageable pageable);
}
