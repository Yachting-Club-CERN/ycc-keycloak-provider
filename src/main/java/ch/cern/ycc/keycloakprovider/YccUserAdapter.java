package ch.cern.ycc.keycloakprovider;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

/**
 * Keycloak adapter for {@link YccUserEntity}.
 *
 * @author Lajos Cseppento
 */
public class YccUserAdapter extends AbstractUserAdapter {
  private final YccUserEntity user;

  /**
   * Creates a new instance.
   *
   * @param session Keycloak session
   * @param realm Keycloak realm
   * @param model Keycloak component model
   * @param user YCC user entity
   */
  public YccUserAdapter(
      KeycloakSession session, RealmModel realm, ComponentModel model, YccUserEntity user) {
    super(session, realm, model);
    this.storageId = new StorageId(storageProviderModel.getId(), String.valueOf(user.getId()));
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getFirstName() {
    return user.getFirstName();
  }

  @Override
  public String getLastName() {
    return user.getLastName();
  }

  @Override
  public String getEmail() {
    return user.getEmail();
  }

  /**
   * Returns the password (hashed).
   *
   * @return the password (hashed)
   */
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    return new LegacyUserCredentialManager(session, realm, this);
  }
}
