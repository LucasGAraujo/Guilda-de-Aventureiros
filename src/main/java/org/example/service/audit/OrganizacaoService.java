package org.example.service.audit;

import lombok.RequiredArgsConstructor;
import org.example.DTO.audit.OrganizacaoDTO;
import org.example.domain.audit.Organizacao;

import org.example.exception.BusinessException;
import org.example.repository.audit.OrganizacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;

    public List<Organizacao> listarOrganizacoes() {
        return organizacaoRepository.findAll();
    }

    public Optional<Organizacao> buscarPorId(Long id) {
        return organizacaoRepository.findById(id);
    }
}