package ch.cern.ycc.keycloakprovider.utils;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.Persistence;
import org.keycloak.component.ComponentModel;

public class DatabaseHelper {
  public static Connection getConnection(ComponentModel config) throws SQLException {
    Persistence
        .createEntityManagerFactory("ycc-db")
        .createEntityManager()
    String driverClass = config.get(CONFIG_KEY_JDBC_DRIVER);
    try {
      Class.forName(driverClass);
    }
    catch(ClassNotFoundException nfe) {
      throw new RuntimeException("Invalid JDBC driver: " + driverClass + ". Please check if your driver if properly installed");
    }

    return DriverManager.getConnection(config.get(CONFIG_KEY_JDBC_URL),
        config.get(CONFIG_KEY_DB_USERNAME),
        config.get(CONFIG_KEY_DB_PASSWORD));
  }
}
