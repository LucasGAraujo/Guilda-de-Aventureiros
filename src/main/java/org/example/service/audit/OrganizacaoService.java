package org.example.service.audit;

import org.example.DTO.audit.OrganizacaoDTO;
import org.example.domain.audit.Organizacao;

import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository) {
        this.organizacaoRepository = organizacaoRepository;
    }

    public Organizacao criarOrganizacao(OrganizacaoDTO.Create dto) {
        Organizacao organizacao = new Organizacao();
        organizacao.setNome(dto.nome());
        organizacao.setAtivo(true);
        return organizacaoRepository.save(organizacao);
    }

    public List<Organizacao> listarOrganizacoes() {
        return organizacaoRepository.findAll();
    }

    public Optional<Organizacao> buscarPorId(Long id) {
        return organizacaoRepository.findById(id);
    }

    public Organizacao atualizar(Long id, OrganizacaoDTO.Update dto) {
        Organizacao organizacao = organizacaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Organização não encontrada"));
        if (dto.nome() != null) organizacao.setNome(dto.nome());
        if (dto.ativo() != null) organizacao.setAtivo(dto.ativo());

        return organizacaoRepository.save(organizacao);
    }

    public void deletar(Long id) {
        if (!organizacaoRepository.existsById(id)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Organização não encontrada");
        }
        organizacaoRepository.deleteById(id);
    }
}