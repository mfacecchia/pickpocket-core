package com.feis.splitnings.common.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.feis.splitnings.common.data.dto.BasePageDto;
import com.feis.splitnings.common.data.entity.BaseEntityWithImage;
import com.feis.splitnings.common.exception.ResourceNotFoundException;

import lombok.SneakyThrows;

public abstract class AbstractFileService<ENTITY extends BaseEntityWithImage, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE>
    extends AbstractService<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO, PK_TYPE> {

        abstract String upload(MultipartFile file);

        abstract InputStream getStreamByName(String filename);

        abstract String delete(String filename);

        @SneakyThrows
        public InputStream getFile(PK_TYPE id) {
            ENTITY existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

            if(existing.getFilename() == null || existing.getFilename().isBlank()) {
                return new ByteArrayInputStream(new byte[0]);
            }

            InputStream fileStream = getStreamByName(existing.getFilename());

            logger.info("GetFile ::: File found with name {} and id {}", existing.getFilename(), id);

            return fileStream;
        }

        @Transactional
        public GET_DTO create(CREATE_DTO createDto, MultipartFile file) {
            doValidate(createDto);
            validateCreateDto(createDto);

            ENTITY entity = convertToEntity(createDto);

            if (file != null && !file.isEmpty()) {
                doCreate(entity, file);
            } else {
                doCreate(entity);
            }

            ENTITY saved = save(entity);

            logger.info("Create ::: File created and uploaded with name {} and id {}", saved.getFilename(), saved.getId());

            return convertToDto(saved);
        }

        @SneakyThrows
        protected void doCreate(ENTITY toCreate, MultipartFile file) {
            String filename = upload(file);
            toCreate.setFilename(filename);
        }

        @SneakyThrows
        @Override
        protected void doDelete(ENTITY toDelete) {
            delete(toDelete.getFilename());
        }
}
