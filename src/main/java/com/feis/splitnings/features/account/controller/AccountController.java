package com.feis.splitnings.features.account.controller;

import com.feis.splitnings.common.data.dto.response.SuccessResponse;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.features.account.data.dto.request.AccountCreateDto;
import com.feis.splitnings.features.account.data.dto.request.AccountUpdateDto;
import com.feis.splitnings.features.account.data.dto.response.AccountDto;
import com.feis.splitnings.features.account.service.AccountService;
import com.feis.splitnings.security.utils.SecurityUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/account")
@Tag(name = "Account", description = "Account CRUD operations")
public class AccountController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "Account";
    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Get an " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<AccountDto>> getAccountById(
            @PathVariable(name = ID_NAME) Integer id) {
        AccountDto dto = accountService.getByIdAndUserId(id, SecurityUtils.getJwtUserId());

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<AccountDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was found", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Create a new " + ITEM_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ITEM_NAME + " was created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " with account name for the requesting user already exists", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<AccountDto>> createAccount(
            @RequestBody(required = true) AccountCreateDto createDto) {
        AccountDto dto = accountService.create(createDto);
        logger.info("New {} was created with {}:{}", ITEM_NAME, ID_NAME, dto.toString());

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<AccountDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Update an " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " with account name for the requesting user already exists", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<AccountDto>> updateAccountById(
            @PathVariable(name = ID_NAME) Integer id,
            @RequestBody(required = true) AccountUpdateDto updateDto) {
        AccountDto dto = accountService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<AccountDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was updated", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Delete an " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ITEM_NAME + " was deleted", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<?>> deleteAccountById(
            @PathVariable(name = ID_NAME) Integer id) {
        accountService.delete(id);

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        SuccessResponse<?> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was deleted", null);

        return ResponseEntity.status(responseStatus).body(response);
    }
}
