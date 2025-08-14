package io.github.sivaramanr.genericmodel.entity;

import io.github.sivaramanr.genericmodel.converter.LocaleConverter;
import jakarta.persistence.*;

import java.util.Locale;

@Entity
@Table(name = "generic_model_name_translation")
public class GenericModelNameTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LocaleConverter.class)
    @Column(name = "language", length = 20)
    private Locale language;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generic_model_id")
    private GenericModel genericModel;

}
