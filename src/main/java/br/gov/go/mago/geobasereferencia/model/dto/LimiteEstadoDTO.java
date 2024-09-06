package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.model.LimiteEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimiteEstadoDTO implements Serializable {

    public LimiteEstadoDTO(LimiteEstado limiteEstado) {
        this.longitude = limiteEstado.getGeometry().getCoordinate().getX();
        this.latitude = limiteEstado.getGeometry().getCoordinate().getY();
        this.id = limiteEstado.getObjectId().longValue();
    }

    private Double longitude;
    private Double latitude;
    private Long id;
}
