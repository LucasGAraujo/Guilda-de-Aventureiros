package org.example.service;

import org.example.DTO.CompanheiroDTO;
import org.example.domain.Aventureiro;
import org.example.domain.Companheiro;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.CompanheiroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CompanheiroService {

    private final CompanheiroRepository companheiroRepository;
    private final AventureiroRepository aventureiroRepository;

    public CompanheiroService(CompanheiroRepository companheiroRepository, AventureiroRepository aventureiroRepository) {
        this.companheiroRepository = companheiroRepository;
        this.aventureiroRepository = aventureiroRepository;
    }

    @Transactional
    public Companheiro salvar(Long aventureiroId, CompanheiroDTO.Request dto) {
        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aventureiro não encontrado"));

        Optional<Companheiro> existing = companheiroRepository.findByAventureiroId(aventureiroId);
        if (existing.isPresent()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Aventureiro já possui um companheiro");
        }

        Companheiro companheiro = new Companheiro();
        companheiro.setNome(dto.nome());
        companheiro.setEspecie(dto.especie());
        companheiro.setLealdade(dto.lealdade());
        companheiro.setAventureiro(aventureiro);

        return companheiroRepository.save(companheiro);
    }

    @Transactional
    public Companheiro atualizarCompanheiro(Long companheiroId, CompanheiroDTO.Request dto) {
        Companheiro companheiro = companheiroRepository.findById(companheiroId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Companheiro não encontrado"));
        companheiro.setNome(dto.nome());
        companheiro.setEspecie(dto.especie());
        companheiro.setLealdade(dto.lealdade());

        return companheiroRepository.save(companheiro);
    }

    @Transactional
    public void removerCompanheiro(Long companheiroId) {
        Companheiro companheiro = companheiroRepository.findById(companheiroId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Companheiro não encontrado"));
        companheiroRepository.delete(companheiro);
    }
}