package br.gov.go.mago.geobasereferencia.service;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import br.gov.go.mago.geobasereferencia.model.dto.GeometriaPontoDTO;
import br.gov.go.mago.geobasereferencia.repository.jpa.LimiteMunicipiosRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LimiteMunicipiosService {

    @Autowired
    private LimiteMunicipiosRepository limiteMunicipiosRepository;

    public List<LimiteMunicipios> findByIntersectsGeometry(Geometry geometry) {
        return limiteMunicipiosRepository.findByIntersectsGeometry(geometry);
    }

    public boolean within(String codibge, GeometriaPontoDTO ponto) {
        return limiteMunicipiosRepository.withInNativeQuery(Integer.parseInt(codibge), ponto.getLongitude(), ponto.getLatitude()).isPresent();
    }
}


