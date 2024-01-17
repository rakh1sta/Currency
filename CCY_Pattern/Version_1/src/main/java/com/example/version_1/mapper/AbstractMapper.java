package com.example.version_1.mapper;


public interface AbstractMapper<E, D, CD> extends BaseMapper{

    D toDto(E e);
    E toEntity(CD e);

}