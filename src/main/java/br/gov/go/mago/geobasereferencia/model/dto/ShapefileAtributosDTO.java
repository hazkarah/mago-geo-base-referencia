package br.gov.go.mago.geobasereferencia.model.dto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.go.mago.geobasereferencia.exception.ShapefileConversionException;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public abstract class ShapefileAtributosDTO {

    @SneakyThrows(IllegalAccessException.class)
    private String getAtributo(Field field) {
        field.setAccessible(true);
        return String.valueOf(field.get(this));
    }

    public void validar(Collection<String> atributosShapefile) {
        List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
        if (!atributosShapefile.containsAll(fields.stream().map(this::getAtributo).collect(Collectors.toList()))) {
            String atributosAusentes = fields.stream()
                    .filter(field -> !atributosShapefile.contains(getAtributo(field)))
                    .map(fieldAusente -> String.format("%s: %s", fieldAusente.getName(), getAtributo(fieldAusente)))
                    .collect(Collectors.joining(", "));
            String mensagem = String.format("O(s) seguinte(s) atributo(s) não está(tão) presente(s) no arquivo " +
                    "shapefile: %s", atributosAusentes);
            throw new ShapefileConversionException(mensagem);
        }
    }

}
