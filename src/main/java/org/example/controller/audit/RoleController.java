package org.example.controller.audit;

import org.example.domain.audit.Role;
import org.example.DTO.audit.RoleDTO;
import org.example.service.audit.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/organizacao/{id}")
    public ResponseEntity<List<RoleDTO.Response>> listarRolePorOrganizacao(@PathVariable Long id) {
        List<RoleDTO.Response> lista = roleService.findAllByOrganizacaoId(id).stream()
                .map(role -> new RoleDTO.Response(
                                role.getId(),
                                role.getNome(),
                                role.getDescricao(),
                                role.getOrganizacao().getId(),
                                role.getOrganizacao().getNome()
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO.Response> listarRole(@PathVariable Long id) {
        return roleService.findById(id)
                .map(role -> ResponseEntity.ok(
                        new RoleDTO.Response(
                                role.getId(),
                                role.getNome(),
                                role.getDescricao(),
                                role.getOrganizacao().getId(),
                                role.getOrganizacao().getNome()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    }
