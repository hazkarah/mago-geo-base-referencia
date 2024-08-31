package br.gov.go.mago.geobasereferencia.cucumber;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * //TODO Cucumber
 * Ambinte de testes para relatório CUCUMBER
 */
@Getter
@Setter
@Builder
public class Banco {
    private String nome;
    private List<Conta> listaDeContas;
}
