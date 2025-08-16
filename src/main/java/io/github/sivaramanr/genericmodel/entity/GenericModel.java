package io.github.sivaramanr.genericmodel.entity;

import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.common.types.ValueType;
import io.github.sivaramanr.genericmodel.converter.GenericTypeConverter;
import io.github.sivaramanr.genericmodel.converter.StatusTypeConverter;
import io.github.sivaramanr.genericmodel.converter.ValueTypeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "generic_model",
        indexes = {
                @Index(name = "idx_generic_model_type", columnList = "generic_type, tenant_id"),
                @Index(name = "idx_generic_model_id", columnList = "id")
        }
)
@NoArgsConstructor
public class GenericModel {

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;

    @Convert(converter = GenericTypeConverter.class)
    @Column(name = "generic_type", nullable = false, length = 100)
    private GenericType genericType;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "generic_value")
    private String value;

    @Convert(converter = ValueTypeConverter.class)
    @Column(name = "value_type", nullable = false, length = 100)
    private ValueType valueType = ValueType.STRING;

    private Boolean deleted = Boolean.FALSE;

    @Column(length = 1000)
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "parent_id", length = 36)
    private String parentId;

    @Convert(converter = StatusTypeConverter.class)
    private StatusType status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "genericModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenericModelNameTranslation> nameTranslations = new ArrayList<>();

    @OneToMany(mappedBy = "genericModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenericModelValueTranslation> valueTranslations = new ArrayList<>();

    @OneToMany(mappedBy = "genericModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenericModelDescriptionTranslation> descriptionTranslations = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
