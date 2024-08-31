package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MunicipioDTO implements Serializable {

    private String nome;
    private String uf;
    private Integer codigoIbge;

    public MunicipioDTO(LimiteMunicipios limite) {
        this.nome = limite.getNome();
        this.uf = limite.getUf();
        this.codigoIbge = limite.getCodigoIbge();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", nome, uf);
    }
}
