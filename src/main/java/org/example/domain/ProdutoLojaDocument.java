package org.example.domain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "guilda_loja", createIndex = false)
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoLojaDocument {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private Double preco;
}