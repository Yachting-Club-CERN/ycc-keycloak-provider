package ch.cern.ycc.keycloakprovider;

import ch.cern.ycc.keycloakprovider.db.UserEntity;
import ch.cern.ycc.keycloakprovider.db.UserRepository;
import java.util.Map;
import java.util.stream.Stream;
import lombok.NonNull;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

/**
 * Keycloak storage provider factory for YCC authentication.
 *
 * @author Lajos Csppento
 */
public class YccUserStorageProvider
    implements CredentialInputValidator,
        OnUserCache,
        UserLookupProvider,
        UserQueryProvider,
        UserStorageProvider {
  private static final String PASSWORD_CACHE_KEY = YccUserAdapter.class.getName() + ".password";

  private final KeycloakSession session;
  private final ComponentModel model;
  private final UserRepository repository;

  /**
   * Creates an instance.
   *
   * @param session Keycloak session
   * @param model Keycloak component model
   * @param repository YCC user repository
   */
  public YccUserStorageProvider(
      @NonNull KeycloakSession session,
      @NonNull ComponentModel model,
      @NonNull UserRepository repository) {
    this.session = session;
    this.model = model;
    this.repository = repository;
  }

  private YccUserAdapter adapt(@NonNull RealmModel realm, UserEntity user) {
    return user == null ? null : new YccUserAdapter(session, realm, model, user, repository);
  }

  ////////////////////////////////////////
  // CredentialInputValidator ////////////
  ////////////////////////////////////////

  @Override
  public boolean supportsCredentialType(String credentialType) {
    return PasswordCredentialModel.TYPE.equals(credentialType);
  }

  @Override
  public boolean isConfiguredFor(RealmModel realm, UserModel userModel, String credentialType) {
    return supportsCredentialType(credentialType) && getPassword(userModel) != null;
  }

  @Override
  public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
    if (supportsCredentialType(credentialInput.getType())) {
      return repository.verifyPassword(user.getUsername(), credentialInput.getChallengeResponse());
    } else {
      return false;
    }
  }

  ////////////////////////////////////////
  // OnUserCache /////////////////////////
  ////////////////////////////////////////
  @Override
  @SuppressWarnings("unchecked")
  public void onCache(RealmModel realm, CachedUserModel cachedUserModel, UserModel userModel) {
    if (userModel instanceof YccUserAdapter user) {
      String password = user.getPassword();
      if (password != null) {
        cachedUserModel.getCachedWith().put(PASSWORD_CACHE_KEY, password);
      }
    }
  }

  private String getPassword(UserModel userModel) {
    if (userModel instanceof CachedUserModel cachedUser) {
      return (String) cachedUser.getCachedWith().get(PASSWORD_CACHE_KEY);
    } else if (userModel instanceof YccUserAdapter user) {
      return user.getPassword();
    } else {
      return null;
    }
  }

  ////////////////////////////////////////
  // UserLookupProvider //////////////////
  ////////////////////////////////////////

  @Override
  public UserModel getUserById(RealmModel realm, String id) {
    long externalId = Long.parseLong(StorageId.externalId(id));
    return adapt(realm, repository.findById(externalId));
  }

  @Override
  public UserModel getUserByUsername(@NonNull RealmModel realm, String username) {
    return adapt(realm, repository.findByUsername(username));
  }

  @Override
  public UserModel getUserByEmail(@NonNull RealmModel realm, String email) {
    return adapt(realm, repository.findByEmail(email));
  }

  ////////////////////////////////////////
  // UserQueryProvider ///////////////////
  ////////////////////////////////////////

  @Override
  public int getUsersCount(@NonNull RealmModel realm, boolean includeServiceAccount) {
    return repository.getCount();
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      @NonNull RealmModel realm, @NonNull String search, Integer firstResult, Integer maxResults) {
    return repository.search(search).map(user -> adapt(realm, user));
  }

  @Override
  public Stream<UserModel> searchForUserStream(
      @NonNull RealmModel realm,
      @NonNull Map<String, String> params,
      Integer firstResult,
      Integer maxResults) {
    return repository
        .search(
            params.get(UserModel.USERNAME),
            params.get(UserModel.EMAIL),
            params.get(UserModel.FIRST_NAME),
            params.get(UserModel.LAST_NAME))
        .map(user -> adapt(realm, user));
  }

  @Override
  public Stream<UserModel> getGroupMembersStream(
      RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
    return Stream.empty();
  }

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(
      RealmModel realm, String attrName, String attrValue) {
    return Stream.empty();
  }

  ////////////////////////////////////////
  // UserStorageProvider /////////////////
  ////////////////////////////////////////

  @Override
  public void close() {
    repository.close();
  }
}
