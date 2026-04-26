package com.feis.splitnings.features.purchase.controller;

import com.feis.splitnings.common.data.dto.response.SuccessResponse;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.common.specification.CommonSpecificationBuilder;
import com.feis.splitnings.common.specification.PageableUtil;
import com.feis.splitnings.features.purchase.data.Purchase;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseCreateDto;
import com.feis.splitnings.features.purchase.data.dto.request.PurchaseUpdateDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchaseDto;
import com.feis.splitnings.features.purchase.data.dto.response.PurchasePageDto;
import com.feis.splitnings.features.purchase.data.enums.Field;
import com.feis.splitnings.features.purchase.orchestrator.PurchaseOrchestrator;
import com.feis.splitnings.features.purchase.service.PurchaseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping(value = "/purchase")
@Tag(name = "Purchase", description = "Purchase CRUD operations")
public class PurchaseController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "Purchase";

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseOrchestrator purchaseOrchestrator;

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
    public ResponseEntity<SuccessResponse<PurchaseDto>> getPurchaseById(
            @PathVariable(name = ID_NAME) Integer id) {
        PurchaseDto dto = purchaseService.get(id);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<PurchaseDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was found", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Get all " + ITEM_NAME + "s")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + "s found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<PurchasePageDto>> getAllPurchases(
            @RequestParam(name = "ids", required = false) List<Integer> ids,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "splitId", required = false) Integer splitId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(name = "showTotalPageCount", required = false, defaultValue = "true") Boolean showTotalPageCount) {

        Pageable pageable = PageableUtil.buildPageable(page, size, sortBy, sortDirection);
        CommonSpecificationBuilder<Purchase> specificationBuilder = new CommonSpecificationBuilder<Purchase>()
                .in(Field.id.getPath(), ids, false)
                .like(Field.name.getPath(), name, false)
                .like(Field.description.getPath(), description, false)
                .whereEqualTo(Field.splitId.getPath(), splitId, false)
                .whereEqualTo(Field.deleted.getPath(), false, false);

        PurchasePageDto pageDto = purchaseService.getAll(specificationBuilder, pageable, showTotalPageCount);
        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<PurchasePageDto> response = new SuccessResponse<>(responseStatus.value(), pageDto.getTotalCount() + " " + ITEM_NAME + "s found", pageDto);

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
    public ResponseEntity<SuccessResponse<PurchaseDto>> createPurchase(
            @RequestBody(required = true) PurchaseCreateDto createDto) {

        PurchaseDto dto = purchaseOrchestrator.submitPurchase(createDto);

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<PurchaseDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " created successfully", dto);

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
    public ResponseEntity<SuccessResponse<PurchaseDto>> updatePurchaseById(
            @PathVariable(name = ID_NAME) Integer id,
            @RequestBody(required = true) PurchaseUpdateDto updateDto) {

        PurchaseDto dto = purchaseService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<PurchaseDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was updated", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }
}

