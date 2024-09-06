package br.gov.go.mago.geobasereferencia.controller;

import br.gov.go.mago.geobasereferencia.config.GeoserverPropertiesConfig;
import br.gov.go.mago.geobasereferencia.model.dto.CamadaDTO;
import br.gov.go.mago.geobasereferencia.model.dto.GeoJSONParamsDTO;
import br.gov.go.mago.geobasereferencia.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.xml.MetadataURL;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/camadas")
public class CamadaController {

    private final GeoserverPropertiesConfig geoserverProperties;

    public CamadaController(@Autowired @Qualifier("geoserverPropertiesConfig") GeoserverPropertiesConfig geoserverProperties) {
        this.geoserverProperties = geoserverProperties;
    }

    @GetMapping(path = "listar", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<CamadaDTO>> carregarCamadas(@RequestParam String url) {
        try {
            URL urlPesquisa = new URL(url);
            final WebMapServer wms = new WebMapServer(urlPesquisa);
            WMSCapabilities capabilities = wms.getCapabilities();

            List<CamadaDTO> camadas = getCamadas(capabilities.getLayer(), wms.getInfo().getSource().toString());
            CamadaDTO camadaDTO = new CamadaDTO(capabilities.getLayer(), camadas, wms.getInfo().getSource().toString());

            String organizationName = getTitle(capabilities, wms.getInfo().getSource().toString());
            camadaDTO.setTitle(organizationName);

            return ResponseEntity.ok(Collections.singletonList(camadaDTO));
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (final ServiceException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static String getTitle(final WMSCapabilities capabilities, String url) {
        if (StringUtils.isNotBlank(capabilities.getLayer().getTitle()) &&
            !"GeoServer Web Map Service".equalsIgnoreCase(capabilities.getLayer().getTitle())) {
            return capabilities.getLayer().getTitle().trim();
        } else if (capabilities.getService() != null && capabilities.getService().getContactInformation() != null
            && StringUtils.isNotBlank(capabilities.getService().getContactInformation().getOrganisationName())) {
            return capabilities.getService().getContactInformation().getOrganisationName().toString().trim();
        }
        return url;
    }

    @GetMapping(path = "sema", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> carregarCamadas() {
        try {
            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            final URL url = new URL(geoserverProperties.getUrl());
            final WebMapServer wms = new WebMapServer(url);
            final WMSCapabilities capabilities = wms.getCapabilities();
            List<CamadaDTO> camadas = getCamadas(capabilities.getLayer(), url.toString());
            return ResponseEntity.ok(camadas);
        } catch (final RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (final ServiceException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "geoserver", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getGeoserverURL() {
        try {
            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            final URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            Map obj = new HashMap();
            obj.put("url", url.toString());

            return ResponseEntity.ok(obj);
        } catch (final RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (final MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "metadado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMetadado(@RequestParam String camadaName) {
        try {
            Map<String, String> response = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            MetadataURL metadata = null;

            String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            WebMapServer wms = new WebMapServer(url);
            WMSCapabilities capabilities = wms.getCapabilities();

            List<Layer> layers = capabilities.getLayerList();
            for (Layer layer : layers) {
                if (camadaName.equals(layer.getName())) {
                    metadata = layer.getMetadataURL().stream()
                        .filter(meta -> "geonetwork_link".equals(meta.getType()))
                        .findFirst()
                        .orElse(null);
                    break;
                }
            }

            response.put("link", metadata != null ? String.valueOf(metadata.getUrl()) : null);
            String json = mapper.writeValueAsString(response);
            return ResponseEntity.ok(json);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL malformada: " + e.getMessage());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de comunicação com o servidor: " + e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Erro ao acessar o serviço: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro de entrada/saída: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    private static List<CamadaDTO> getCamadas(Layer layer, String url) {
        return Arrays.stream(layer.getChildren())
            .map(c -> new CamadaDTO(c, getCamadas(c, url), url))
            .collect(Collectors.toList());

    }

    @GetMapping(path = "feature-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFeatureInfo(@RequestParam String url) {
        try {
            HashMap<String, Object> body = getJSON(url);
            List<Map<String, Object>> features = getFeatures(body);

            // Verifica se a lista de features está vazia antes de prosseguir
            if (features.isEmpty()) {
                return ResponseEntity.badRequest().body("Nenhuma feature encontrada.");
            }

            // Inicializa o sistema de coordenadas e a transformação uma vez
            CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4674", true);
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857", true);
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

            // Processa cada feature para calcular a área
            OptionalDouble optionalArea = features.stream()
                .map(feature -> (LinkedHashMap<String, Object>) feature.remove("geometry"))
                .filter(geometry -> geometry != null && "Polygon".equalsIgnoreCase((String) geometry.get("type")))
                .mapToDouble(geometry -> calculateAreaInHectares(geometry, transform))
                .findFirst();

            if (optionalArea.isPresent()) {
                ((Map) features.get(0).get("properties")).put("Área", String.format("%.4f ha", optionalArea.getAsDouble()));
            }

            return ResponseEntity.ok(features);
        } catch (FactoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao configurar sistema de coordenadas: " + e.getMessage());
        } catch (TransformException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao transformar a geometria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
        }
    }

    // Método auxiliar para calcular a área em hectares
    private double calculateAreaInHectares(LinkedHashMap<String, Object> geometry, MathTransform transform) {
        try {
            List<List<List<Double>>> coordinates = (List<List<List<Double>>>) geometry.get("coordinates");

            // Converte as coordenadas para o formato GeoJSON
            String geoJsonString = String.format("{\"type\": \"Polygon\", \"coordinates\": %s}", coordinates.toString());

            // Usa GeoTools para ler a geometria a partir do GeoJSON
            GeometryJSON geometryJSON = new GeometryJSON();
            Geometry geom = geometryJSON.read(geoJsonString);

            // Transforma a geometria para o sistema de coordenadas apropriado
            Geometry transformedGeom = JTS.transform(geom, transform);

            // Calcula e retorna a área em hectares
            return transformedGeom.getArea() / 10_000; // Convertendo para hectares
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular a área da geometria: " + e.getMessage(), e);
        }
    }

    @PostMapping(path = "geojson", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJsonFromServer(@RequestBody GeoJSONParamsDTO geoJSONParamsDTO) {
        try {
            final HashMap body = getJSON(geoJSONParamsDTO.getUrl());
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "getrows", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRowsJsonFromServer(@RequestParam String url, @RequestParam String cql_filter) {
        try {
            if (!cql_filter.isEmpty()) {
                url += cql_filter;
            }
            final HashMap body = getJSONFromGeoService(url);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static HashMap<String, Object> getJSON(String url) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Geometry> entity = new HttpEntity<>(headers);
        URI urlSearch = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(urlSearch, HttpMethod.GET, entity, HashMap.class);
            if (response.getStatusCode().value() % 100 == 4) {
                throw new Exception("Serviço indisponível.");
            } else if (response.getStatusCode().value() % 500 == 4 || response.getStatusCode().value() != 200) {
                throw new Exception("Falha interna no serviço remoto de WMS.");
            } else if (response.getBody().containsValue("ServiceException")) {
                throw new Exception("Falha ao carregar a camada do serviço remoto de WFS.");
            }
            return response.getBody();
        } catch (final RestClientException e) {
            if (e.getCause() instanceof HttpMessageNotReadableException) {
                throw new Exception("Falha no serviço de WMS remoto. Formato de resposta inválido.", e);
            }
            throw new Exception("Falha interna no serviço remoto de WMS.", e);
        }
    }

    private static HashMap<String, Object> getJSONFromGeoService(String url) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Geometry> entity = new HttpEntity<>(headers);

        URI urlSearch = Utils.containsNonAsciiOrBrazilianChars(url)
            ? Utils.buildURIWithSpecialCharacters(url)
            : UriComponentsBuilder.fromHttpUrl(url).build(false).toUri();

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(urlSearch, HttpMethod.GET, entity, HashMap.class);

            // Verifica o status da resposta
            int statusCode = response.getStatusCode().value();
            if (statusCode >= 400 && statusCode < 500) {
                throw new Exception("Serviço indisponível.");
            } else if (statusCode >= 500 || statusCode != 200) {
                throw new Exception("Falha interna no serviço remoto de WMS.");
            }

            return response.getBody();
        } catch (HttpMessageNotReadableException e) {
            throw new Exception("Falha no serviço de WMS remoto. Formato de resposta inválido.", e);
        } catch (RestClientException e) {
            throw new Exception("Falha interna no serviço remoto de WMS.", e);
        }
    }

    private static List<Map<String, Object>> getFeatures(Map<String, Object> body) {
        if (body == null) {
            return Collections.emptyList();
        }

        if (body.containsKey("features")) {
            // Feature collection
            return (List<Map<String, Object>>) body.get("features");
        } else if (body.containsKey("properties")) {
            // Single feature
            return Collections.singletonList(body);
        }

        return Collections.emptyList();
    }
}
