package com.feis.splitnings.common.repository;

import com.feis.splitnings.common.data.entity.BaseAuditingEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseAuditingEntity, PK_TYPE> extends JpaRepository<ENTITY, PK_TYPE>, JpaSpecificationExecutor<ENTITY> {
}
