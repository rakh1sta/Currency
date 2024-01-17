package com.example.version_2.mapper;


import com.example.version_2.entity.BaseEntity;

public interface AbstractMapper<E extends BaseEntity, D, CD> extends BaseMapper{

    D toDto(E e);
    E toEntity(CD e);

}