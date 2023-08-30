package com.hasangurbuz.moviehub.api.mapper;

import java.util.ArrayList;
import java.util.List;

public interface Mapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);

    default List<D> toDtoList(List<E> entityList) {
        if (entityList == null) {
            return null;
        }
        List<D> dtoList = new ArrayList<>(entityList.size());
        for (E entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    default List<E> toEntityList(List<D> dtoList) {
        if (dtoList == null) {
            return null;
        }
        List<E> entityList = new ArrayList<>(dtoList.size());
        for (D dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

}
