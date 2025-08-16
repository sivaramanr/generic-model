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
    name = "generic_model_name_translation",
    indexes = {
        @Index(name = "idx_name_translation_generic_model_id_locale", columnList = "generic_model_id, id"),
        @Index(name = "idx_name_translation_generic_model_id", columnList = "generic_model_id")
    }
)
public class GenericModelNameTranslation extends GenericModelTranslation {

    @Column(name = "name", length = 500)
    private String name;

}
