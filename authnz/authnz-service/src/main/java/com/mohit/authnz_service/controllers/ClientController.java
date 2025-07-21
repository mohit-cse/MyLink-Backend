package com.mohit.authnz_service.controllers;

import com.mohit.authnz_service.dto.profile.RegisterClientRequest;
import com.mohit.authnz_service.dto.profile.RegisterClientResponse;
import com.mohit.models.authentication.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Client Management", description = "APIs for managing clients")
public class ClientController {

    @PostMapping("/register")
    @Operation(summary = "Register client", description = "API to register a new client")
    public ResponseEntity<RegisterClientResponse> registerClient(@RequestBody RegisterClientRequest client) {
        // Logic to register a client
        // For now, just return a dummy response
        RegisterClientResponse response = RegisterClientResponse.builder()
                .clientId(client.getClientId())
                .roles(client.getRoles())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/grant/{clientId}/roles")
    @Operation(summary = "Grant roles", description = "API to grant roles to a client")
    public ResponseEntity<List<Role>> grantRolesToClient(@PathVariable String clientId, @RequestBody List<Role> roles) {
        // Logic to grant roles to a client
        // For now, just return the roles that were granted
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/revoke/{clientId}/roles")
    @Operation(summary = "Revoke roles", description = "API to revoke roles from a client")
    public ResponseEntity<List<Role>> revokeRolesFromClient(@RequestParam String clientId, @RequestBody List<Role> roles) {
        // Logic to revoke roles from a client
        // For now, just return the roles that were revoked
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete client", description = "API to delete a client")
    public ResponseEntity<String> deleteClient(@PathVariable String clientId, @RequestBody String password) {
        // Logic to delete a client
        // For now, just return a success message
        return ResponseEntity.ok("Client " + clientId + " deleted successfully.");
    }
}
