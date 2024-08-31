package br.gov.go.mago.geobasereferencia.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static boolean containsNonAsciiOrBrazilianChars(String s) {
        // Pattern to match non-ASCII characters
        Pattern pattern = Pattern.compile("[^\\p{ASCII}]+");
        return pattern.matcher(s).find();
    }

    public static URI buildURIWithSpecialCharacters(String url) {
        try {


            URL originalUrl = new URL(url);

            String encodedPath = Arrays.stream(originalUrl.getPath().split("/"))
                    .map(pathSegment -> containsNonAsciiOrBrazilianChars(pathSegment) ? encodeValue(pathSegment) : pathSegment)
                    .collect(Collectors.joining("/"));

            String encodedQuery = originalUrl.getQuery() == null ? null : Arrays.stream(originalUrl.getQuery().split("&"))
                    .map(queryParam -> {
                        int equalsIdx = queryParam.indexOf('=');
                        String key = queryParam.substring(0, equalsIdx);
                        String encodedKey = containsNonAsciiOrBrazilianChars(key) ? encodeValue(key) : key;

                        String encodedValue = queryParam.substring(equalsIdx + 1);

                        // check if the value contains a comma
                        if(encodedValue.contains(",")) {
                            encodedValue = Arrays.stream(encodedValue.split(","))
                                    .filter(value -> !containsNonAsciiOrBrazilianChars(value))
                                    .map(value -> encodeValue(value.trim()))
                                    .collect(Collectors.joining(","));
                        } else {
                            encodedValue = containsNonAsciiOrBrazilianChars(encodedValue) ? encodeValue(encodedValue) : encodedValue;
                        }

                        return encodedKey + "=" + encodedValue;
                    })
                    .collect(Collectors.joining("&"));

            return UriComponentsBuilder.newInstance()
                    .scheme(originalUrl.getProtocol())
                    .host(originalUrl.getHost())
                    .port(originalUrl.getPort())
                    .path(encodedPath)
                    .query(encodedQuery)
                    .build()
                    .toUri();

        } catch (Exception e) {
            throw new RuntimeException("Error while encoding URL: " + url, e);
        }
    }


}
