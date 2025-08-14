package io.github.sivaramanr.genericmodel.repository;

import io.github.sivaramanr.genericmodel.entity.GenericModelNameTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericModelNameTranslationRepository extends JpaRepository<GenericModelNameTranslation, Long> {
}