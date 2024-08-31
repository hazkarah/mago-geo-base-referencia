package br.gov.go.mago.geobasereferencia.model.dto;

import java.io.Serializable;
import java.util.List;

import org.geotools.ows.wms.Layer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CamadaDTO implements Serializable {

    
    private String name;

    
    private String title;

    
    private boolean queryable;

    
    private String url;

    
    private List<CamadaDTO> camadas;

    public CamadaDTO(Layer layer, List<CamadaDTO> camadas, String url) {
        this.name = layer.getName();
        this.title = layer.getTitle();
        this.queryable = layer.isQueryable();
        this.camadas = camadas;
        this.url = url;
    }
}
