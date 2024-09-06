package br.gov.go.mago.geobasereferencia.repository.jpa;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.go.mago.geobasereferencia.model.TIAmortecimento;

@Repository
public interface TIAmortecimentoRepository extends JpaRepository<TIAmortecimento, Integer> {

    @Query(value = " Select * from BASE_REFERENCIA_GEO.TI_AMORTECIMENTO where SDO_GEOM.RELATE(sdo_cs.transform(shape,4674),'ANYINTERACT', sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<TIAmortecimento> findTIAmortecimentoByGeometryNativeQuery(Double longitude, Double latitude);

    @Query(value = " Select * from BASE_REFERENCIA_GEO.TI_AMORTECIMENTO where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', sdo_cs.transform(SDO_GEOM.SDO_BUFFER(:geometry, 10, 0.0001, 'unit=km'), 4674), 0.05) = 'TRUE'", nativeQuery = true)
    List<TIAmortecimento> findByGeometry(Geometry geometry);

    @Query(value = " Select * from BASE_REFERENCIA_GEO.TI_AMORTECIMENTO where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', sdo_cs.transform( SDO_GEOM.SDO_BUFFER(sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null), 10, 0.0001, 'unit=km'),4674),0.05) = 'TRUE'", nativeQuery = true)
    List<TIAmortecimento> findByGeometry(Double longitude, Double latitude);
}
