package br.gov.go.mago.geobasereferencia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.model.LimiteMunicipios;
import br.gov.go.mago.geobasereferencia.model.dto.LimiteMunicipioMTDTO;
import br.gov.go.mago.geobasereferencia.service.LimiteMunicipiosService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/feicao-ponto-atividade")
public class LimiteMunicipioController {

    @Autowired
    private LimiteMunicipiosService service;

    @PutMapping(path = "/interseccao-limite_municipios", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verifica se uma coordenada geográfica está dentro de um polígono de município.", description = "recebe um código de IBGE e uma coordenada geográfica")
    // @AuthorizationTokenNotRequired
    public ResponseEntity<?> findLimiteMunicipiosByIntersectsGeometry(@RequestBody Geometry geometry) {
        List<LimiteMunicipios> listaLimiteMunicipios = service.findByIntersectsGeometry(geometry);
        if (listaLimiteMunicipios != null) {
            return ResponseEntity
                    .ok(listaLimiteMunicipios.stream().map(LimiteMunicipioMTDTO::new).collect(Collectors.toList()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
