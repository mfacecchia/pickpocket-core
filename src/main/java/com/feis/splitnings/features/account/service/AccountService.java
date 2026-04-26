package com.feis.splitnings.features.account.service;

import com.feis.splitnings.common.exception.ConflictException;
import com.feis.splitnings.common.exception.ResourceNotFoundException;
import com.feis.splitnings.common.service.AbstractService;
import com.feis.splitnings.features.account.data.Account;
import com.feis.splitnings.features.account.data.dto.request.AccountCreateDto;
import com.feis.splitnings.features.account.data.dto.request.AccountUpdateDto;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.data.dto.response.AccountPageDto;
import com.feis.splitnings.features.account.mapper.AccountMapper;
import com.feis.splitnings.features.account.repository.AccountRepository;
import com.feis.splitnings.security.utils.SecurityUtils;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends AbstractService<Account, AccountDto, AccountCreateDto, AccountUpdateDto, AccountPageDto, Integer> {
    private final AccountReadService accountReadService;
    private static final Logger logger = LogManager.getLogger(AccountService.class);

    public AccountService(AccountMapper accountMapper, AccountRepository accountRepository, AccountReadService accountReadService) {
        this.mapper = accountMapper;
        this.repository = accountRepository;
        this.resourceName = "Account";
        this.accountReadService = accountReadService;
    }

    public AccountDto getByIdAndUserId(Integer id, Integer userId) {
        Account entity = ((AccountRepository) repository).findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        logger.info("GetById ::: {} found with id {}", resourceName, id);

        return convertToDto(entity);
    }

    public List<AccountDto> getAllByUserId(Integer userId) {
        List<Account> userAccounts = ((AccountRepository) repository).findByUserId(userId);

        logger.info("GetById ::: {} found {} results with userId {}", resourceName, userAccounts.size(), userId);

        return userAccounts.stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackOn = Exception.class)
    public AccountDto topUpAccount(Integer accountId, Double topUpAmount) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Account account = accountReadService.getByIdAndUserId(accountId, jwtUserId);

        Double previousWealth = account.getWealth();

        account.setWealth(account.getWealth() + topUpAmount);
        account.setModifiedBy(jwtUserId.toString());

        repository.save(account);

        logger.info("TopUpAccount ::: Updated account {} wealth to {}. Was {}", accountId, account.getWealth(), previousWealth);

        return convertToDto(account);
    }

    // TODO: This method is kind of repetitive compared to
    // `topUpAccount`. Maybe refactor it?
    @Transactional(rollbackOn = Exception.class)
    public AccountDto chargeAccount(Integer accountId, Double chargeAmount) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        Account account = accountReadService.getByIdAndUserId(accountId, jwtUserId);

        Double previousWealth = account.getWealth();

        account.setWealth(account.getWealth() - chargeAmount);
        account.setModifiedBy(jwtUserId.toString());

        repository.save(account);

        logger.info("ChargeAccount ::: Updated account {} wealth to {}. Was {}", accountId, account.getWealth(), previousWealth);

        return convertToDto(account);
    }

    @Override
    protected void validateCreateDto(AccountCreateDto createDto) {
        Optional<Account> account = ((AccountRepository) repository).findByNameAndUserId(createDto.getName(), SecurityUtils.getJwtUserId());

        if (account.isPresent()) {
            throw new ConflictException(resourceName, "name", createDto.getName());
        }
    }

    @Override
    protected void validateUpdateDto(AccountUpdateDto updateDto, Account existing) {
        Optional<Account> account = ((AccountRepository) repository).findByNameAndUserId(updateDto.getName(), SecurityUtils.getJwtUserId());

        if (account.isPresent() && !account.get().getId().equals(existing.getId())) {
            throw new ConflictException(resourceName, "name", updateDto.getName());
        }
    }

    @Override
    protected void validateDelete(Integer id) {
        ((AccountRepository) repository).findByIdAndUserId(id, SecurityUtils.getJwtUserId())
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));
    }

    @Override
    protected Integer getResourceId(Account entity) {
        return entity.getId();
    }

    @Override
    protected void doCreate(Account toCreate) {
        Integer jwtUserId = SecurityUtils.getJwtUserId();

        toCreate.setUserId(jwtUserId);
        toCreate.setCreatedBy(jwtUserId.toString());
        toCreate.setModifiedBy(jwtUserId.toString());
    }

    @Override
    protected void doUpdate(Account toUpdate, AccountUpdateDto updateDto) {
        toUpdate.setModifiedBy(SecurityUtils.getJwtUserId().toString());
    }

    @Override
    protected void doDelete(Account toDelete) {
        toDelete.setModifiedBy(SecurityUtils.getJwtUserId().toString());
    }
}
