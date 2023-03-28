package ch.cern.ycc.keycloakprovider;

import ch.cern.ycc.keycloakprovider.db.UserEntity;
import ch.cern.ycc.keycloakprovider.db.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;

/**
 * Keycloak adapter for {@link UserEntity}.
 *
 * @author Lajos Cseppento
 */
public class YccUserAdapter extends AbstractUserAdapter {
  private final UserEntity user;
  private final UserRepository repository;

  /**
   * Creates a new instance.
   *
   * @param session Keycloak session
   * @param realm Keycloak realm
   * @param model Keycloak component model
   * @param user YCC user entity
   */
  public YccUserAdapter(
      @NonNull KeycloakSession session,
      @NonNull RealmModel realm,
      @NonNull ComponentModel model,
      @NonNull UserEntity user,
      @NonNull UserRepository repository) {
    super(session, realm, model);
    this.storageId = new StorageId(storageProviderModel.getId(), String.valueOf(user.getId()));
    this.user = user;
    this.repository = repository;
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
  public List<String> getAttribute(String name) {
    // TODO
    return super.getAttribute(name);
  }

  @Override
  public Stream<String> getAttributeStream(String name) {
    // TODO
    return super.getAttributeStream(name);
  }

  @Override
  public Map<String, List<String>> getAttributes() {
    MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();

    // These are needed to have a decent Keycloak user profile for the clients
    attributes.add(UserModel.USERNAME, getUsername());
    attributes.add(UserModel.FIRST_NAME, getFirstName());
    attributes.add(UserModel.LAST_NAME, getLastName());
    attributes.add(UserModel.EMAIL, getEmail());

    // This is a good location to add extra YCC-specific attributes

    return attributes;
  }

  @Override
  protected Set<GroupModel> getGroupsInternal() {
    // This is a good location to add extra groups
    return Set.of(createOrGetGroup(Constants.YCC_USERS_GROUP));
  }

  @Override
  protected Set<RoleModel> getRoleMappingsInternal() {
    return Set.of(
        createOrGetRole(
            repository.isActiveMember(user)
                ? Constants.YCC_ACTIVE_MEMBER_ROLE
                : Constants.YCC_INACTIVE_MEMBER_ROLE)
        // This is a good location to add extra roles
        );
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    return new LegacyUserCredentialManager(session, realm, this);
  }

  /**
   * Gets a group with the specified id. Creates it dynamically if it does not exist.
   *
   * @param idAndName id = name, for simplicity
   * @return the group
   */
  private GroupModel createOrGetGroup(String idAndName) {
    GroupModel group = realm.getGroupById(idAndName);
    if (group == null) {
      group = realm.createGroup(idAndName, idAndName);
    }
    return group;
  }

  /**
   * Gets a role with the specified id. Creates it dynamically if it does not exist.
   *
   * @param idAndName id = name, for simplicity
   * @return the role
   */
  private RoleModel createOrGetRole(String idAndName) {
    RoleModel role = realm.getRoleById(idAndName);
    if (role == null) {
      role = realm.addRole(idAndName, idAndName);
    }
    return role;
  }
}
