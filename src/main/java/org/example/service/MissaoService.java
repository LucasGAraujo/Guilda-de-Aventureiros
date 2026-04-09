package org.example.service;

import org.example.DTO.MissaoDTO;
import org.example.DTO.MissaoMetricasDTO;
import org.example.domain.ENUM.NivelPerigo;
import org.example.domain.ENUM.StatusMissao;
import org.example.domain.Missao;
import org.example.domain.audit.Organizacao;
import org.example.exception.BusinessException;
import org.example.repository.MissaoRepository;
import org.example.repository.audit.OrganizacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    public MissaoService(MissaoRepository missaoRepository,OrganizacaoRepository organizacaoRepository) {
        this.missaoRepository = missaoRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    public Page<Missao> listarMissoes(StatusMissao status, NivelPerigo nivelPerigo,
                                      LocalDateTime dataInicio, LocalDateTime dataFim,
                                      Pageable pageable) {
        return missaoRepository.listarMissoes(status, nivelPerigo, dataInicio, dataFim, pageable);
    }

    public Optional<Missao> buscarMissaoComParticipantes(Long id) {
        return missaoRepository.buscarMissaoComParticipantes(id);
    }

    public Missao salvar(MissaoDTO.Create dto) {
        Organizacao org = organizacaoRepository.findById(dto.organizacaoId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Organização não encontrada"));
        Missao missao = new Missao();
        missao.setTitulo(dto.titulo());
        missao.setNivelPerigo(dto.nivelPerigo());
        missao.setOrganizacao(org);
        missao.setDataInicio(dto.dataInicio());
        missao.setDataFim(dto.dataFim());
        missao.setStatus(StatusMissao.PLANEJADA);
        return missaoRepository.save(missao);
    }

    public void deletar(Long id) {
        missaoRepository.deleteById(id);
    }

}