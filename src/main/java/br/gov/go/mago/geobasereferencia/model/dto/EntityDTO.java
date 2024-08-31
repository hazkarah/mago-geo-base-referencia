package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class EntityDTO<T> implements Serializable {

    @EqualsAndHashCode.Include
    protected T id;
    protected String acao = "A";  // A, R (adicionar, remover)
    protected Object entityRef; // referencia entidade
}
