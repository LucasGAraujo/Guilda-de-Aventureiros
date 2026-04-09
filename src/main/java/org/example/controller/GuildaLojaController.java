package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.DTO.AgregacaoCategoriaDTO;
import org.example.DTO.ProdutoLojaResponseDTO;
import org.example.service.GuildaLojaBuscaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class GuildaLojaController {

    private final GuildaLojaBuscaService service;

    @GetMapping("/busca")
    public List<ProdutoLojaResponseDTO> buscar(@RequestParam String termo) {
        return service.buscarProdutosPorTermo(termo);
    }
    @GetMapping("/busca/nome")
    public List<ProdutoLojaResponseDTO> buscarNome(@RequestParam String termo) {
        return service.buscarPorNome(termo);
    }
    @GetMapping("/busca/descricao")
    public List<ProdutoLojaResponseDTO> buscarDescricao(@RequestParam String termo) {
        return service.buscarPorDescricao(termo);
    }
    @GetMapping("/busca/frase")
    public List<ProdutoLojaResponseDTO> buscarFraseExata(@RequestParam String termo) {
        return service.buscarPorFraseExata(termo);
    }
    @GetMapping("/busca/fuzzy")
    public List<ProdutoLojaResponseDTO> buscarFuzzy(@RequestParam String termo) {
        return service.buscarFuzzy(termo);
    }
    @GetMapping("/busca/multicampos")
    public List<ProdutoLojaResponseDTO> buscarMultiCampos(@RequestParam String termo) {
        return service.buscarProdutosPorTermo(termo);
    }
    @GetMapping("/busca/com-filtro")
    public List<ProdutoLojaResponseDTO> buscarComFiltro(@RequestParam String termo, @RequestParam String categoria) {
        return service.buscarComFiltro(termo, categoria);
    }
    @GetMapping("/busca/faixa-preco")
    public List<ProdutoLojaResponseDTO> buscarPorFaixaDePreco(@RequestParam Double min, @RequestParam Double max) {
        return service.buscarPorFaixaDePreco(min, max);
    }
    @GetMapping("/busca/avancada")
    public List<ProdutoLojaResponseDTO> buscaAvancada(@RequestParam String categoria, @RequestParam String raridade, @RequestParam Double min, @RequestParam Double max) {
        return service.buscaAvancada(categoria, raridade, min, max);
    }
    @GetMapping("/agregacoes/por-categoria")
    public List<AgregacaoCategoriaDTO> agruparPorCategoria() {return service.agruparPorCategoria();
    }
    @GetMapping("/agregacoes/por-raridade")
    public List<AgregacaoCategoriaDTO> agruparPorRaridade() {
        return service.agruparPorRaridade();
    }
    @GetMapping("/agregacoes/faixas-preco")
    public Map<String, Long> agruparFaixasPreco() {
        return service.agruparFaixasPreco();
    }
    @GetMapping("/agregacao/categorias")
    public List<AgregacaoCategoriaDTO> contarPorCategoria() {
        return service.agruparPorCategoria();
    }
}