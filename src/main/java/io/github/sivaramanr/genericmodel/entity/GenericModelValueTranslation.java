package io.github.sivaramanr.genericmodel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "generic_model_value_translation",
    indexes = {
        @Index(name = "idx_value_translation_generic_model_id_locale", columnList = "generic_model_id, id"),
        @Index(name = "idx_value_translation_generic_model_id", columnList = "generic_model_id")
    }
)
public class GenericModelValueTranslation extends GenericModelTranslation {

    @Column(name = "translated_value", length = 500)
    private String value;

}
