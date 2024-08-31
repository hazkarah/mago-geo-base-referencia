package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;
import java.util.List;

import br.gov.go.mago.geobasereferencia.enumerator.EnumLayerAnaliseDeInterseccao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidacaoPontoDTO implements Serializable {
    private List<GeometriaPontoDTO> listaPonto;
    private List<EnumLayerAnaliseDeInterseccao> listaValidacoes;
}
