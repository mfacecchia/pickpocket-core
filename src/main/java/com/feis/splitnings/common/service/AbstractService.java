package com.feis.splitnings.common.service;

import com.feis.splitnings.common.data.dto.response.BasePageDto;
import com.feis.splitnings.common.data.entity.BaseAuditingEntity;
import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;
import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.errors.ValidationError;
import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.common.repository.BaseRepository;
import com.feis.splitnings.common.specification.CommonSpecificationBuilder;

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

public abstract class AbstractService<ENTITY extends BaseAuditingEntity, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE> {
    @Autowired
    protected Validator validator;

    protected static Logger logger = LogManager.getLogger(AbstractService.class);

    protected BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> mapper;
    protected BaseRepository<ENTITY, PK_TYPE> repository;
    protected String resourceName = "Resource";

    protected abstract void validateCreateDto(CREATE_DTO createDto);

    protected abstract void validateUpdateDto(UPDATE_DTO updateDto, ENTITY existing);

    protected abstract void validateDelete(PK_TYPE id);

    protected abstract PK_TYPE getResourceId(ENTITY entity);

    public GET_DTO get(PK_TYPE id) {
        ENTITY entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        logger.info("GetById ::: {} found with id {}", resourceName, id);

        return convertToDto(entity);
    }

    public PAGEABLE_DTO getAll(CommonSpecificationBuilder<ENTITY> specificationBuilder, Pageable pageable, boolean showTotalCount) {
        Specification<ENTITY> specification = specificationBuilder.build();

        if (pageable != null) {
            Page<ENTITY> entityPage = repository.findAll(specification, pageable);

            Page<GET_DTO> entityDtoPage = doFilter(entityPage).map(mapper::mapToDto);

            PAGEABLE_DTO pageableDto = convertToPageDto(entityDtoPage);

            if (showTotalCount) {
                long totalCount = entityDtoPage.getTotalElements();
                pageableDto.setTotalCount(totalCount);
            }

            logger.info("GetAll ::: Found {} total results. Displaying first {} items", entityDtoPage.getTotalElements(), entityDtoPage.getNumberOfElements());

            return pageableDto;
        }

        List<ENTITY> itemsList = repository.findAll(specification);

        Page<GET_DTO> itemsPage;
        if (itemsList.isEmpty()) {
            itemsPage = Page.empty();
        } else {
            pageable = PageRequest.of(0, itemsList.size());
            itemsPage = doFilter(new PageImpl<>(itemsList, pageable, itemsList.size())).map(mapper::mapToDto);
        }

        logger.info("GetAll ::: Found {} total results. Displaying first {} items", itemsPage.getTotalElements(), itemsPage.getNumberOfElements());

        return convertToPageDto(itemsPage);
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO create(CREATE_DTO createDto) {
        doValidate(createDto);
        validateCreateDto(createDto);
        ENTITY entity = convertToEntity(createDto);
        doCreate(entity);
        ENTITY saved = save(entity);

        logger.info("Create ::: Created new {} with id {}", resourceName, getResourceId(saved));

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO update(PK_TYPE id, UPDATE_DTO updateDto) {
        ENTITY existing = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));

        doValidate(updateDto);
        validateUpdateDto(updateDto, existing);

        convertUpdateDtoToEntity(updateDto, existing);
        doUpdate(existing, updateDto);
        ENTITY saved = save(existing);

        logger.info("Update ::: Updated {} with id {}", resourceName, id);

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PK_TYPE id) {
        validateDelete(id);
        softDelete(id);
    }

    protected void softDelete(PK_TYPE id) {
        Optional<ENTITY> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return;
        }

        ENTITY toDelete = existing.get();
        doDelete(toDelete);

        toDelete.setDeleted(true);
        save(toDelete);

        logger.info("Delete ::: Execute soft delete on {} with id {}", resourceName, id);
    }

    protected void hardDelete(PK_TYPE id) {
        repository.deleteById(id);

        logger.info("Delete ::: Execute hard delete on {} with id {}", resourceName, id);
    }

    protected ENTITY save(ENTITY entity) {
        return repository.save(entity);
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

    protected  Page<ENTITY> doFilter(Page<ENTITY> entityPage) {
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
}
