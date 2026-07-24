package com.feis.splitnings.features.cashTransfer.controller;

import com.feis.splitnings.common.data.dto.response.SuccessResponse;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.common.specification.CommonSpecificationBuilder;
import com.feis.splitnings.common.specification.PageableUtil;
import com.feis.splitnings.features.cashTransfer.data.CashTransfer;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferCreateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.request.CashTransferUpdateDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferDto;
import com.feis.splitnings.features.cashTransfer.data.dto.response.CashTransferPageDto;
import com.feis.splitnings.features.cashTransfer.data.enums.Field;
import com.feis.splitnings.features.cashTransfer.orchestrator.CashTransferOrchestrator;
import com.feis.splitnings.features.cashTransfer.service.CashTransferService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/cash-transfers")
@Tag(name = "Cash transfer", description = "Cash transfer CRUD operations")
public class CashTransferController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "Cash Transfer";
    private final CashTransferOrchestrator cashTransferOrchestrator;
    private final CashTransferService cashTransferService;

    @Operation(summary = "Get a " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<CashTransferDto>> getCashTransferById(
            @PathVariable(name = ID_NAME) Integer id) {
        CashTransferDto dto = cashTransferService.get(id);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<CashTransferDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was found", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Get all " + ITEM_NAME + "s")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + "s found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping()
    public ResponseEntity<SuccessResponse<CashTransferPageDto>> getAllCashTransfers(
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "fromSplitId", required = false) Long fromSplitId,
            @RequestParam(name = "toSplitId", required = false) Long toSplitId,
            @RequestParam(name = "accountId", required = false) Long accountId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(name = "showTotalPageCount", required = false, defaultValue = "true") Boolean showTotalPageCount) {

        Pageable pageable = PageableUtil.buildPageable(page, size, sortBy, sortDirection);
        CommonSpecificationBuilder<CashTransfer> specificationBuilder = new CommonSpecificationBuilder<CashTransfer>()
                .like(Field.description.name(), description, false)
                .whereEqualTo(Field.fromSplitId.name(), fromSplitId, false)
                .whereEqualTo(Field.toSplitId.name(), toSplitId, false)
                .whereEqualTo(Field.accountId.name(), accountId, false);

        CashTransferPageDto pageDto = cashTransferService.getAll(specificationBuilder, pageable, showTotalPageCount);
        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<CashTransferPageDto> response = new SuccessResponse<>(responseStatus.value(), pageDto.getTotalCount() + " " + ITEM_NAME + "s found", pageDto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Create a new " + ITEM_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ITEM_NAME + " was created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Linked split is not active", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<CashTransferDto>> createCashTransfer(
            @RequestBody(required = true) CashTransferCreateDto createDto) {

        CashTransferDto dto = cashTransferOrchestrator.transfer(createDto);

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<CashTransferDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Update a " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<CashTransferDto>> updateCashTransferById(
            @PathVariable(name = ID_NAME) Integer id,
            @RequestBody(required = true) CashTransferUpdateDto updateDto) {

        CashTransferDto dto = cashTransferService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<CashTransferDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was updated", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }
}

