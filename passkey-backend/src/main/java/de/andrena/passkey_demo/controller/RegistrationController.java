package de.andrena.passkey_demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import de.andrena.passkey_demo.model.entities.UserRegistration;
import de.andrena.passkey_demo.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * @author Christoph Eifert (christoph.eifert@brueckner.com)
 */
@RestController
@Valid
@CrossOrigin
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    @Operation(summary = "Start register.", parameters = {
        @Parameter(name = "username", in = ParameterIn.QUERY, description = "The username", required = true, schema = @Schema(type = "string")) })
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public String startRegistration(@RequestParam(value = "username") @NotBlank String username) {
        try {
            return registrationService.startRegistration(username);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error converting credential options to json", e);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Finish register")
    @ApiResponse(responseCode = "201", description = "Successfully registered passkey")
    @ApiResponse(responseCode = "401", description = "Error validating passkey")
    public UserRegistration finishRegistration(@RequestBody String credentials) {
        try {
            return registrationService.finishRegistration(credentials);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse credentials in request body", e);
        } catch (RegistrationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Registration failed");
        }
    }
}
