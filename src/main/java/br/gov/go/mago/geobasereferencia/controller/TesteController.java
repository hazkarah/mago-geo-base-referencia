package br.gov.go.mago.geobasereferencia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.go.mago.geobasereferencia.service.GeometriaService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/api/teste", path = "/api/teste")
public class TesteController extends DefaultController {

        @Autowired
        GeometriaService geometriaService;


        @GetMapping("/{timeout}")
        public ResponseEntity<?> testeTimeout(@PathVariable long timeout) throws InterruptedException {
            Thread.sleep(timeout);
            return ResponseEntity.ok().body("OK");
        }

        @GetMapping("/wms-image")
        public ResponseEntity<?> wmsimage() throws InterruptedException {
            return ResponseEntity.ok().body(this.geometriaService.exportWMSToBase64Image());
        }
}
