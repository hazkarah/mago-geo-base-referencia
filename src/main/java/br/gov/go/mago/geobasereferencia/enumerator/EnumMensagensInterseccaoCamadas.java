package br.gov.go.mago.geobasereferencia.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnumMensagensInterseccaoCamadas {

    MENSAGEM53("A área da atividade não pode sobrepor terra indígena."),
    MENSAGEM54("A área da atividade não pode sobrepor zona de amortecimento de terra indígena."),
    MENSAGEM55("A camada se intersecta com a Unidade de Conservação "),
    MENSAGEM56("A área da atividade não pode sobrepor zona de amortecimento de Unidade de Conservação (UC)."),
    MENSAGEM57("A distância linear entre a camada e a Terra indígena  "),
    MENSAGEM58("A área da atividade não pode sobrepor Área de Preservação Permanente (APP)."),
    MENSAGEM59("Há interseccção com a bacia:  ");

    @Getter
    private final String descricao;

}
