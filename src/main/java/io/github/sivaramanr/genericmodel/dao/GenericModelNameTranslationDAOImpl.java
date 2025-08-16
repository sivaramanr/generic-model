package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.entity.GenericModelNameTranslation;
import io.github.sivaramanr.genericmodel.repository.GenericModelNameTranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GenericModelNameTranslationDAOImpl implements GenericModelNameTranslationDAO {

    @Autowired
    private GenericModelNameTranslationRepository translationRepository;

    @Override
    public String createGenericModelTranslation(String tenantId, GenericModel genericModel,
                                                GenericModelTranslationBean genericModelTranslationBean,
                                                String createdBy) {
        final GenericModelNameTranslation translation = new GenericModelNameTranslation();
        translation.setTenantId(tenantId);
        translation.setLocale(genericModelTranslationBean.getLocale());
        translation.setName(genericModelTranslationBean.getText());
        translation.setCreatedBy(createdBy);
        translation.setGenericModel(genericModel);
        translationRepository.save(translation);
        log.debug("Created GenericModelNameTranslation with : {}", translation.getLocale());
        return translation.getLocale();
    }

    @Override
    public void updateGenericModelTranslation(String tenantId, GenericModel genericModel,
                                              String locale, String text, String updatedBy) {
        translationRepository.findByTenantIdAndGenericModelAndLocale(tenantId, genericModel, locale)
            .ifPresent(translation -> {
                translation.setTenantId(tenantId);
                translation.setName(text);
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
    public Optional<GenericModelNameTranslation> getGenericModelTranslation(String tenantId, GenericModel genericModel, String locale) {
        return translationRepository.findByTenantIdAndGenericModelAndLocale(tenantId, genericModel, locale);
    }

    @Override
    public Page<GenericModelNameTranslation> getGenericModelTranslations(String tenentId, GenericModel genericModel,
                                                                         Integer page, Integer size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return translationRepository.findAllByTenantIdAndGenericModel(tenentId, genericModel, pageRequest);
    }

}
