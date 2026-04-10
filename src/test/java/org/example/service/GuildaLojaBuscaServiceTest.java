package org.example.service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import org.example.DTO.ProdutoLojaResponseDTO;
import org.example.domain.ProdutoLojaDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuildaLojaBuscaServiceTest {

    @Mock
    private ElasticsearchClient client;

    @InjectMocks
    private GuildaLojaBuscaService service;

    @Test
    @DisplayName("Deve buscar produto por nome e converter para DTO corretamente")
    void buscarPorNome() throws IOException {
        ProdutoLojaDocument docFake = new ProdutoLojaDocument();
        docFake.setNome("Espada Flamejante");
        docFake.setPreco(500.0);
        docFake.setCategoria("Armas");
        SearchResponse<ProdutoLojaDocument> responseFake = mockSearchResponse(docFake, "123");
        when(client.search(any(Function.class), eq(ProdutoLojaDocument.class))).thenReturn(responseFake);
        List<ProdutoLojaResponseDTO> resultado = service.buscarPorNome("Espada");
        assertFalse(resultado.isEmpty());
        assertEquals("123", resultado.get(0).id());
        assertEquals("Espada Flamejante", resultado.get(0).nome());
        assertEquals(500.0, resultado.get(0).preco());
    }

    @Test
    @DisplayName("Deve buscar por faixa de preço")
    void buscarPorFaixaDePreco() throws IOException {
        ProdutoLojaDocument docFake = new ProdutoLojaDocument();
        docFake.setNome("Poção de Vida");
        docFake.setPreco(50.0);
        SearchResponse<ProdutoLojaDocument> responseFake = mockSearchResponse(docFake, "456");
        when(client.search(any(Function.class), eq(ProdutoLojaDocument.class))).thenReturn(responseFake);
        List<ProdutoLojaResponseDTO> resultado = service.buscarPorFaixaDePreco(10.0, 100.0);
        assertFalse(resultado.isEmpty());
        assertEquals("Poção de Vida", resultado.get(0).nome());
        assertEquals(50.0, resultado.get(0).preco());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando o Elasticsearch não achar nada")
    void deveRetornarListaVazia() throws IOException {
        SearchResponse<ProdutoLojaDocument> responseVazia = mockSearchResponseVazia();
        when(client.search(any(Function.class), eq(ProdutoLojaDocument.class))).thenReturn(responseVazia);
        List<ProdutoLojaResponseDTO> resultado = service.buscarPorNome("ItemQueNaoExiste");
        assertTrue(resultado.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private SearchResponse<ProdutoLojaDocument> mockSearchResponse(ProdutoLojaDocument doc, String id) {
        SearchResponse<ProdutoLojaDocument> searchResponse = mock(SearchResponse.class);
        HitsMetadata<ProdutoLojaDocument> hitsMetadata = mock(HitsMetadata.class);
        Hit<ProdutoLojaDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(doc);
        when(hit.id()).thenReturn(id);
        when(hitsMetadata.hits()).thenReturn(List.of(hit));
        when(searchResponse.hits()).thenReturn(hitsMetadata);

        return searchResponse;
    }

    @SuppressWarnings("unchecked")
    private SearchResponse<ProdutoLojaDocument> mockSearchResponseVazia() {
        SearchResponse<ProdutoLojaDocument> searchResponse = mock(SearchResponse.class);
        HitsMetadata<ProdutoLojaDocument> hitsMetadata = mock(HitsMetadata.class);

        when(hitsMetadata.hits()).thenReturn(List.of());
        when(searchResponse.hits()).thenReturn(hitsMetadata);

        return searchResponse;
    }
}