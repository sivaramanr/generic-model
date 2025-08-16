package io.github.sivaramanr.genericmodel.converter;

import io.github.sivaramanr.common.GenericModelBean;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenericModelToBeanConverter implements Converter<GenericModel, GenericModelBean> {

    @Override
    public GenericModelBean convert(GenericModel genericModel) {
        final GenericModelBean genericBean = new GenericModelBean();
        genericBean.setId(genericModel.getId());
        genericBean.setName(genericModel.getName());
        genericBean.setGenericType(genericModel.getGenericType());
        genericBean.setName(genericModel.getName());
        genericBean.setValue(genericModel.getValue());
        genericBean.setStatus(genericModel.getStatus());
        genericBean.setDescription(genericModel.getDescription());
        genericBean.setSortOrder(genericModel.getSortOrder());
        genericBean.setCreatedBy(genericModel.getCreatedBy());
        genericBean.setCreatedAt(genericModel.getCreatedAt());
        genericBean.setUpdatedBy(genericModel.getUpdatedBy());
        genericBean.setUpdatedAt(genericModel.getUpdatedAt());
        return genericBean;
    }

}
