package org.example.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import org.example.domain.ProdutoLojaDocument;
import org.example.DTO.AgregacaoCategoriaDTO;
import org.example.DTO.ProdutoLojaResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuildaLojaBuscaService {

    private final ElasticsearchClient client;
    private final String INDEX_NAME = "guilda_loja";

    public GuildaLojaBuscaService(ElasticsearchClient client) {
        this.client = client;
    }

    public List<ProdutoLojaResponseDTO> buscarProdutosPorTermo(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                        .multiMatch(mm -> mm
                                .fields("nome", "descricao", "categoria", "raridade")
                                .query(termo)
                        )
                ), ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarPorNome(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.match(m -> m.field("nome").query(termo))),
                ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarPorDescricao(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.match(m -> m.field("descricao").query(termo))),
                ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarPorFraseExata(String frase) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.matchPhrase(m -> m.field("descricao").query(frase))),
                ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarFuzzy(String termo) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.match(m -> m.field("nome").query(termo).fuzziness("AUTO"))),
                ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarComFiltro(String termo, String categoria) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q.bool(b -> b
                        .must(m -> m.multiMatch(mm -> mm
                                .fields("nome", "descricao")
                                .query(termo)
                        ))
                        .filter(f -> f.match(mt -> mt
                                .field("categoria")
                                .query(categoria)
                        ))
                )), ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscarPorFaixaDePreco(Double min, Double max) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q.range(r -> r
                        .field("preco")
                        .gte(JsonData.of(min))
                        .lte(JsonData.of(max))
                )), ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<ProdutoLojaResponseDTO> buscaAvancada(String categoria, String raridade, Double min, Double max) throws IOException {
        SearchResponse<ProdutoLojaDocument> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q.bool(b -> b
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))
                        .filter(f -> f.term(t -> t.field("raridade").value(raridade)))
                        .filter(f -> f.range(r -> r.field("preco").gte(JsonData.of(min)).lte(JsonData.of(max))))
                )), ProdutoLojaDocument.class
        );
        return converterparaDTO(response);
    }

    public List<AgregacaoCategoriaDTO> agruparPorRaridade() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(INDEX_NAME)
                .size(0)
                .aggregations("contagem_raridade", a -> a
                        .terms(t -> t.field("raridade"))
                ), Void.class
        );

        return response.aggregations()
                .get("contagem_raridade")
                .sterms()
                .buckets()
                .array()
                .stream()
                .map(b -> new AgregacaoCategoriaDTO(b.key().stringValue(), b.docCount()))
                .toList();
    }

    public List<AgregacaoCategoriaDTO> agruparPorCategoria() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(INDEX_NAME)
                .size(0)
                .aggregations("contagem_por_categoria", a -> a
                        .terms(t -> t.field("categoria"))
                ), Void.class
        );

        return response.aggregations()
                .get("contagem_por_categoria")
                .sterms()
                .buckets()
                .array()
                .stream()
                .map(b -> new AgregacaoCategoriaDTO(b.key().stringValue(), b.docCount()))
                .toList();
    }

    public Map<String, Long> agruparFaixasPreco() throws IOException {
        SearchResponse<Void> response = client.search(s -> s
                .index(INDEX_NAME)
                .size(0)
                .aggregations("faixas", a -> a
                        .range(r -> r.field("preco")
                                .ranges(rg -> rg.to("100.0"))
                                .ranges(rg -> rg.from("100.0").to("300.0"))
                                .ranges(rg -> rg.from("300.0").to("700.0"))
                                .ranges(rg -> rg.from("700.0"))
                        )
                ), Void.class
        );

        Map<String, Long> faixas = new HashMap<>();
        response.aggregations()
                .get("faixas")
                .range()
                .buckets()
                .array()
                .forEach(b -> faixas.put(b.key(), b.docCount()));

        return faixas;
    }

    private List<ProdutoLojaResponseDTO> converterparaDTO(SearchResponse<ProdutoLojaDocument> response) {
        return response.hits().hits().stream()
                .map(hit -> {
                    ProdutoLojaDocument doc = hit.source();
                     return new ProdutoLojaResponseDTO(
                            hit.id(),
                            doc.getNome(),
                            doc.getCategoria(),
                            doc.getRaridade(),
                            doc.getPreco(),
                            doc.getDescricao()
                    );
                })
                .toList();
    }
}