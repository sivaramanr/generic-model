package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import io.github.sivaramanr.genericmodel.repository.GenericModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.data.jpa.domain.Specification.where;

@Slf4j
@Component
public class GenericModelDAOImpl implements GenericModelDAO {

    @Autowired
    private GenericModelRepository genericModelRepository;

    @Override
    public String createGenericModel(String tenantId, GenericModelBean genericModelBean, String createdBy) {
        final GenericModel genericModel = new GenericModel();
        genericModel.setTenantId(tenantId);
        genericModel.setGenericType(genericModelBean.getGenericType());
        genericModel.setName(genericModelBean.getName());
        genericModel.setValue(genericModelBean.getValue());
        genericModel.setValueType(genericModelBean.getValueType());
        genericModel.setDescription(genericModelBean.getDescription());
        genericModel.setSortOrder(genericModelBean.getSortOrder());
        genericModel.setParentId(genericModelBean.getParentId());
        genericModel.setStatus(genericModelBean.getStatus());
        genericModel.setCreatedBy(createdBy);
        genericModel.setDeleted(false);
        genericModelRepository.save(genericModel);
        log.debug("Created GenericModel with ID: {}", genericModel.getId());
        return genericModel.getId();
    }

    @Override
    public void updateGenericModel(String tenantId, String id, GenericModelBean genericModelBean, String updatedBy) {
        genericModelRepository.findActiveByTenantIdAndId(tenantId, id)
        .ifPresent(genericModel -> {
            ofNullable(genericModelBean.getGenericType()).ifPresent(genericModel::setGenericType);
            ofNullable(genericModelBean.getName()).ifPresent(genericModel::setName);
            ofNullable(genericModelBean.getValue()).ifPresent(genericModel::setValue);
            ofNullable(genericModelBean.getValueType()).ifPresent(genericModel::setValueType);
            ofNullable(genericModelBean.getDescription()).ifPresent(genericModel::setDescription);
            ofNullable(genericModelBean.getSortOrder()).ifPresent(genericModel::setSortOrder);
            ofNullable(genericModelBean.getParentId()).ifPresent(genericModel::setParentId);
            ofNullable(genericModelBean.getStatus()).ifPresent(genericModel::setStatus);
            genericModel.setUpdatedBy(updatedBy);
            genericModelRepository.save(genericModel);
            log.debug("Updated GenericModel with ID: {}", id);
        });
    }

    @Override
    public void deleteGenericModel(String tenantId, String id, String updatedBy) {
        genericModelRepository.findActiveByTenantIdAndId(tenantId, id).ifPresent(genericModel -> {
            genericModel.setDeleted(true);
            genericModel.setUpdatedBy(updatedBy);
            genericModelRepository.save(genericModel);
            log.debug("Soft-deleted GenericModel with ID: {}", id);
        });
    }

    @Override
    public Optional<GenericModel> getGenericModel(String tenantId, String id) {
        return genericModelRepository.findActiveByTenantIdAndId(tenantId, id);
    }

    @Override
    public Page<GenericModel> getGenericModels(String tenentId, GenericType genericType, Integer page, Integer size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return genericModelRepository.findAllByTenantIdAndGenericType(tenentId, genericType, pageRequest);
    }

    @Override
    public Page<GenericModel> searchGenericModel(String tenantId, String name, GenericType type, StatusType status) {
        final PageRequest pageRequest = PageRequest.of(1, 2);
        return genericModelRepository.findAll(
            where(GenericModelSpecifications.tenantIdEquals(tenantId))
                .and(GenericModelSpecifications.nameContains(name))
                .and(GenericModelSpecifications.genericTypeEquals(type))
                .and(GenericModelSpecifications.statusEquals(status))
                .and(GenericModelSpecifications.notDeleted()),
                pageRequest
        );
    }

}
