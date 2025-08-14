package io.github.sivaramanr.genericmodel.entity;

import io.github.sivaramanr.genericmodel.converter.LocaleConverter;
import jakarta.persistence.*;

import java.util.Locale;

@Entity
@Table(name = "generic_model_description_translation")
public class GenericModelDescriptionTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LocaleConverter.class)
    @Column(name = "language", length = 20)
    private Locale language;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generic_model_id")
    private GenericModel genericModel;

}
