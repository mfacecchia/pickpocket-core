package com.feis.splitnings.features.split.controller;

import com.feis.splitnings.common.data.dto.response.SuccessResponse;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.features.split.data.dto.request.SplitCreateDto;
import com.feis.splitnings.features.split.data.dto.request.SplitUpdateDto;
import com.feis.splitnings.features.split.data.dto.response.SplitDto;
import com.feis.splitnings.features.split.service.SplitService;

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
@RequestMapping(value = "/split")
@Tag(name = "Split", description = "Split CRUD operations")
public class SplitController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "Split";
    private static final Logger logger = LogManager.getLogger(SplitController.class);

    @Autowired
    private SplitService splitService;

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
    public ResponseEntity<SuccessResponse<SplitDto>> getSplitById(
            @PathVariable(name = ID_NAME) Integer id) {
        SplitDto dto = splitService.get(id);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<SplitDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was found", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Create a new " + ITEM_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ITEM_NAME + " was created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " with name for the same account already exists", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<SplitDto>> createSplit(
            @RequestBody(required = true) SplitCreateDto createDto) {
        SplitDto dto = splitService.create(createDto);
        logger.info("New {} was created with {}:{}", ITEM_NAME, ID_NAME, dto.toString());

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<SplitDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Update a " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " with name for the same account already exists", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<SplitDto>> updateSplitById(
            @PathVariable(name = ID_NAME) Integer id,
            @RequestBody(required = true) SplitUpdateDto updateDto) {
        SplitDto dto = splitService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<SplitDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was updated", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Delete a " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ITEM_NAME + " was deleted", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<?>> deleteSplitById(
            @PathVariable(name = ID_NAME) Integer id) {
        splitService.delete(id);

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        SuccessResponse<?> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was deleted", null);

        return ResponseEntity.status(responseStatus).body(response);
    }
}
