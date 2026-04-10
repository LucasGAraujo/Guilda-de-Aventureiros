package org.example.controller.audit;

import lombok.RequiredArgsConstructor;
import org.example.domain.audit.Usuario;
import org.example.DTO.audit.UsuarioDTO;
import org.example.service.audit.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;


    @GetMapping("/organizacao/{id}")
    public ResponseEntity<List<UsuarioDTO.Response>> listarUsuarioPorOrganizacao(@PathVariable Long id) {
        List<UsuarioDTO.Response> lista = usuarioService.buscarTodasOrgPorId(id).stream()
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
        return usuarioService.buscarPorId(id)
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

    @GetMapping("/buscar")
    public ResponseEntity<Page<UsuarioDTO.Response>> buscarPorNome(
            @RequestParam String nome, @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<UsuarioDTO.Response> page = usuarioService.buscarPorNome(nome, pageable)
                .map(a -> new UsuarioDTO.Response(
                        a.getId(),
                        a.getNome(),
                        a.getEmail(),
                        a.getStatus(),
                        a.getOrganizacao().getNome(),
                        a.getRoles().stream()
                                .map(role -> role.getNome())
                                .collect(Collectors.toSet()),
                        a.getCreatedAt()
                ));
        return ResponseEntity.ok(page);
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
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}