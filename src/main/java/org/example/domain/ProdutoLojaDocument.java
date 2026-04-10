package org.example.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
@Getter
@Setter
@Document(indexName = "guilda_loja", createIndex = false)
public class ProdutoLojaDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String nome;

    @Field(type = FieldType.Text, analyzer = "analyzer_pt")
    private String descricao;

    @Field(type = FieldType.Keyword)
    private String categoria;

    @Field(type = FieldType.Keyword)
    private String raridade;

    @Field(type = FieldType.Double)
    private Double preco;
}