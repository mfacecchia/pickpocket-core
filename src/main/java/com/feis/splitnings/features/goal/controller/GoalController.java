package com.feis.splitnings.features.goal.controller;

import com.feis.splitnings.common.data.dto.response.SuccessResponse;
import com.feis.splitnings.common.exception.data.dto.response.ErrorResponse;
import com.feis.splitnings.common.specification.CommonSpecificationBuilder;
import com.feis.splitnings.common.specification.PageableUtil;
import com.feis.splitnings.features.goal.data.Goal;
import com.feis.splitnings.features.goal.data.dto.request.GoalCreateDto;
import com.feis.splitnings.features.goal.data.dto.request.GoalUpdateDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalDto;
import com.feis.splitnings.features.goal.data.dto.response.GoalPageDto;
import com.feis.splitnings.features.goal.data.enums.Field;
import com.feis.splitnings.features.goal.service.GoalService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/goal")
@Tag(name = "Goal", description = "Goal CRUD operations")
public class GoalController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "Goal";

    @Autowired
    private GoalService goalService;

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
    public ResponseEntity<SuccessResponse<GoalDto>> getGoalById(
            @PathVariable(name = ID_NAME) Integer id) {
        GoalDto dto = goalService.get(id);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<GoalDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was found", dto);

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
    public ResponseEntity<SuccessResponse<GoalPageDto>> getAllGoals(
            @RequestParam(name = "ids", required = false) List<Integer> ids,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "completed", required = false) Boolean completed,
            @RequestParam(name = "splitId", required = false) Integer splitId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(name = "showTotalPageCount", required = false, defaultValue = "true") Boolean showTotalPageCount) {

        Pageable pageable = PageableUtil.buildPageable(page, size, sortBy, sortDirection);
        CommonSpecificationBuilder<Goal> specificationBuilder = new CommonSpecificationBuilder<Goal>()
                .in(Field.id.name(), ids, false)
                .like(Field.name.name(), name, false)
                .whereEqualTo(Field.completed.name(), completed, false)
                .whereEqualTo(Field.splitId.name(), splitId, false)
                .whereEqualTo(Field.deleted.name(), false, false);

        GoalPageDto pageDto = goalService.getAll(specificationBuilder, pageable, showTotalPageCount);
        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<GoalPageDto> response = new SuccessResponse<>(responseStatus.value(), pageDto.getTotalCount() + " " + ITEM_NAME + "s found", pageDto);

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
    public ResponseEntity<SuccessResponse<GoalDto>> createGoal(
            @RequestBody(required = true) GoalCreateDto createDto) {

        GoalDto dto = goalService.create(createDto);

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<GoalDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Update a " + ITEM_NAME + " by its " + ID_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ITEM_NAME + " was updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " with name for the same split already exists", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PutMapping("/{" + ID_NAME + "}")
    public ResponseEntity<SuccessResponse<GoalDto>> updateGoalById(
            @PathVariable(name = ID_NAME) Integer id,
            @RequestBody(required = true) GoalUpdateDto updateDto) {
        GoalDto dto = goalService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<GoalDto> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was updated", dto);

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
    public ResponseEntity<SuccessResponse<?>> deleteGoalById(
            @PathVariable(name = ID_NAME) Integer id) {
        goalService.delete(id);

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        SuccessResponse<?> response = new SuccessResponse<>(responseStatus.value(), ITEM_NAME + " was deleted", null);

        return ResponseEntity.status(responseStatus).body(response);
    }
}
