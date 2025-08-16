package io.github.sivaramanr.genericmodel.dao;

import io.github.sivaramanr.common.types.GenericType;
import io.github.sivaramanr.common.types.StatusType;
import io.github.sivaramanr.genericmodel.entity.GenericModel;
import org.springframework.data.jpa.domain.Specification;

public class GenericModelSpecifications {

    public static Specification<GenericModel> tenantIdEquals(String tenantId) {
        return (root, query, cb) ->
                tenantId == null ? null : cb.equal(root.get("tenantId"), tenantId);
    }

    public static Specification<GenericModel> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<GenericModel> nameStartsWith(String prefix) {
        return (root, query, cb) ->
                prefix == null ? null : cb.like(cb.lower(root.get("name")), prefix.toLowerCase() + "%");
    }

    public static Specification<GenericModel> nameEndsWith(String suffix) {
        return (root, query, cb) ->
                suffix == null ? null : cb.like(cb.lower(root.get("name")), "%" + suffix.toLowerCase());
    }

    public static Specification<GenericModel> genericTypeEquals(GenericType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("genericType"), type);
    }

    public static Specification<GenericModel> statusEquals(StatusType status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<GenericModel> notDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("deleted"));
    }

}
