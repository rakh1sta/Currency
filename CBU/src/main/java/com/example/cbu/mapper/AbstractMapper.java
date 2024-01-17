package com.example.cbu.mapper;


public interface AbstractMapper<E, D, CD> {

    D toDto(E e);
    E toEntity(CD e);

}