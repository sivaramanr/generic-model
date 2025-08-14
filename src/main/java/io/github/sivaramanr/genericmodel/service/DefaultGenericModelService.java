package io.github.sivaramanr.genericmodel.service;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.genericmodel.dao.GenericModelDAO;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultGenericModelService implements GenericModelService {

    @Autowired
    private GenericModelDAO genericModelDAO;

    @Autowired
    private Converter<GenericModel, GenericModelBean> converter;

    @Override
    public Page<GenericModelBean> getGenericModels(String tenentId, GenericType genericType,
                                                   Integer page, Integer size) {
        return genericModelDAO
            .getGenericModels(tenentId, genericType, page, size)
            .map(this.converter::convert);
    }

    @Override
    public Optional<GenericModelBean> getGenericModel(String tenentId, String id) {
        return genericModelDAO
            .getGenericModel(tenentId, id)
            .map(this.converter::convert);
    }

    @Override
    public String createGenericModel(String tenentId, GenericModelBean genericModelBean, String createdBy) {
        return genericModelDAO.createGenericModel(tenentId, genericModelBean, createdBy);
    }

    @Override
    public void updateGenericModel(String tenentId, String id, GenericModelBean genericModelBean, String updatedBy) {
        genericModelDAO.updateGenericModel(tenentId, id, genericModelBean, updatedBy);
    }

    @Override
    public void deleteGenericModel(String tenentId, String id, String updatedBy) {
        genericModelDAO.deleteGenericModel(tenentId, id, updatedBy);
    }

}
