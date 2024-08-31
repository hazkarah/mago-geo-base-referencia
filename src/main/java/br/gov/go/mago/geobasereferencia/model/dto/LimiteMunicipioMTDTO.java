package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimiteMunicipioMTDTO implements Serializable {

    public LimiteMunicipioMTDTO(LimiteMunicipios limiteMunicipiosMT) {
        this.longitude = limiteMunicipiosMT.getGeometria().getCoordinate().getX();
        this.latitude = limiteMunicipiosMT.getGeometria().getCoordinate().getY();
        this.id = limiteMunicipiosMT.getId().longValue();
    }

    private Double longitude;
    private Double latitude;
    private Long id;
}
