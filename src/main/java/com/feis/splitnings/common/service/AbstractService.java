package com.feis.splitnings.common.service;

import com.feis.splitnings.common.data.dto.response.BasePageDto;
import com.feis.splitnings.common.data.entity.BaseAuditingEntity;
import com.feis.splitnings.common.enums.Identifiable;
import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.errors.ValidationError;
import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.common.specification.CommonSpecificationBuilder;
import com.feis.splitnings.security.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public abstract class AbstractService<ENTITY extends BaseAuditingEntity & Identifiable<?>, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE> {
    @Autowired
    protected Validator validator;

    protected static Logger logger = LogManager.getLogger(AbstractService.class);

    protected BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> mapper;
    protected BaseRepository<ENTITY, PK_TYPE> repository;
    protected AbstractPermissionChecker<ENTITY> permissionChecker;
    protected String resourceName = "Resource";

    protected abstract void validateCreateDto(CREATE_DTO createDto);

    protected abstract void validateUpdateDto(UPDATE_DTO updateDto, ENTITY existing);

    protected abstract void validateDelete(PK_TYPE id);

    public GET_DTO get(PK_TYPE id) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        ENTITY entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));

        permissionChecker.checkReadPermission(jwtUserId, entity);

        logger.info("GetById ::: {} found with id {}", resourceName, id);

        return convertToDto(entity);
    }

    public PAGEABLE_DTO getAll(CommonSpecificationBuilder<ENTITY> specificationBuilder, Pageable pageable, boolean showTotalCount) {
        Specification<ENTITY> specification = specificationBuilder.build();

        List<ENTITY> entities;
        if (pageable != null) {
            // FIXME: Since filterring is applied later, all query results are loaded in memory
            entities = repository.findAll(specification, pageable.getSort());
        } else {
            entities = repository.findAll(specification);
        }

        entities = doFilter(entities);

        if (entities.isEmpty()) {
            logger.info("GetAll ::: No entries were found with the given parameters. Returning an empty page.");
            return convertToPageDto(Page.empty());
        }

        if (pageable == null) {
            pageable = PageRequest.of(0, entities.size());
        }

        List<GET_DTO> entitiesSplit = getEntitiesPage(entities, pageable).stream()
                .map(this::convertToDto)
                .toList();

        Page<GET_DTO> page = new PageImpl<>(entitiesSplit, pageable, entities.size());

        PAGEABLE_DTO dto = convertToPageDto(page);

        if (showTotalCount) {
            dto.setTotalCount(page.getTotalElements());
        } else {
            dto.setTotalCount(null);
        }

        int pageNumber = page.getPageable().getPageNumber();
        logger.info("GetAll ::: Found {} total results. Displaying first {} items on page {}", entities.size(), page.getNumberOfElements(), pageNumber);

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO create(CREATE_DTO createDto) {
        doValidate(createDto);
        validateCreateDto(createDto);
        ENTITY entity = convertToEntity(createDto);
        doCreate(entity);
        ENTITY saved = repository.saveAndFlush(entity);

        logger.info("Create ::: Created new {} with id {}", resourceName, saved.getId());

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO update(PK_TYPE id, UPDATE_DTO updateDto) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        ENTITY existing = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));

        permissionChecker.checkUpdatePermission(jwtUserId, existing);

        doValidate(updateDto);
        validateUpdateDto(updateDto, existing);

        doUpdate(existing, updateDto);
        convertUpdateDtoToEntity(updateDto, existing);

        ENTITY saved = repository.saveAndFlush(existing);

        logger.info("Update ::: Updated {} with id {}", resourceName, id);

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PK_TYPE id) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Optional<ENTITY> existing = repository.findById(id);

        if (existing.isEmpty()) {
            logger.info("Delete ::: {} with id {} does not exist. Early returning.", resourceName, id);
            return;
        }

        ENTITY toDelete = existing.get();

        permissionChecker.checkDeletePermission(jwtUserId, toDelete);

        validateDelete(id);
        doDelete(toDelete);

        repository.delete(toDelete);

        logger.info("Delete ::: Deleted {} with id {}", resourceName, toDelete.getId());
    }

    protected void doValidate(Object dto) {
        Set<ConstraintViolation<Object>> validationErrors = validator.validate(dto);
        List<Error> appValidationerrors = new ArrayList<>();

        for (ConstraintViolation<?> validationError : validationErrors) {
            String fieldName = validationError.getPropertyPath().iterator().next().getName();
            appValidationerrors.add(
                    new ValidationError(fieldName, InternalErrorCode.PARAMETER_INVALID, validationError.getMessage()));
        }
        if (appValidationerrors.size() >= 1) {
            throw new ValidationException(appValidationerrors);
        }
    }

    // Override this if you expect filtering based on
    // user roles or so
    protected List<ENTITY> doFilter(List<ENTITY> entityPage) {
        if (entityPage == null) {
            return new ArrayList<>();
        }
        return entityPage;
    }

    protected void doCreate(ENTITY toCreate) {
    }

    protected void doUpdate(ENTITY toUpdate, UPDATE_DTO updateDto) {
    }

    protected void doDelete(ENTITY entity) {
    }

    public GET_DTO convertToDto(ENTITY entity) {
        return mapper.mapToDto(entity);
    }

    public ENTITY convertDtoToEntity(GET_DTO getDto) {
        return mapper.mapToEntity(getDto);
    }

    public PAGEABLE_DTO convertToPageDto(Page<GET_DTO> itemPage) {
        return mapper.mapPageToPageableDto(itemPage);
    }

    public ENTITY convertToEntity(CREATE_DTO createDto) {
        return mapper.mapCreateDtoToEntity(createDto);
    }


    public ENTITY convertUpdateDtoToEntity(UPDATE_DTO updateDto, ENTITY toUpdate) {
        mapper.mapUpdateDtoToEntity(updateDto, toUpdate);
        return toUpdate;
    }

    public UPDATE_DTO convertToUpdateDto(ENTITY entity) {
        return mapper.mapToUpdateDto(entity);
    }

    /**
     * Splits the provided list to the required
     * page, returning a portion of the same list.
     */
    private List<ENTITY> getEntitiesPage(List<ENTITY> entities, Pageable pageable) {
        if (pageable == null) {
            return entities;
        }

        if (entities.size() <= pageable.getOffset()) {
            return new ArrayList<>();
        }

        int start = (int) pageable.getOffset();

        int endIndex = start + pageable.getPageSize();
        int end = Math.min(endIndex, entities.size());

        return entities.subList(start, end);
    }
}

