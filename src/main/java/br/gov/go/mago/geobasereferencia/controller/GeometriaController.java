package br.gov.go.mago.geobasereferencia.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.model.AreaGeometria;
import br.gov.go.mago.geobasereferencia.service.GeometriaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/geometria")

public class GeometriaController {

    private final GeometriaService service;

    public GeometriaController(GeometriaService service) {
        this.service = service;
    }

    @Operation(summary = "Calcula área geodésica da geometria submetida via wkt")
    @PostMapping(value = "/area-geodesica", produces = MediaType.APPLICATION_JSON_VALUE)
    // @AuthorizationTokenNotRequired
    public ResponseEntity<AreaGeometria> getAreaFromWkt(@RequestBody AreaGeometria geometria) {
        return ResponseEntity.ok(this.service.calculaAreaGeodesica(geometria));
    }
}
