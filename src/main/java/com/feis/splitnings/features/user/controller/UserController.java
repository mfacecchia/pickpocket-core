package com.feis.splitnings.features.user.controller;

import com.feis.splitnings.common.data.dto.SuccessResponse;
import com.feis.splitnings.common.exception.model.ErrorResponse;
import com.feis.splitnings.features.user.data.dto.response.UserDto;
import com.feis.splitnings.features.user.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/user")
@Tag(name = "User", description = "User CRUD operations")
public class UserController {
    private static final String ID_NAME = "id";
    private static final String ITEM_NAME = "User";
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new " + ITEM_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ITEM_NAME + " was created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SuccessResponse.class)) }),
            @ApiResponse(responseCode = "409", description = ITEM_NAME + " is already registered", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = ITEM_NAME + " with provided external id does not exist", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<UserDto>> createUser() {
        UserDto dto = userService.createFromJwt();
        logger.info("New {} was created with {}:{}", ITEM_NAME, ID_NAME, dto.toString());

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<UserDto> response = new SuccessResponse<>(responseStatus.value(), "User created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }
}
