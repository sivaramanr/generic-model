package io.github.sivaramanr.genericmodel.facade;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.genericmodel.service.GenericModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultGenericModelFacade implements GenericModelFacade {

    @Autowired
    private GenericModelService genericModelService;

    @Override
    public Page<GenericModelBean> getGenericModels(String tenentId, GenericType genericType,
                                                   Integer page, Integer size) {
        return genericModelService.getGenericModels(tenentId, genericType, page, size);
    }

    @Override
    public Optional<GenericModelBean> getGenericModel(String tenentId, String id) {
        return genericModelService.getGenericModel(tenentId, id);
    }

    @Override
    public String createGenericModel(String tenentId, GenericModelBean genericModelBean, String createdBy) {
        return genericModelService.createGenericModel(tenentId, genericModelBean, createdBy);
    }

    @Override
    public void updateGenericModel(String tenentId, String id, GenericModelBean genericModelBean, String updatedBy) {
        genericModelService.updateGenericModel(tenentId, id, genericModelBean, updatedBy);
    }

    @Override
    public void deleteGenericModel(String tenentId, String id, String updatedBy) {
        genericModelService.deleteGenericModel(tenentId, id, updatedBy);
    }

}
