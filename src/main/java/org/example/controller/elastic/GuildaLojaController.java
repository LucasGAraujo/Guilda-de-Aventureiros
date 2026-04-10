package org.example.controller.elastic;

import lombok.RequiredArgsConstructor;
import org.example.DTO.AgregacaoCategoriaDTO;
import org.example.DTO.ProdutoLojaResponseDTO;
import org.example.service.GuildaLojaBuscaService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class GuildaLojaController {
    private final GuildaLojaBuscaService guildaservice;

    @GetMapping("/busca")
    public List<ProdutoLojaResponseDTO> buscar(@RequestParam String termo) throws IOException {
        return guildaservice.buscarProdutosPorTermo(termo);
    }

    @GetMapping("/busca/nome")
    public List<ProdutoLojaResponseDTO> buscarNome(@RequestParam String termo) throws IOException {
        return guildaservice.buscarPorNome(termo);
    }

    @GetMapping("/busca/descricao")
    public List<ProdutoLojaResponseDTO> buscarDescricao(@RequestParam String termo) throws IOException {
        return guildaservice.buscarPorDescricao(termo);
    }

    @GetMapping("/busca/frase")
    public List<ProdutoLojaResponseDTO> buscarFraseExata(@RequestParam String termo) throws IOException {
        return guildaservice.buscarPorFraseExata(termo);
    }

    @GetMapping("/busca/fuzzy")
    public List<ProdutoLojaResponseDTO> buscarFuzzy(@RequestParam String termo) throws IOException {
        return guildaservice.buscarFuzzy(termo);
    }

    @GetMapping("/busca/multicampos")
    public List<ProdutoLojaResponseDTO> buscarMultiCampos(@RequestParam String termo) throws IOException {
        return guildaservice.buscarProdutosPorTermo(termo);
    }

    @GetMapping("/busca/com-filtro")
    public List<ProdutoLojaResponseDTO> buscarComFiltro(@RequestParam String termo, @RequestParam String categoria) throws IOException {
        return guildaservice.buscarComFiltro(termo, categoria);
    }

    @GetMapping("/busca/faixa-preco")
    public List<ProdutoLojaResponseDTO> buscarPorFaixaDePreco(@RequestParam Double min, @RequestParam Double max) throws IOException {
        return guildaservice.buscarPorFaixaDePreco(min, max);
    }

    @GetMapping("/busca/avancada")
    public List<ProdutoLojaResponseDTO> buscaAvancada(@RequestParam String categoria, @RequestParam String raridade, @RequestParam Double min, @RequestParam Double max) throws IOException {
        return guildaservice.buscaAvancada(categoria, raridade, min, max);
    }

    @GetMapping("/agregacoes/por-categoria")
    public List<AgregacaoCategoriaDTO> agruparPorCategoria() throws IOException {
        return guildaservice.agruparPorCategoria();
    }

    @GetMapping("/agregacoes/por-raridade")
    public List<AgregacaoCategoriaDTO> agruparPorRaridade() throws IOException {
        return guildaservice.agruparPorRaridade();
    }

    @GetMapping("/agregacoes/faixas-preco")
    public Map<String, Long> agruparFaixasPreco() throws IOException {
        return guildaservice.agruparFaixasPreco();
    }

    @GetMapping("/agregacao/categorias")
    public List<AgregacaoCategoriaDTO> contarPorCategoria() throws IOException {
        return guildaservice.agruparPorCategoria();
    }
}