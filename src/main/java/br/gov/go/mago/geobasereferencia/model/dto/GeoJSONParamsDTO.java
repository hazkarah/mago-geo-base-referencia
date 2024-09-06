package br.gov.go.mago.geobasereferencia.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GeoJSONParamsDTO implements Serializable {
    private String url;
}
