package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.model.LimiteEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimiteEstadoGODTO implements Serializable {

    public LimiteEstadoGODTO(LimiteEstado limiteEstadoMT) {
        this.longitude = limiteEstadoMT.getGeometry().getCoordinate().getX();
        this.latitude = limiteEstadoMT.getGeometry().getCoordinate().getY();
        this.id = limiteEstadoMT.getObjectId().longValue();
    }

    private Double longitude;
    private Double latitude;
    private Long id;
}
