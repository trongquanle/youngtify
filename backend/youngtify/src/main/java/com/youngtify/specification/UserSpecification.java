package com.youngtify.specification;

import com.youngtify.entity.UserRequestEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public final class UserSpecification {

    public static Specification<UserRequestEntity> in(String field, Object ids) {
        return (root, query, cb) -> root.get(field).in(ids);
    }

    public static Specification<UserRequestEntity> equal(String field, Object value) {
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

}
