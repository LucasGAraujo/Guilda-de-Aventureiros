package org.example.service;

import org.example.DTO.AventureiroDTO;
import org.example.domain.Aventureiro;
import org.example.domain.ENUM.ClasseAventureiro;
import org.example.domain.audit.Organizacao;
import org.example.domain.audit.Usuario;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.audit.OrganizacaoRepository;
import org.example.repository.audit.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AventureiroService {

    private final AventureiroRepository aventureiroRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public AventureiroService(AventureiroRepository aventureiroRepository, OrganizacaoRepository organizacaoRepository, UsuarioRepository usuarioRepository) {
        this.aventureiroRepository = aventureiroRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Aventureiro> buscarComFiltros(Boolean status, ClasseAventureiro classe, Integer nivelMinimo, Pageable pageable) {
        return aventureiroRepository.buscarAventureirosComFiltros(status, classe, nivelMinimo, pageable);
    }

    public Page<Aventureiro> buscarPorNome(String nome, Pageable pageable) {
        return aventureiroRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Optional<Aventureiro> buscarPerfilCompleto(Long id) {
        return aventureiroRepository.buscarPerfilCompleto(id);
    }

    public Aventureiro salvar(AventureiroDTO.Create dto) {
        Organizacao org = organizacaoRepository.findById(dto.organizacaoId())
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Organização não encontrada"
                ));

        Usuario usuario = usuarioRepository.findById(dto.usuarioCadastroId())
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));

        if (!usuario.getOrganizacao().getId().equals(org.getId())) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Usuário não pertence a essa organização, tente outra organização"
            );
        }
        Aventureiro aventureiro = new Aventureiro();
        aventureiro.setNome(dto.nome());
        aventureiro.setClasse(dto.classe());
        aventureiro.setNivel(dto.nivel());
        aventureiro.setOrganizacao(org);
        aventureiro.setUsuarioCadastro(usuario);
        aventureiro.setAtivo(true);

        return aventureiroRepository.save(aventureiro);
    }

    public void deletar(Long id) {
        if (!aventureiroRepository.existsById(id)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Aventureiro não encontrado");
        }
        aventureiroRepository.deleteById(id);
    }

}