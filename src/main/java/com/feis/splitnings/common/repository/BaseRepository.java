package com.feis.splitnings.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<ENTITY, PK_TYPE> extends JpaRepository<ENTITY, PK_TYPE>, JpaSpecificationExecutor<ENTITY> {
}
