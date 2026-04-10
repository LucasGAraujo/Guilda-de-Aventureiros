package org.example.service;

import org.example.DTO.MissaoDTO;
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

    public Page<MissaoDTO.Response> listarMissoes(
            StatusMissao status,
            NivelPerigo nivelPerigo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable
    ) {
        return missaoRepository.listarMissoes(status, nivelPerigo, dataInicio, dataFim, pageable)
                .map(m -> new MissaoDTO.Response(
                        m.getId(),
                        m.getTitulo(),
                        m.getNivelPerigo(),
                        m.getStatus(),
                        m.getOrganizacao().getNome()
                ));
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
    public Missao iniciarMissao(Long id) {
        Missao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Missão não encontrada"));
        if (missao.getStatus() != StatusMissao.PLANEJADA) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Apenas missões planejadas podem ser iniciadas");
        }
        missao.setStatus(StatusMissao.EM_ANDAMENTO);
        return missaoRepository.save(missao);
    }
    public Missao cancelarMissao(Long id) {
        Missao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Missão não encontrada"));
        missao.setStatus(StatusMissao.CANCELADA);
        return missaoRepository.save(missao);
    }
    public Missao concluirMissao(Long id) {
        Missao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Missão não encontrada"));
        if (missao.getStatus() != StatusMissao.EM_ANDAMENTO) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Apenas missões Iniciadas podem ser concluidas");
        }
        missao.setStatus(StatusMissao.CONCLUIDA);
        return missaoRepository.save(missao);
    }
    public void deletar(Long id) {
        missaoRepository.deleteById(id);
    }

}