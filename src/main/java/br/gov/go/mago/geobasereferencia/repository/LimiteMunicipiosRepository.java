package br.gov.go.mago.geobasereferencia.repository;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;

@Repository
public interface LimiteMunicipiosRepository extends JpaRepository<LimiteMunicipios, Integer> {

    @Query("select lm from LimiteMunicipios lm where INTERSECTS(lm.geometria, :geometry) = true")
    List<LimiteMunicipios> findByIntersectsGeometry(Geometry geometry);

    @Query(value = " Select * from BASE_REFERENCIA_GEO.LIM_MUNICIPIOS_MT where COD_IBGE = :codibge and SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    Optional<LimiteMunicipios> withInNativeQuery(Integer codibge, Double longitude, Double latitude);

    @Query(value = " Select * from BASE_REFERENCIA_GEO.LIM_MUNICIPIOS_MT where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674),'ANYINTERACT', sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<LimiteMunicipios> withIn(Double latitude, Double longitude);
}
