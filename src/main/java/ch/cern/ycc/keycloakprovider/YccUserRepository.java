package ch.cern.ycc.keycloakprovider;

import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.NonNull;

/**
 * Keycloak repository for YCC users.
 *
 * @author Lajos Cseppento
 */
class YccUserRepository implements AutoCloseable {
  private final EntityManager entityManager;

  YccUserRepository(@NonNull EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  int getCount() {
    return entityManager
        .createNamedQuery("YccUserEntity.getCount", Long.class)
        .getSingleResult()
        .intValue();
  }

  Stream<YccUserEntity> findAll() {
    return entityManager
        .createNamedQuery("YccUserEntity.findAll", YccUserEntity.class)
        .getResultStream();
  }

  YccUserEntity findById(long id) {
    return entityManager.find(YccUserEntity.class, id);
  }

  YccUserEntity findByUsername(String username) {
    return entityManager
        .createNamedQuery("YccUserEntity.findByUsername", YccUserEntity.class)
        .setParameter("username", username)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  YccUserEntity findByEmail(String email) {
    return entityManager
        .createNamedQuery("YccUserEntity.findByEmail", YccUserEntity.class)
        .setParameter("email", email)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  YccUserEntity findByUsernameOrEmail(String usernameOrEmail) {
    return entityManager
        .createNamedQuery("YccUserEntity.findByUsernameOrEmail", YccUserEntity.class)
        .setParameter("usernameOrEmail", usernameOrEmail)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  Stream<YccUserEntity> search(String search) {
    return entityManager
        .createNamedQuery("YccUserEntity.search", YccUserEntity.class)
        .setParameter("search", search)
        .getResultStream();
  }

  Stream<YccUserEntity> search(String username, String email, String firstName, String lastName) {
    return entityManager
        .createNamedQuery("YccUserEntity.searchByParameters", YccUserEntity.class)
        .setParameter("username", username)
        .setParameter("email", email)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .getResultStream();
  }

  boolean verifyPassword(@NonNull String username, @NonNull String password) {
    return YccPasswordHasher.verify(password, findByUsernameOrEmail(username).getPassword());
  }

  @Override
  public void close() {
    // Note: Reference implementation does not close the EntityManager
    // https://github.com/keycloak/keycloak-quickstarts/blob/9f1470474af85a9ab3ac0ba64ed0cc68dbd2b921/user-storage-jpa/src/main/java/org/keycloak/quickstart/storage/user/MyUserStorageProvider.java#L92
  }
}
