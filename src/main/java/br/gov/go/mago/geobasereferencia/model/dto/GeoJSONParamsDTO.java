package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeoJSONParamsDTO implements Serializable {
    private String url;
}
