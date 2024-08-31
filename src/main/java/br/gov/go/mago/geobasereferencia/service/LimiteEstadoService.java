package br.gov.go.mago.geobasereferencia.service;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.go.mago.geobasereferencia.model.LimiteEstado;
import br.gov.go.mago.geobasereferencia.repository.LimiteEstadoRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LimiteEstadoService {

    @Autowired
    private LimiteEstadoRepository repository;

    public List<LimiteEstado> findByIntersectsGeometry(Geometry geometry) {
        return repository.findByIntersectsGeometry(geometry);
    }

    public List<Geometry> findAll4674() {
        return repository.findAll4674();
    }
}


