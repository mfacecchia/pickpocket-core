package com.feis.splitnings.features.account.mapper;

import com.feis.splitnings.common.mapper.BaseMapper;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.data.dto.request.AccountCreateDto;
import com.feis.splitnings.features.account.data.dto.request.AccountUpdateDto;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.data.dto.response.AccountPageDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements BaseMapper<Account, AccountDto, AccountCreateDto, AccountUpdateDto, AccountPageDto> {

    @Override
    public Account mapToEntity(AccountDto dto) {
        Account entity = new Account();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWealth(dto.getWealth());
        entity.setUserId(dto.getUserId());

        // Auditing
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setDeleted(dto.getDeleted());
        entity.setModifiedBy(dto.getModifiedBy());
        entity.setModifiedDate(dto.getModifiedDate());

        return entity;
    }

    @Override
    public AccountDto mapToDto(Account entity) {
        AccountDto dto = new AccountDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWealth(entity.getWealth());
        dto.setUserId(entity.getUserId());

        // Auditing
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setDeleted(entity.getDeleted());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());

        return dto;
    }

    @Override
    public AccountPageDto mapPageToPageableDto(Page<AccountDto> page) {
        AccountPageDto pageDto = new AccountPageDto();

        pageDto.setContent(page.getContent());
        pageDto.setPageElements(page.getNumberOfElements());
        pageDto.setTotalCount(page.getTotalElements());

        return pageDto;
    }

    @Override
    public Account mapCreateDtoToEntity(AccountCreateDto createDto) {
        Account entity = new Account();

        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());

        return entity;
    }

    @Override
    public void mapUpdateDtoToEntity(AccountUpdateDto updateDto, Account toUpdate) {
        toUpdate.setName(updateDto.getName());
        toUpdate.setDescription(updateDto.getDescription());
    }

    @Override
    public AccountUpdateDto mapToUpdateDto(Account entity) {
        AccountUpdateDto updateDto = new AccountUpdateDto();

        updateDto.setName(entity.getName());
        updateDto.setDescription(entity.getDescription());

        return updateDto;
    }
}
