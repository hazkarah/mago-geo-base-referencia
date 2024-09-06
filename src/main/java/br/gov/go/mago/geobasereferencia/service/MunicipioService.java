package br.gov.go.mago.geobasereferencia.service;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.gov.go.mago.car.core.web.exception.BusinessEntityNotFoundException;
import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import br.gov.go.mago.geobasereferencia.model.Municipio;
import br.gov.go.mago.geobasereferencia.model.dto.GeometriaPontoDTO;
import br.gov.go.mago.geobasereferencia.repository.jpa.LimiteMunicipiosRepository;
import br.gov.go.mago.geobasereferencia.repository.jpa.MunicipioRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = Throwable.class)
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private LimiteMunicipiosRepository limiteMunicipiosMTRepository;

    @Cacheable(cacheNames = "listaDeMunicipiosPorUfComCache", key = "#uf")
    public List<Municipio> readByUF(String uf) {
        return municipioRepository.findByUfOrderByNome(uf);
    }

    @Cacheable(cacheNames = "readByCodigoIBGE", key = "T(java.lang.String).valueOf(#codigoIBGE)")
    public Municipio readByCodigoIBGE(Integer codigoIBGE) {
        return municipioRepository.findFirstByCodigoIbge(codigoIBGE).orElseThrow(() ->
                new BusinessEntityNotFoundException(String.format("Municipio código IBGE %d", codigoIBGE)));
    }

    public Page<Municipio> read(String nome, Pageable pageable) {
        if (StringUtils.hasText(nome)) {
            return municipioRepository.findByNomeContainingIgnoreCaseOrderByNome(nome, pageable);
        }
        return municipioRepository.findAllByOrderByNome(pageable);
    }

    public List<Municipio> findByCodigoIbgeIn(List<Integer> codigosIbge) {
        return municipioRepository.findByCodigoIbgeIn(codigosIbge);
    }

    public List<LimiteMunicipios> findByIntersectsGeometry(Geometry geometry) {
        return limiteMunicipiosMTRepository.findByIntersectsGeometry(geometry);
    }

    public boolean within(String codibge, GeometriaPontoDTO ponto) {
        return limiteMunicipiosMTRepository.withInNativeQuery(Integer.parseInt(codibge), ponto.getLongitude(), ponto.getLatitude()).isPresent();
    }

    public List<LimiteMunicipios> within(Double latitude, Double longitude) {
        return limiteMunicipiosMTRepository.withIn(latitude, longitude);
    }
}
