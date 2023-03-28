package ch.cern.ycc.keycloakprovider.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * User entity demo. Start the local DB before running this program.
 *
 * @author Lajos Cseppento
 */
public class UserEntityDemo {
  public static void main(String[] args) {
    EntityManagerFactory entityManagerFactory =
        Persistence.createEntityManagerFactory("ycc-db-local");
    EntityManager entityManager = null;

    try {
      entityManager = entityManagerFactory.createEntityManager();
      demo(entityManager);
    } finally {
      if (entityManager != null) {
        entityManager.close();
      }
    }
  }

  private static void demo(EntityManager entityManager) {
    System.out.println("> Users:");
    List<UserEntity> allUsers =
        entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();

    for (UserEntity user : allUsers) {
      System.out.println(user);
    }
    System.out.println();

    System.out.println("> User count:");
    int userCount =
        entityManager
            .createQuery("SELECT COUNT(u) FROM UserEntity u", Long.class)
            .getSingleResult()
            .intValue();
    System.out.println(userCount);
    System.out.println();

    System.out.println("> Search:");
    UserEntity searchResult =
        entityManager
            .createQuery(
                "SELECT u FROM UserEntity u WHERE lower(u.username)=lower(:usernameOrEmail) OR lower(u.email)=lower(:usernameOrEmail)",
                UserEntity.class)
            .setParameter("usernameOrEmail", "DHOGaN")
            .getResultStream()
            .findAny()
            .orElse(null);

    System.out.println(searchResult);
    System.out.println();

    System.out.println("> Search with null:");
    List<UserEntity> nullSearchResult =
        entityManager
            .createNamedQuery("UserEntity.search", UserEntity.class)
            .setParameter("search", null)
            .getResultStream()
            .toList();
    for (UserEntity user : nullSearchResult) {
      System.out.println(user);
    }
    System.out.println(nullSearchResult.size());
    System.out.println();
  }
}
