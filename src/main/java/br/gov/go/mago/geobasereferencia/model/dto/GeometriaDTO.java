package br.gov.go.mago.geobasereferencia.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class GeometriaDTO {

    private Integer id;
    private String wkt;
    private String descricao;

}
