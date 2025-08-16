package io.github.sivaramanr.genericmodel.service;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GenericModelService {

    String createGenericModel(String tenentId, GenericModelBean genericModelBean, String createdBy);

    void updateGenericModel(String tenentId, String id, GenericModelBean genericModelBean, String updatedBy);

    void deleteGenericModel(String tenentId, String id, String updatedBy);

    Optional<GenericModelBean> getGenericModel(String tenentId, String id, String acceptLanguage);

    Page<GenericModelBean> getGenericModels(String tenentId, GenericType genericType, String acceptLanguage, Integer page, Integer size);

    Page<GenericModelBean> searchGenericModels(String tenantId, String name, GenericType type, StatusType status, String acceptLanguage, Integer page,
                                               Integer size);

}
