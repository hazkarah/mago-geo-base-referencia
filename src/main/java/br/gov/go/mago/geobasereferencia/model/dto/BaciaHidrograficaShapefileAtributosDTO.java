package br.gov.go.mago.geobasereferencia.model.dto;

import org.locationtech.jts.geom.Geometry;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaciaHidrograficaShapefileAtributosDTO extends ShapefileAtributosDTO {

    private Integer objectId;
    private String bacia;
    private String subBacia;
    private String microBacia;
    private String codigo;
    private String unidade;
    private Geometry geometry;

}
