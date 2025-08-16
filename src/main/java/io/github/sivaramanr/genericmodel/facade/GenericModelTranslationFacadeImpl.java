package io.github.sivaramanr.genericmodel.facade;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.service.GenericModelTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenericModelTranslationFacadeImpl implements GenericModelTranslationFacade {

    @Autowired
    private GenericModelTranslationService genericModelTranslationService;

    @Override
    public String createGenericModelTranslation(String tenentId, String genericModelId, String field,
                                                GenericModelTranslationBean genericModelBean, String createdBy) {
        final Optional<GenericModelTranslationBean> beanOptional = genericModelTranslationService
                .getGenericModelTranslation(tenentId, genericModelId, field, genericModelBean.getLocale());
        if (beanOptional.isEmpty()) {
            genericModelTranslationService.createGenericModelTranslation(tenentId, genericModelId, field, genericModelBean, createdBy);
        } else {
            throw new IllegalArgumentException("Translation already exists for locale: " + genericModelBean.getLocale());
        }
        return genericModelBean.getLocale();
    }

    @Override
    public void updateGenericModelTranslation(String tenentId, String genericModelId, String field, String locale, String text, String updatedBy) {
        genericModelTranslationService.updateGenericModelTranslation(tenentId, genericModelId, field, locale, text, updatedBy);
    }

    @Override
    public void deleteGenericModelTranslation(String tenentId, String genericModelId, String field, String locale) {
        genericModelTranslationService.deleteGenericModelTranslation(tenentId, genericModelId, field, locale);
    }

    @Override
    public Optional<GenericModelTranslationBean> getGenericModelTranslation(String tenentId, String genericModelId, String field, String locale) {
        return genericModelTranslationService.getGenericModelTranslation(tenentId, genericModelId, field, locale);
    }

    @Override
    public Page<GenericModelTranslationBean> getGenericModelTranslations(String tenentId, String genericModelId, String field, Integer page, Integer size) {
        return genericModelTranslationService.getGenericModelTranslations(tenentId, genericModelId, field, page, size);
    }

}
