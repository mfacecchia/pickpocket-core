package com.feis.splitnings.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.feis.splitnings.common.data.entity.BaseAuditingEntity;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseAuditingEntity, PK_TYPE> extends JpaRepository<ENTITY, PK_TYPE>, JpaSpecificationExecutor<ENTITY> {

    Optional<ENTITY> findByIdAndDeleted(PK_TYPE id, Boolean deleted);
}
