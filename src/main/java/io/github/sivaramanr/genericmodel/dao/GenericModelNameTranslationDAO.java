package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelNameTranslation;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GenericModelNameTranslationDAO {

    String createGenericModelTranslation(String tenantId, GenericModel genericModel, GenericModelTranslationBean genericModelTranslationBean,
                                         String createdBy);

    void updateGenericModelTranslation(String tenantId, GenericModel genericModel, String locale, String text,
                                       String updatedBy);

    void deleteGenericModelTranslation(String tenantId, GenericModel genericModel, String locale);

    Optional<GenericModelNameTranslation> getGenericModelTranslation(String tenantId, GenericModel genericModel, String locale);

    Page<GenericModelNameTranslation> getGenericModelTranslations(String tenentId, GenericModel genericModel, Integer page, Integer size);

}
