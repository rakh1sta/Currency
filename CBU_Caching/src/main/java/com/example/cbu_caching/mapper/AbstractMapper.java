package com.example.cbu_caching.mapper;


public interface AbstractMapper<E, D, CD> {

    D toDto(E e);
    E toEntity(CD e);

}