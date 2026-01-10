package com.feis.splitnings.features.user.controller;

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
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class)) })
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser() {
        UserDto dto = userService.createFromJwt();
        logger.info("New {} was created with {}:{}", ITEM_NAME, ID_NAME, dto.getId().toString());
        // TODO: Change respose type to custom DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
