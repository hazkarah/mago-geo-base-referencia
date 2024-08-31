package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.enumerator.EnumLayerAnaliseDeInterseccao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoRestricaoPontoGeo implements Serializable {
    private boolean restricaoExistente;
    private String descricaoResultado;
    private String categoria;
    private EnumLayerAnaliseDeInterseccao tipoRestricao;

    public ResultadoRestricaoPontoGeo(boolean restricaoExistente, String descricaoResultado, EnumLayerAnaliseDeInterseccao tipoRestricao) {
        this.restricaoExistente = restricaoExistente;
        this.descricaoResultado = descricaoResultado;
        this.tipoRestricao = tipoRestricao;
    }
}
