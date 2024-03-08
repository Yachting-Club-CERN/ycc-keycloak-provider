package ch.cern.ycc.keycloakprovider;

import ch.cern.ycc.keycloakprovider.db.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class YccUserStorageProviderFactory
    implements UserStorageProviderFactory<YccUserStorageProvider> {
  private static final String DATA_SOURCE_PROPERTY_NAME = "dataSource";
  private final List<ProviderConfigProperty> configurationProperties;

  /** Constructor. Initialises configuration metadata. */
  public YccUserStorageProviderFactory() {
    this.configurationProperties =
        ProviderConfigurationBuilder.create()
            .property()
            .name(DATA_SOURCE_PROPERTY_NAME)
            .label("Data source name")
            .type(ProviderConfigProperty.STRING_TYPE)
            .helpText(
                "Probably ycc-db-local, ycc-db-test or ycc-db-prod. Must be preconfigured before you start the server instance.")
            .add()
            .build();
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return configurationProperties;
  }

  @Override
  public void validateConfiguration(
      @NonNull KeycloakSession session, @NonNull RealmModel realm, @NonNull ComponentModel config)
      throws ComponentValidationException {
    try (EntityManager em = createEntityManager(session, config)) {
      var repository = new UserRepository(em);
      int userCount = repository.getCount();
      log.info("Connected to the database and found {} users", userCount);
    } catch (Exception ex) {
      String msg = "Failed to validate the database configuration: " + ex.getMessage();
      log.warn(msg, ex);
      throw new ComponentValidationException(msg, ex);
    }
  }

  @Override
  public YccUserStorageProvider create(
      @NonNull KeycloakSession session, @NonNull ComponentModel model) {
    UserRepository repository = new UserRepository(createEntityManager(session, model));
    return new YccUserStorageProvider(session, model, repository);
  }

  private static EntityManager createEntityManager(
      @NonNull KeycloakSession session, @NonNull ComponentModel model) {
    String dataSourceName = model.get(DATA_SOURCE_PROPERTY_NAME);
    if (dataSourceName == null) {
      throw new YccKeycloakProviderException("Data source name is null");
    }

    var provider = session.getProvider(JpaConnectionProvider.class, dataSourceName);
    if (provider == null) {
      throw new YccKeycloakProviderException(
          "Data source not found (could not locate JpaConnectionProvider): " + dataSourceName);
    }

    var em = provider.getEntityManager();

    if (em == null) {
      throw new YccKeycloakProviderException(
          "Entity manager not found for data source " + dataSourceName);
    }

    return em;
  }

  @Override
  public String getId() {
    return Constants.ID;
  }

  @Override
  public String getHelpText() {
    return Constants.HELP_TEXT;
  }
}
