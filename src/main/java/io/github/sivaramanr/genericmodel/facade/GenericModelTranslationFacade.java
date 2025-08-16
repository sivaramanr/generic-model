package io.github.sivaramanr.genericmodel.facade;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import org.springframework.data.domain.Page;

import java.util.Locale;
import java.util.Optional;

public interface GenericModelTranslationFacade {

    String createGenericModelTranslation(String tenentId, String genericModelId, String field,
                                         GenericModelTranslationBean genericModelBean, String createdBy);

    void updateGenericModelTranslation(String tenentId, String genericModelId, String field, String locale, String text, String updatedBy);

    void deleteGenericModelTranslation(String tenentId, String genericModelId, String field, String locale);

    Optional<GenericModelTranslationBean> getGenericModelTranslation(String tenentId, String genericModelId, String field, String locale);

    Page<GenericModelTranslationBean> getGenericModelTranslations(String tenentId, String genericModelId, String field, Integer page, Integer size);

}
