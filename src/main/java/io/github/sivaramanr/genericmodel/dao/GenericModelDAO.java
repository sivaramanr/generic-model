package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GenericModelDAO {


    String createGenericModel(String tenantId, GenericModelBean genericModelBean, String createdBy);

    void updateGenericModel(String tenantId, String id, GenericModelBean genericModelBean, String updatedBy);

    void deleteGenericModel(String tenantId, String id, String updatedBy);

    Optional<GenericModel> getGenericModel(String tenantId, String id);

    Page<GenericModel> getGenericModels(String tenentId, GenericType genericType, Integer page, Integer size);

    Page<GenericModel> searchGenericModel(String tenantId, String name, GenericType type, StatusType status);

}
