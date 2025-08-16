package io.github.sivaramanr.genericmodel.service;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.dao.GenericModelDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelDescriptionTranslationDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelNameTranslationDAO;
import io.github.sivaramanr.genericmodel.dao.GenericModelValueTranslationDAO;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelTranslation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GenericModelTranslationServiceImpl implements GenericModelTranslationService {

    @Autowired
    private Converter<GenericModelTranslation, GenericModelTranslationBean> converter;

    @Autowired
    private GenericModelDAO genericModelDAO;

    @Autowired
    private GenericModelNameTranslationDAO genericModelNameTranslationDAO;

    @Autowired
    private GenericModelValueTranslationDAO genericModelValueTranslationDAO;

    @Autowired
    private GenericModelDescriptionTranslationDAO genericModelDescriptionTranslationDAO;

    @Override
    public String createGenericModelTranslation(String tenentId, String genericModelId, String field,
                                                GenericModelTranslationBean genericModelBean, String createdBy) {
        final GenericModel genericModel = genericModelDAO
                .getGenericModel(tenentId, genericModelId)
                .orElseThrow(() -> new IllegalArgumentException("Generic model not found"));
        if (field.equals("name")) {
            return genericModelNameTranslationDAO.createGenericModelTranslation(tenentId, genericModel, genericModelBean, createdBy);
        } else if (field.equals("value")) {
            return genericModelValueTranslationDAO.createGenericModelTranslation(tenentId, genericModel, genericModelBean, createdBy);
        } else if (field.equals("description")) {
            return genericModelDescriptionTranslationDAO.createGenericModelTranslation(tenentId, genericModel, genericModelBean, createdBy);
        } else {
            throw new IllegalArgumentException("Invalid field type: " + field);
        }
    }

    @Override
    public void updateGenericModelTranslation(String tenentId, String genericModelId, String field,
                                              String locale, String text, String updatedBy) {
        final GenericModel genericModel = genericModelDAO
                .getGenericModel(tenentId, genericModelId)
                .orElseThrow(() -> new IllegalArgumentException("Generic model not found"));
        if (field.equals("name")) {
            genericModelNameTranslationDAO.updateGenericModelTranslation(tenentId, genericModel, locale, text, updatedBy);
        } else if (field.equals("value")) {
            genericModelValueTranslationDAO.updateGenericModelTranslation(tenentId, genericModel, locale, text, updatedBy);
        } else if (field.equals("description")) {
            genericModelDescriptionTranslationDAO.updateGenericModelTranslation(tenentId, genericModel, locale, text, updatedBy);
        }
    }

    @Transactional
    @Override
    public void deleteGenericModelTranslation(String tenentId, String genericModelId, String type, String locale) {
        final GenericModel genericModel = genericModelDAO
                .getGenericModel(tenentId, genericModelId)
                .orElseThrow(() -> new IllegalArgumentException("Generic model not found"));
        if (type.equals("name")) {
            genericModelNameTranslationDAO.deleteGenericModelTranslation(tenentId, genericModel, locale);
        } else if (type.equals("value")) {
            genericModelValueTranslationDAO.deleteGenericModelTranslation(tenentId, genericModel, locale);
        } else if (type.equals("description")) {
            genericModelDescriptionTranslationDAO.deleteGenericModelTranslation(tenentId, genericModel, locale);
        }
    }

    @Override
    public Optional<GenericModelTranslationBean> getGenericModelTranslation(String tenentId, String genericModelId, String type, String locale) {
        final GenericModel genericModel = genericModelDAO
                .getGenericModel(tenentId, genericModelId)
                .orElseThrow(() -> new IllegalArgumentException("Generic model not found"));
        if (type.equals("name")) {
            return genericModelNameTranslationDAO.getGenericModelTranslation(tenentId, genericModel, locale).map(this.converter::convert);
        } else if (type.equals("value")) {
            return genericModelValueTranslationDAO.getGenericModelTranslation(tenentId, genericModel, locale).map(this.converter::convert);
        } else if (type.equals("description")) {
            return genericModelDescriptionTranslationDAO.getGenericModelTranslation(tenentId, genericModel, locale).map(this.converter::convert);
        }
        return Optional.empty();
    }

    @Override
    public Page<GenericModelTranslationBean> getGenericModelTranslations(String tenentId, String genericModelId,
                                                                         String type, Integer page, Integer size) {
        final GenericModel genericModel = genericModelDAO.getGenericModel(tenentId, genericModelId)
                .orElseThrow(() -> new IllegalArgumentException("Generic model not found"));
        if (type.equals("name")) {
            return genericModelNameTranslationDAO
                    .getGenericModelTranslations(tenentId, genericModel, page, size)
                    .map(this.converter::convert);
        } else if (type.equals("value")) {
            return genericModelValueTranslationDAO
                    .getGenericModelTranslations(tenentId, genericModel, page, size)
                    .map(this.converter::convert);
        } else if (type.equals("description")) {
            return genericModelDescriptionTranslationDAO
                    .getGenericModelTranslations(tenentId, genericModel, page, size)
                    .map(this.converter::convert);
        }
        return Page.empty();
    }

}
