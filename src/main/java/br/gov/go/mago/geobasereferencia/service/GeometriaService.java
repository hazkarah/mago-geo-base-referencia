package br.gov.go.mago.geobasereferencia.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.go.mago.geobasereferencia.config.GeoserverPropertiesConfig;
import br.gov.go.mago.geobasereferencia.model.AreaGeometria;
import br.gov.go.mago.geobasereferencia.model.dto.CamadaDTO;
import br.gov.go.mago.geobasereferencia.repository.jpa.GeometriaRepository;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class GeometriaService {

    private final GeometriaRepository geometriaRepository;

    private final GeoserverPropertiesConfig geoserverProperties;


    public GeometriaService(@Autowired GeometriaRepository geometriaRepository, @Autowired GeoserverPropertiesConfig propertiesConfig) {
        this.geometriaRepository = geometriaRepository;
        this.geoserverProperties = propertiesConfig;

    }

    public AreaGeometria calculaAreaGeodesica(AreaGeometria geometria) {
        return geometriaRepository.calculaArea(geometria.getWkt());
    }

    public String exportWMSToBase64Image() {

        try {


            final String authKeyValue = "&authkey=" + geoserverProperties.getAuthToken();
            URL url = new URL(geoserverProperties.getUrl() + authKeyValue);
            final WebMapServer wms = new WebMapServer(url);
            final WMSCapabilities capabilities = wms.getCapabilities();
            final List<CamadaDTO> camadaDTOs = getCamadas(capabilities.getLayer(), url.toString());


            GetMapRequest request = wms.createGetMapRequest();
            List<Layer> layers = Arrays.asList( capabilities.getLayer().getChildren() );
            Layer layerGeoportal = layers.stream().filter((l) -> l.getName() != null && l.getName().toLowerCase().equals("geoportal")).findFirst().orElse(null);

            // for (Layer layer : WMSUtils.getNamedLayers(capabilities)) {
            //     request.addLayer(layer);
            // }

            if (layerGeoportal != null) {

                // request.addLayer("OSM-Overlay-WMS", "");
                // request.addLayer( layerGeoportal.getName(), "");
                // request.addLayer( "Base de Referência", "");
                String format = "image/png";
                request.setFormat(format);
                request.setDimensions("400", "400"); // sets the dimensions of the image to be returned from the server
                request.setTransparent(true);
                // request.setSRS("EPSG:4326");
                request.setSRS("EPSG:4374");
                // request.setBBox("47.75,12.98,47.86,13.12");
                request.setBBox( capabilities.getLayer().getBoundingBoxes().values().stream().findFirst().orElse(null) );


                try {
                    GetMapResponse response = wms.issueRequest(request);
                    if (response.getContentType().equalsIgnoreCase(format)) {
                        BufferedImage image = ImageIO.read(response.getInputStream());
                        // File outputfile = new File("saved.png");
                        ByteArrayOutputStream  baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        String base64Image = Base64.getEncoder().encodeToString( baos.toByteArray() );
                        return base64Image;
                    } else {
                        StringWriter writer = new StringWriter();
                        IOUtils.copy(response.getInputStream(), writer);
                        String error = writer.toString();
                        System.out.println(error);

                    }
                } catch (ServiceException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            }

            // FileOutputStream img = new
            // FileOutputStream("C:\\Users\\Edhem\\Desktop\\WMSimage.png");
        } catch (IOException e) {
            // There was an error communicating with the server
            // For example, the server is down
        } catch (ServiceException e) {
            // The server returned a ServiceException (unusual in this case)
        }

        return null;
    }

    private static List<CamadaDTO> getCamadas(Layer layer, String url) {
        return Arrays.stream(layer.getChildren())
                .map(c -> new CamadaDTO(c, getCamadas(c, url), url))
                .collect(Collectors.toList());

    }


    private BufferedImage getBufferedImage(WebMapServer wms, Layer l, WMSCapabilities capabilities, String format ) throws Exception {
        GetMapRequest request = wms.createGetMapRequest();
        request.addLayer(l);
        request.setBBox( capabilities.getLayer().getBoundingBoxes().values().stream().findFirst().orElse(null));
        request.setDimensions(400, 400);
        request.setFormat( format );
        request.setSRS("EPSG:4374");
        System.out.println(request.getFinalURL());

        try {
            GetMapResponse response = wms.issueRequest(request);
            if (response.getContentType().equalsIgnoreCase(format)) {
                return ImageIO.read(response.getInputStream());
            } else {
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getInputStream(), writer);
                String error = writer.toString();
                throw new Exception(error);
            }
        } catch (ServiceException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;

        }
    }


}
