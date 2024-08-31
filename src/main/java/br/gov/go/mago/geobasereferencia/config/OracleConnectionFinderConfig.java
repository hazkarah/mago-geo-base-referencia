package br.gov.go.mago.geobasereferencia.config;

import java.lang.reflect.Field;
import java.sql.Connection;

import org.geolatte.geom.codec.db.oracle.DefaultConnectionFinder;

import com.zaxxer.hikari.pool.HikariProxyConnection;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OracleConnectionFinderConfig extends DefaultConnectionFinder {

    private static final long serialVersionUID = 1L;

    @Override
    public Connection find(Connection con) {
        try {
            Field delegate = ((HikariProxyConnection) con).getClass().getSuperclass().getDeclaredField("delegate");
            delegate.setAccessible(true);
            return (Connection) delegate.get(con);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
