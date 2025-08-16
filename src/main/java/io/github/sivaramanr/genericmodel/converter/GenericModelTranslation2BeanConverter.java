package io.github.sivaramanr.genericmodel.converter;

import io.github.sivaramanr.common.GenericModelTranslationBean;
import io.github.sivaramanr.genericmodel.entity.GenericModelDescriptionTranslation;
import io.github.sivaramanr.genericmodel.entity.GenericModelNameTranslation;
import io.github.sivaramanr.genericmodel.entity.GenericModelTranslation;
import io.github.sivaramanr.genericmodel.entity.GenericModelValueTranslation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenericModelTranslation2BeanConverter  implements Converter<GenericModelTranslation,
        GenericModelTranslationBean> {

    @Override
    public GenericModelTranslationBean convert(GenericModelTranslation source) {
        GenericModelTranslationBean bean = new GenericModelTranslationBean();
        bean.setLocale(source.getLocale());
        if (source instanceof GenericModelNameTranslation translation) {
            bean.setText(translation.getName());
        } else if (source instanceof GenericModelValueTranslation translation) {
            bean.setText(translation.getValue());
        } else if (source instanceof GenericModelDescriptionTranslation translation) {
            bean.setText(translation.getDescription());
        }
        bean.setCreatedAt(source.getCreatedAt());
        bean.setCreatedBy(source.getCreatedBy());
        bean.setUpdatedAt(source.getUpdatedAt());
        bean.setUpdatedBy(source.getUpdatedBy());
        return bean;
    }

}
