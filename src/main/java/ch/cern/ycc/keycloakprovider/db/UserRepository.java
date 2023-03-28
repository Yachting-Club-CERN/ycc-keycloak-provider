package ch.cern.ycc.keycloakprovider.db;

import ch.cern.ycc.keycloakprovider.utils.PasswordHasher;
import java.time.Year;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import lombok.NonNull;

/**
 * Keycloak repository for YCC users.
 *
 * @author Lajos Cseppento
 */
public class UserRepository implements AutoCloseable {
  private static final String HONORARY_MEMBERSHIP_TYPE = "H";
  private final EntityManager entityManager;

  public UserRepository(@NonNull EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public int getCount() {
    return entityManager
        .createNamedQuery("UserEntity.getCount", Long.class)
        .getSingleResult()
        .intValue();
  }

  public Stream<UserEntity> findAll() {
    return entityManager.createNamedQuery("UserEntity.findAll", UserEntity.class).getResultStream();
  }

  public UserEntity findById(long id) {
    return entityManager.find(UserEntity.class, id);
  }

  public UserEntity findByUsername(String username) {
    return entityManager
        .createNamedQuery("UserEntity.findByUsername", UserEntity.class)
        .setParameter("username", username)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  public UserEntity findByEmail(String email) {
    return entityManager
        .createNamedQuery("UserEntity.findByEmail", UserEntity.class)
        .setParameter("email", email)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  public UserEntity findByUsernameOrEmail(String usernameOrEmail) {
    return entityManager
        .createNamedQuery("UserEntity.findByUsernameOrEmail", UserEntity.class)
        .setParameter("usernameOrEmail", usernameOrEmail)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  public Stream<UserEntity> search(String search) {
    return entityManager
        .createNamedQuery("UserEntity.search", UserEntity.class)
        .setParameter("search", search)
        .getResultStream();
  }

  public Stream<UserEntity> search(
      String username, String email, String firstName, String lastName) {
    return entityManager
        .createNamedQuery("UserEntity.searchByParameters", UserEntity.class)
        .setParameter("username", username)
        .setParameter("email", email)
        .setParameter("firstName", firstName)
        .setParameter("lastName", lastName)
        .getResultStream();
  }

  public boolean verifyPassword(@NonNull String username, @NonNull String password) {
    return PasswordHasher.verify(password, findByUsernameOrEmail(username).getPassword());
  }

  public boolean isActiveMember(@NonNull UserEntity user) {
    if (HONORARY_MEMBERSHIP_TYPE.equals(user.getMembershipType())) {
      return true;
    }

    return entityManager
        .createNamedQuery("FeeRecordEntity.findAllByMemberIdAndFinancialYear")
        .setParameter("memberId", user.getId())
        .setParameter("financialYear", Year.now().getValue())
        .setMaxResults(1)
        .getResultStream()
        .findAny()
        .isPresent();
  }

  @Override
  public void close() {
    // Note: Reference implementation does not close the EntityManager
    // https://github.com/keycloak/keycloak-quickstarts/blob/9f1470474af85a9ab3ac0ba64ed0cc68dbd2b921/user-storage-jpa/src/main/java/org/keycloak/quickstart/storage/user/MyUserStorageProvider.java#L92
  }
}
