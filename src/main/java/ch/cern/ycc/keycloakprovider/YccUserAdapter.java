package ch.cern.ycc.keycloakprovider;

import ch.cern.ycc.keycloakprovider.db.UserEntity;
import ch.cern.ycc.keycloakprovider.db.UserRepository;
import java.util.LinkedHashSet;
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
   * @param repository user repository
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
  public String getFirstAttribute(String name) {
    List<String> attr = getAttributes().get(name);
    return attr == null || attr.isEmpty() ? null : attr.get(0);
  }

  /**
   * @deprecated Use {@link #getAttributeStream(String)} instead. To be removed when Keycloak
   *     removes it.
   */
  @Deprecated(forRemoval = true)
  @Override
  public List<String> getAttribute(String name) {
    List<String> attr = getAttributes().get(name);
    return attr == null ? List.of() : attr;
  }

  @Override
  public Stream<String> getAttributeStream(String name) {
    List<String> attr = getAttributes().get(name);
    return attr == null ? Stream.empty() : attr.stream();
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
    return Set.of(createOrGetGroup(Constants.YCC_ALL_MEMBERS_GROUP));
  }

  @Override
  protected Set<RoleModel> getRoleMappingsInternal() {
    var roles = new LinkedHashSet<RoleModel>();

    if (repository.isActiveMember(user)) {
      roles.add(createOrGetRole(Constants.YCC_ACTIVE_MEMBER_ROLE));

      repository
          .findActiveLicences(user)
          .forEach(
              licence ->
                  roles.add(
                      createOrGetRole(Constants.YCC_LICENCE_ROLE_PREFIX + licence.toLowerCase())));
    }

    // This is a good location to add extra roles

    return Set.copyOf(roles);
  }

  @Override
  public SubjectCredentialManager credentialManager() {
    return new LegacyUserCredentialManager(session, realm, this);
  }

  /**
   * Gets the group with the specified name. Creates it dynamically if it does not exist.
   *
   * @param name name
   * @return the group
   */
  private GroupModel createOrGetGroup(@NonNull String name) {
    return realm
        .getGroupsStream()
        .filter(group -> group.getName().equals(name))
        .findFirst()
        .orElseGet(() -> realm.createGroup(name));
  }

  /**
   * Gets the role with the specified name. Creates it dynamically if it does not exist.
   *
   * @param name name
   * @return the role
   */
  private RoleModel createOrGetRole(@NonNull String name) {
    RoleModel role = realm.getRole(name);
    if (role == null) {
      role = realm.addRole(name);
    }
    return role;
  }
}
