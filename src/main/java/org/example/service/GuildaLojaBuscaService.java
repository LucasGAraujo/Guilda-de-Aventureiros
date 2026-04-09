package org.example.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.example.domain.ProdutoLojaDocument;
import org.example.DTO.AgregacaoCategoriaDTO;
import org.example.DTO.ProdutoLojaResponseDTO;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GuildaLojaBuscaService {
    private final ElasticsearchOperations elasticsearchOperations;
    public List<ProdutoLojaResponseDTO> buscarProdutosPorTermo(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(mm -> mm
                        .fields("nome", "descricao", "categoria", "raridade")
                        .query(termo)
                ))
                .build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarPorNome(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo))).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarPorDescricao(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("descricao").query(termo))).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarPorFraseExata(String frase) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrase(m -> m.field("descricao").query(frase))).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarFuzzy(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo).fuzziness("AUTO"))).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarComFiltro(String termo, String categoria) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.multiMatch(mm -> mm
                                .fields("nome", "descricao")
                                .query(termo)
                        ))
                        .filter(f -> f.match(mt -> mt
                                .field("categoria")
                                .query(categoria)
                        ))
                ))
                .build();

        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscarPorFaixaDePreco(Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r.field("preco")
                        .gte(JsonData.of(min))
                        .lte(JsonData.of(max))
                )).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<ProdutoLojaResponseDTO> buscaAvancada(String categoria, String raridade, Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .filter(f -> f.term(t -> t.field("categoria").value(categoria)))
                        .filter(f -> f.term(t -> t.field("raridade").value(raridade)))
                        .filter(f -> f.range(r -> r.field("preco").gte(JsonData.of(min)).lte(JsonData.of(max))))
                )).build();
        return converterParaDTO(elasticsearchOperations.search(query, ProdutoLojaDocument.class));
    }
    public List<AgregacaoCategoriaDTO> agruparPorRaridade() {
        NativeQuery query = NativeQuery.builder()
                .withAggregation("contagem_raridade", Aggregation.of(a -> a.terms(t -> t.field("raridade"))))
                .build();

        var hits = elasticsearchOperations.search(query, ProdutoLojaDocument.class);
        var aggregate = ((ElasticsearchAggregations) hits.getAggregations())
                .aggregationsAsMap().get("contagem_raridade").aggregation().getAggregate();

        return aggregate.sterms().buckets().array().stream()
                .map(b -> new AgregacaoCategoriaDTO(b.key().stringValue(), b.docCount()))
                .toList();
    }
    public List<AgregacaoCategoriaDTO> agruparPorCategoria() {
        var query = NativeQuery.builder()
                .withAggregation("contagem_por_categoria", Aggregation.of(a -> a.terms(t -> t.field("categoria"))))
                .build();
        var hits = elasticsearchOperations.search(query, ProdutoLojaDocument.class);
        var aggregate = ((ElasticsearchAggregations) hits.getAggregations())
                .aggregationsAsMap().get("contagem_por_categoria").aggregation().getAggregate();
        return aggregate.sterms().buckets().array().stream()
                .map(b -> new AgregacaoCategoriaDTO(b.key().stringValue(), b.docCount()))
                .toList();
    }
    public Map<String, Long> agruparFaixasPreco() {
        var query = NativeQuery.builder()
                .withAggregation("faixas", Aggregation.of(a -> a.range(r -> r.field("preco")
                        .ranges(rg -> rg.to(JsonData.of(100).toString()))
                        .ranges(rg -> rg.from(JsonData.of(100).toString()).to(JsonData.of(300).toString()))
                        .ranges(rg -> rg.from(JsonData.of(300).toString()).to(JsonData.of(700).toString()))
                        .ranges(rg -> rg.from(JsonData.of(700).toString()))
                ))).build();

        var hits = elasticsearchOperations.search(query, ProdutoLojaDocument.class);

        var aggregate = ((ElasticsearchAggregations) hits.getAggregations())
                .aggregationsAsMap().get("faixas").aggregation().getAggregate();

        Map<String, Long> faixas = new HashMap<>();
        aggregate.range().buckets().array().forEach(b -> faixas.put(b.key(), b.docCount()));

        return faixas;
    }
    private List<ProdutoLojaResponseDTO> converterParaDTO(SearchHits<ProdutoLojaDocument> hits) {
        return hits.getSearchHits().stream()
                .map(hit -> {
                    ProdutoLojaDocument doc = hit.getContent();
                    return new ProdutoLojaResponseDTO(
                            doc.getId(),
                            doc.getNome(),
                            doc.getCategoria(),
                            doc.getRaridade(),
                            doc.getPreco(),
                            doc.getDescricao()
                    );
                }).toList();
    }
}