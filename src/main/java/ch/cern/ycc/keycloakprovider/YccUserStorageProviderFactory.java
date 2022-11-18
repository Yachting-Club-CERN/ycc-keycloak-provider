package ch.cern.ycc.keycloakprovider;

import javax.persistence.EntityManager;
import lombok.NonNull;
import org.keycloak.component.ComponentModel;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * Keycloak storage provider factory for YCC authentication.
 *
 * @author Lajos Csppento
 */
public class YccUserStorageProviderFactory
    implements UserStorageProviderFactory<YccUserStorageProvider> {
  @Override
  public YccUserStorageProvider create(
      @NonNull KeycloakSession session, @NonNull ComponentModel model) {
    EntityManager entityManager =
        session.getProvider(JpaConnectionProvider.class, "user-store").getEntityManager();
    YccUserRepository repository = new YccUserRepository(entityManager);
    return new YccUserStorageProvider(session, model, repository);
  }

  @Override
  public String getId() {
    return "ycc-user-provider";
  }
}
