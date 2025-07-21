package com.mohit.authnz_service.controllers;

import com.mohit.authnz_service.dto.role.RoleDTO;
import com.mohit.authnz_service.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "APIs for managing roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create role", description = "API to create a role")
    public ResponseEntity<String> createRole(@RequestBody RoleDTO roleDTO) {
        // Logic to create a role
        // For now, just return a success message
        return ResponseEntity.ok("Role created successfully: " + roleDTO.getRoleName());
    }

    @DeleteMapping("/{roleName}")
    @Operation(summary = "Delete role", description = "API to delete a role")
    public ResponseEntity<String> deleteRole(@PathVariable String roleName) {
        // Logic to delete a role
        // For now, just return a success message
        return ResponseEntity.ok("Role deleted successfully: " + roleName);
    }

    @GetMapping("/{roleName}")
    @Operation(summary = "Get role", description = "API to get a role")
    public ResponseEntity<RoleDTO> getRole(@PathVariable String roleName) {
        // Logic to retrieve a role
        // For now, just return a dummy role
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(roleName);
        roleDTO.setDescription("Dummy description for " + roleName);
        return ResponseEntity.ok(roleDTO);
    }
}
