package br.gov.go.mago.geobasereferencia.repository;

import br.gov.go.mago.geobasereferencia.model.UnidadesConservacao;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeConservacaoRepository extends JpaRepository<UnidadesConservacao, Integer> {

    @Query(value = "select * from BASE_REFERENCIA_GEO.UNIDADES_CONSERVACAO " +
            " where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674),'ANYINTERACT', sdo_cs.transform( sdo_geometry( 2001, 4674,SDO_POINT_TYPE(:longitude,:latitude,NULL),null,null),4674),0.05) = 'TRUE' " +
            " and upper(grupo) in ('PROTEÇÃO INTEGRAL', 'PROTEÇÃO PARCIAL')",
            nativeQuery = true)
    List<UnidadesConservacao> findByLongitudeLatitude(Double longitude, Double latitude);

    @Query(value = "select * from BASE_REFERENCIA_GEO.UNIDADES_CONSERVACAO " +
            " where SDO_GEOM.RELATE(sdo_cs.transform(shape, 4674), 'ANYINTERACT', :geometry,0.05) = 'TRUE' ",
            nativeQuery = true)
    List<UnidadesConservacao> findByLongitudeLatitude(Geometry geometry);
}
