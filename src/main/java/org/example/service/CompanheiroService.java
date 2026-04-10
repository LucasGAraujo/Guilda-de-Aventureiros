package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.DTO.CompanheiroDTO;
import org.example.domain.Aventureiro;
import org.example.domain.Companheiro;
import org.example.exception.BusinessException;
import org.example.repository.AventureiroRepository;
import org.example.repository.CompanheiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanheiroService {

    private final CompanheiroRepository companheiroRepository;
    private final AventureiroRepository aventureiroRepository;



    public Companheiro salvar(Long aventureiroId, CompanheiroDTO.Request dto) {
        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId).orElseThrow(() -> new BusinessException( "Aventureiro não encontrado"));

        Optional<Companheiro> existing = companheiroRepository.findByAventureiroId(aventureiroId);
        if (existing.isPresent()) {throw new BusinessException( "Aventureiro já possui um companheiro");
        }

        Companheiro companheiro = new Companheiro();
        companheiro.setNome(dto.nome());
        companheiro.setEspecie(dto.especie());
        companheiro.setLealdade(dto.lealdade());
        companheiro.setAventureiro(aventureiro);

        return companheiroRepository.save(companheiro);
    }

    public Companheiro atualizar(Long companheiroId, CompanheiroDTO.Request dto) {
        Companheiro companheiro = companheiroRepository.findById(companheiroId).orElseThrow(() -> new BusinessException( "Companheiro não encontrado"));
        companheiro.setNome(dto.nome());
        companheiro.setEspecie(dto.especie());
        companheiro.setLealdade(dto.lealdade());

        return companheiroRepository.save(companheiro);
    }

    public void deletar(Long companheiroId) {
        Companheiro companheiro = companheiroRepository.findById(companheiroId).orElseThrow(() -> new BusinessException( "Companheiro não encontrado"));
        companheiroRepository.delete(companheiro);
    }
}