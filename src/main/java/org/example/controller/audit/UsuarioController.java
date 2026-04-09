package org.example.controller.audit;

import org.example.domain.audit.Usuario;
import org.example.DTO.audit.UsuarioDTO;
import org.example.service.audit.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/organizacao/{id}")
    public ResponseEntity<List<UsuarioDTO.Response>> listarUsuarioPorOrganizacao(@PathVariable Long id) {
        List<UsuarioDTO.Response> lista = usuarioService.findAllByOrganizacaoId(id).stream()
                .map(usuario -> new UsuarioDTO.Response(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getStatus(),
                        usuario.getOrganizacao().getNome(),
                        usuario.getRoles().stream()
                                .map(role -> role.getNome())
                                .collect(Collectors.toSet()),
                        usuario.getCreatedAt()
                ))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO.Response> listar(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(
                        new UsuarioDTO.Response(
                                usuario.getId(),
                                usuario.getNome(),
                                usuario.getEmail(),
                                usuario.getStatus(),
                                usuario.getOrganizacao().getNome(),
                                usuario.getRoles().stream()
                                        .map(role -> role.getNome())
                                        .collect(Collectors.toSet()),
                                usuario.getCreatedAt()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO.Response> criar(@RequestBody UsuarioDTO.Create dto) {
        Usuario criada = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UsuarioDTO.Response(
                        criada.getId(),
                        criada.getNome(),
                        criada.getEmail(),
                        criada.getStatus(),
                        criada.getOrganizacao().getNome(),
                        criada.getRoles().stream()
                                .map(role -> role.getNome())
                                .collect(Collectors.toSet()),
                        criada.getCreatedAt()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}