package com.example.version_2.repositiry.impls;

import com.example.version_2.entity.impls.CbuCcyEntity;
import com.example.version_2.repositiry.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CbuCcyRepository extends JpaRepository<CbuCcyEntity, Integer>, BaseRepository {
    Optional<CbuCcyEntity> findByCurrency(String integer);
}
