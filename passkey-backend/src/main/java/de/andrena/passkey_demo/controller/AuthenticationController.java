package de.andrena.passkey_demo.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.AssertionResult;

import de.andrena.passkey_demo.exception.AuthenticationException;
import de.andrena.passkey_demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/authenticate")
    @Operation(summary = "Start authentication.", parameters = {
        @Parameter(name = "username", in = ParameterIn.QUERY, description = "The username", required = true, schema = @Schema(type = "string")) })
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public String startAuthentication(@RequestParam(value = "username") String username) {
        try {
            return authenticationService.authenticate(username);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting authentication challenge to json", e);
        }
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Finish authentication")
    @ApiResponse(responseCode = "201", description = "Successfully authenticated with passkey")
    @ApiResponse(responseCode = "401", description = "Error validating passkey")
    public AssertionResult finishAuthentication(@RequestBody String authCredentials) {
        try {
            return authenticationService.finishAuthentication(authCredentials);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse credentials in request body", e);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }
}
