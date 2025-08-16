package io.github.sivaramanr.genericmodel.service;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.genericmodel.dao.GenericModelDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelDescriptionTranslationDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelNameTranslationDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelValueTranslationDAO;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenericModelServiceImpl implements GenericModelService {

    @Autowired
    private GenericModelDAO genericModelDAO;

    @Autowired
    private GenericModelNameTranslationDAO genericModelNameTranslationDAO;

    @Autowired
    private GenericModelValueTranslationDAO genericModelValueTranslationDAO;

    @Autowired
    private GenericModelDescriptionTranslationDAO genericModelDescriptionTranslationDAO;

    @Autowired
    private Converter<GenericModel, GenericModelBean> converter;

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

    @Override
    public Optional<GenericModelBean> getGenericModel(String tenentId, String id, String acceptLanguage) {
        final Optional<GenericModel> genericModelOptional = genericModelDAO
            .getGenericModel(tenentId, id);
        if (genericModelOptional.isPresent()) {
            final GenericModel genericModel = genericModelOptional.get();
            GenericModelBean genericModelBean = converter.convert(genericModel);
            if (acceptLanguage != null && genericModelBean != null)
            {
                genericModelNameTranslationDAO
                        .getGenericModelTranslation(tenentId, genericModel, acceptLanguage)
                        .ifPresent(translation -> genericModelBean.setName(translation.getName()));
                genericModelValueTranslationDAO
                        .getGenericModelTranslation(tenentId, genericModel, acceptLanguage)
                        .ifPresent(translation -> genericModelBean.setValue(translation.getValue()));
                genericModelDescriptionTranslationDAO
                        .getGenericModelTranslation(tenentId, genericModel, acceptLanguage)
                        .ifPresent(translation -> genericModelBean.setDescription(translation.getDescription()));
            }
            return genericModelBean != null? Optional.of(genericModelBean): Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Page<GenericModelBean> getGenericModels(String tenantId, GenericType genericType, String acceptLanguage,
                                                   Integer page, Integer size) {
        return genericModelDAO
            .getGenericModels(tenantId, genericType, page, size)
            .map(genericModel -> {
                GenericModelBean genericModelBean = converter.convert(genericModel);
                if (acceptLanguage != null && genericModelBean != null)
                {
                    genericModelNameTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setName(translation.getName()));
                    genericModelValueTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setValue(translation.getValue()));
                    genericModelDescriptionTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setDescription(translation.getDescription()));
                }
                return genericModelBean;
            });
    }

    @Override
    public Page<GenericModelBean> searchGenericModels(String tenantId, String name, GenericType type, StatusType status,
                                                      String acceptLanguage, Integer page, Integer size) {
        return genericModelDAO
            .searchGenericModel(tenantId, name, type, status)
            .map(genericModel -> {
                GenericModelBean genericModelBean = converter.convert(genericModel);
                if (acceptLanguage != null && genericModelBean != null)
                {
                    genericModelNameTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setName(translation.getName()));
                    genericModelValueTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setValue(translation.getValue()));
                    genericModelDescriptionTranslationDAO
                            .getGenericModelTranslation(tenantId, genericModel, acceptLanguage)
                            .ifPresent(translation -> genericModelBean.setDescription(translation.getDescription()));
                }
                return genericModelBean;
            });
    }

}
