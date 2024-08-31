package br.gov.go.mago.geobasereferencia.cucumber;

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
public class Conta {
    private String dono;
    private Integer numero;
    private Double saldo;
    private Double limite;

    public boolean sacar(Double valor) {
        if (saldo <= valor) {
            // Não pode sacar
            return false;
        } else {
            // Pode sacar
            saldo = saldo - valor;
            return true;
        }
    }

    public boolean depositar(Double quantidade) {

        if (limite <= quantidade + saldo) {
            // Não pode depositar
            return false;
        } else {
            // Pode depositar
            saldo += quantidade;
            return true;
        }
    }
}
