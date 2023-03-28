package ch.cern.ycc.keycloakprovider.db;

import ch.cern.ycc.keycloakprovider.Constants;
import ch.cern.ycc.keycloakprovider.utils.PasswordHasher;
import java.time.Year;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import lombok.NonNull;

/**
 * Keycloak repository for YCC users.
 *
 * @author Lajos Cseppento
 */
public class UserRepository {
  private final EntityManager entityManager;

  /**
   * Constructor.
   *
   * @param entityManager entity manager
   */
  public UserRepository(@NonNull EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Queries user count.
   *
   * @return user count
   */
  public int getCount() {
    return entityManager
        .createNamedQuery("UserEntity.getCount", Long.class)
        .getSingleResult()
        .intValue();
  }

  /**
   * Queries all users.
   *
   * @return stream of all users
   */
  public Stream<UserEntity> findAll() {
    return entityManager.createNamedQuery("UserEntity.findAll", UserEntity.class).getResultStream();
  }

  /**
   * Queries a user by id.
   *
   * @param id id
   * @return user or <code>null</code>
   */
  @Nullable
  public UserEntity findById(long id) {
    return entityManager.find(UserEntity.class, id);
  }

  /**
   * Queries a user by username.
   *
   * @param username username
   * @return user or <code>null</code>
   */
  @Nullable
  public UserEntity findByUsername(String username) {
    return entityManager
        .createNamedQuery("UserEntity.findByUsername", UserEntity.class)
        .setParameter("username", username)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  /**
   * Queries a user by e-mail address.
   *
   * @param email e-mail address
   * @return user or <code>null</code>
   */
  @Nullable
  public UserEntity findByEmail(String email) {
    return entityManager
        .createNamedQuery("UserEntity.findByEmail", UserEntity.class)
        .setParameter("email", email)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  /**
   * Queries a user by username or e-mail address.
   *
   * @param usernameOrEmail username or e-mail address
   * @return user or <code>null</code>
   */
  @Nullable
  public UserEntity findByUsernameOrEmail(String usernameOrEmail) {
    return entityManager
        .createNamedQuery("UserEntity.findByUsernameOrEmail", UserEntity.class)
        .setParameter("usernameOrEmail", usernameOrEmail)
        .getResultStream()
        .findAny()
        .orElse(null);
  }

  /**
   * Searches users.
   *
   * @param search search term
   * @return result stream
   */
  public Stream<UserEntity> search(String search) {
    return entityManager
        .createNamedQuery("UserEntity.search", UserEntity.class)
        .setParameter("search", search)
        .getResultStream();
  }

  /**
   * Searches users.
   *
   * @param username username
   * @param email e-mail address
   * @param firstName first name
   * @param lastName last name
   * @return result stream
   */
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

  /**
   * Verifies a user's password.
   *
   * @param username username
   * @param password password
   * @return <code>true</code> if the user exists and the password is valid, otherwise <code>false
   *     </code>
   */
  public boolean verifyPassword(@NonNull String username, @NonNull String password) {
    return PasswordHasher.verify(password, findByUsernameOrEmail(username).getPassword());
  }

  /**
   * Queries whether a user is an active member.
   *
   * @param user user
   * @return <code>true</code> if the user is an active member, otherwise <code>false</code>
   */
  public boolean isActiveMember(@NonNull UserEntity user) {
    if (Constants.HONORARY_MEMBERSHIP_TYPE.equals(user.getMembershipType())) {
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
}
