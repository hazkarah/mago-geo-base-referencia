package br.gov.go.mago.geobasereferencia.repository.jpa;

import br.gov.go.mago.geobasereferencia.model.AreasUsoRestrito;
import br.gov.go.mago.geobasereferencia.model.ProvinciaHidrologica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinciaHidrologicaRepository extends JpaRepository<ProvinciaHidrologica, Integer> {

    @Query(value = "SELECT * from BASE_REFERENCIA_GEO.PROVINCIA_HIDROLOGICA " +
            " where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT'," +
            " sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude, NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<ProvinciaHidrologica> findByLongitudeLatitude(Double longitude, Double latitude);

}
