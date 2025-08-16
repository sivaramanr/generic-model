package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelValueTranslation;
import io.github.sivaramanr.genericmodel.repository.GenericModelValueTranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GenericModelValueTranslationDAOImpl implements GenericModelValueTranslationDAO {

    @Autowired
    private GenericModelValueTranslationRepository translationRepository;

    @Override
    public String createGenericModelTranslation(String tenantId, GenericModel genericModel,
                                                GenericModelTranslationBean genericModelTranslationBean,
                                                String createdBy) {
        final GenericModelValueTranslation translation = new GenericModelValueTranslation();
        translation.setTenantId(tenantId);
        translation.setLocale(genericModelTranslationBean.getLocale());
        translation.setValue(genericModelTranslationBean.getText());
        translation.setCreatedBy(createdBy);
        translation.setGenericModel(genericModel);
        translationRepository.save(translation);
        log.debug("Created GenericModelValueTranslation with ID: {}", translation.getLocale());
        return translation.getLocale();
    }

    @Override
    public void updateGenericModelTranslation(String tenantId, GenericModel genericModel, String locale, String text,
                                              String updatedBy) {
        translationRepository.findByTenantIdAndGenericModelAndLocale(tenantId, genericModel, locale)
            .ifPresent(translation -> {
                translation.setTenantId(tenantId);
                translation.setValue(text);
                translation.setUpdatedBy(updatedBy);
                translationRepository.save(translation);
                log.debug("Updated GenericModelNameTranslation with : {}", translation.getLocale());
            });
    }

    @Override
    public void deleteGenericModelTranslation(String tenantId, GenericModel genericModel, String locale) {
        translationRepository.deleteByTenantIdAndGenericModelAndLocale(tenantId, genericModel, locale);
    }

    @Override
    public Optional<GenericModelValueTranslation> getGenericModelTranslation(String tenantId, GenericModel genericModel, String locale) {
        return translationRepository.findByTenantIdAndGenericModelAndLocale(tenantId, genericModel, locale);
    }

    @Override
    public Page<GenericModelValueTranslation> getGenericModelTranslations(String tenentId, GenericModel genericModel,
                                                                          Integer page, Integer size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return translationRepository.findAllByTenantIdAndGenericModel(tenentId, genericModel, pageRequest);
    }

}
