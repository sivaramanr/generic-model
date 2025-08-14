package io.github.sivaramanr.genericmodel.facade;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GenericModelFacade {

    Page<GenericModelBean> getGenericModels(String tenentId, GenericType genericType, Integer page, Integer size);

    Optional<GenericModelBean> getGenericModel(String tenentId, String id);

    String createGenericModel(String tenentId, GenericModelBean genericModelBean, String createdBy);

    void updateGenericModel(String tenentId, String id, GenericModelBean genericModelBean, String updatedBy);

    void deleteGenericModel(String tenentId, String id, String updatedBy);

}
