package com.example.version_2.repositiry.impls;

import com.example.version_2.entity.impls.NbuCcyEntity;
import com.example.version_2.repositiry.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NbuCcyRepository extends JpaRepository<NbuCcyEntity,Integer>, BaseRepository {
    Optional<NbuCcyEntity> findByCurrency(String integer);
}
