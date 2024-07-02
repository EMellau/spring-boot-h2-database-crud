package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.config.ApplicationConfig;
import com.mellau.spring.jpa.h2.domain.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user") // OpenAPI
@RestController
@RequestMapping(ApplicationConfig.USER_URL)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDTOMapper mapper;

    /**
     * Fetches user by criteria and returns them.
     *
     * @param userSearchRequestDTO user search parameters
     * @return a JSON list representation of user
     */
    @Operation(description = "Return list of user by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was fulfilled successfully."),
    })
    @PostMapping("/search")
    public List<UserDTO> searchUser(@RequestBody final UserSearchRequestDTO userSearchRequestDTO) {
        return mapper.toUserDTOs(userService.searchUser(mapper.toUserSearchRequest(userSearchRequestDTO)));
    }

    /**
     * Fetches user by gold and returns them.
     *
     * @return a JSON representation of a user
     */
    @Operation(description = "Return list of user by gold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was fulfilled successfully."),
    })
    @GetMapping("/ranking")
    public List<UserDTO> getRanking() {
        return mapper.toUserDTOs(userService.getRanking());
    }


    /**
     * Fetches user Id.
     *
     * @param userId id to search by
     * @return a JSON representation of an user
     */
    @Operation(description = "Return an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was fulfilled successfully."),
    })
    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable final Long userId) {
        return mapper.toUserDTO(userService.getUserById(userId));
    }

    /**
     * Creates user.
     *
     * @param userCreateRequestDTO user create parameters
     * @return a JSON list representation of user
     */
    @Operation(description = "Return list of user by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was fulfilled successfully."),
    })
    @PostMapping()
    public UserDTO createUser(@RequestBody final UserCreateRequestDTO userCreateRequestDTO) {
        return mapper.toUserDTO(userService.createUser(mapper.toUserCreateRequest(userCreateRequestDTO)));
    }

    /**
     * Update a user
     *
     * @param userId               the id of the user to update
     * @param userUpdateRequestDTO user update request
     * @return a JSON representation of the updated user
     */
    @Operation(description = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was fulfilled successfully."),
    })
    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable final Long userId,
                              @RequestBody(required = false) final UserUpdateRequestDTO userUpdateRequestDTO) {
        return mapper.toUserDTO(userService.updateUser(userId, mapper.toUserUpdateRequest(userUpdateRequestDTO)));
    }

    /**
     * Delete an user by Id
     *
     * @param userId userId to delete
     */
    @Operation(description = "Delete a user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request was fulfilled successfully."),
    })
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable final Long userId) {
        userService.deleteById(userId);
    }
}
