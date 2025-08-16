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
    name = "generic_model_description_translation",
    indexes = {
        @Index(name = "idx_description_translation_generic_model_id_locale", columnList = "generic_model_id, id"),
        @Index(name = "idx_description_translation_generic_model_id", columnList = "generic_model_id")
    }
)
public class GenericModelDescriptionTranslation extends GenericModelTranslation {

    @Column(name = "description", length = 1000)
    private String description;

}
