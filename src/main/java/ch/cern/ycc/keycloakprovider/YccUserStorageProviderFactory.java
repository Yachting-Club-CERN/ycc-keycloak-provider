package ch.cern.ycc.keycloakprovider;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.NonNull;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;


/**
 * Keycloak storage provider factory for YCC authentication.
 *
 * @author Lajos Csppento
 */
// TODO https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-rest/keycloak-custom-providers/src/main/java/com/baeldung/auth/provider/user/CustomUserStorageProvider.java
public class YccUserStorageProviderFactory
    implements UserStorageProviderFactory<YccUserStorageProvider> {

  private final List<ProviderConfigProperty> configMetadata;

  public YccUserStorageProviderFactory() {
    this.configMetadata = ProviderConfigurationBuilder.create()
        .property()
        .name(YccKeycloakProviderConstants.JDBC_URL_PROPERTY_NAME)
        .label("JDBC URL")
        .type(ProviderConfigProperty.STRING_TYPE)
        .helpText("Database JDBC")
        .add()
        .property()
        .name(YccKeycloakProviderConstants.JDBC_USERNAME_PROPERTY_NAME)
        .label("Username")
        .type(ProviderConfigProperty.STRING_TYPE)
        .helpText("Database username")
        .add()
        .property()
        .name(YccKeycloakProviderConstants.JDBC_PASSWORD_PROPERTY_NAME)
        .label("Password")
        .type(ProviderConfigProperty.STRING_TYPE)
        .helpText("Database password")
        .secret(true)
        .add()
        .build();;
  }

  // Configuration support methods
  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return configMetadata;
  }

  @Override
  public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
    try (Connection c = DbUtil.getConnection(config)) {
      log.info("[I84] Testing connection..." );
      c.createStatement().execute(config.get(CONFIG_KEY_VALIDATION_QUERY));
      log.info("[I92] Connection OK !" );
    }
    catch(Exception ex) {
      log.warn("[W94] Unable to validate connection: ex={}", ex.getMessage());
      throw new ComponentValidationException("Unable to validate database connection",ex);
    }
  }

  // TODO
  @Override
  public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
    log.info("[I94] onUpdate()" );
  }

  // TODO
  @Override
  public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
    log.info("[I99] onCreate()" );
  }

  @Override
  public YccUserStorageProvider create(
      @NonNull KeycloakSession session, @NonNull ComponentModel model) {
    EntityManager entityManager =
        session.getProvider(JpaConnectionProvider.class, "ycc-db").getEntityManager();
    YccUserRepository repository = new YccUserRepository(entityManager);
    return new YccUserStorageProvider(session, model, repository);
  }

  @Override
  public String getId() {
    return "ycc-user-provider";
  }
}
