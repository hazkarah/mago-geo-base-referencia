package br.gov.go.mago.geobasereferencia.repository;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.geobasereferencia.model.TerrasIndigenas;

@Repository
public interface TerrasIndigenasRepository extends JpaRepository<TerrasIndigenas, Integer> {

    @Query(value = " Select * from BASE_REFERENCIA_GEO.TERRAS_INDIGENAS where SDO_GEOM.RELATE(sdo_cs.transform(shape,4674),'ANYINTERACT',sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<TerrasIndigenas> findTerrasIndigenasByGeometryNativeQuery(Double longitude, Double latitude);

    @Query(value = " Select * from BASE_REFERENCIA_GEO.TERRAS_INDIGENAS where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', sdo_cs.transform(:geometry,4674),0.05) = 'TRUE'", nativeQuery = true)
    List<TerrasIndigenas> findTerrasIndigenasByGeometryNativeQuery(Geometry geometry);

}
