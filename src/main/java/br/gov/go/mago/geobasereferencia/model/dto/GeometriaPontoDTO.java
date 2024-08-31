package br.gov.go.mago.geobasereferencia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeometriaPontoDTO implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GeometriaPontoDTO.class.getName());

    private Double longitude;
    private Double latitude;
    private Integer id;
    private String wkt;

    @JsonIgnore
    public Geometry getGeometry() {
        if (wkt != null) {
            try {
                WKTReader reader = new WKTReader();
                Geometry geometry = reader.read(this.wkt);
                geometry.setSRID(4674);
                return geometry;
            } catch (ParseException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                return null;
            }
        }

        return null;
    }
}
