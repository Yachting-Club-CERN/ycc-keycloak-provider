package ch.cern.ycc.keycloakprovider;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * User entity demo. Start the local DB before running this program.
 *
 * @author Lajos Cseppento
 */
class YccUserEntityDemo {

  public static void main(String[] args) {
    // Start the local db
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ycc-db-local");
    EntityManager entityManager = emf.createEntityManager();

    System.out.println("> Users:");
    List<YccUserEntity> allUsers =
        entityManager
            .createQuery("SELECT u FROM YccUserEntity u", YccUserEntity.class)
            .getResultList();

    for (YccUserEntity user : allUsers) {
      System.out.println(user);
    }
    System.out.println();

    System.out.println("> User count:");
    int userCount =
        entityManager
            .createQuery("SELECT COUNT(u) FROM YccUserEntity u", Long.class)
            .getSingleResult()
            .intValue();
    System.out.println(userCount);
    System.out.println();

    System.out.println("> Search:");
    YccUserEntity searchResult =
        entityManager
            .createQuery(
                "SELECT u FROM YccUserEntity u WHERE lower(u.username)=lower(:usernameOrEmail) OR lower(u.email)=lower(:usernameOrEmail)",
                YccUserEntity.class)
            .setParameter("usernameOrEmail", "DHOGaN")
            .getResultStream()
            .findAny()
            .orElse(null);

    System.out.println(searchResult);
    System.out.println();

    System.out.println("> Search with null:");
    List<YccUserEntity> nullSearchResult =
        entityManager
            .createNamedQuery("YccUserEntity.search", YccUserEntity.class)
            .setParameter("search", null)
            .getResultStream()
            .toList();
    for (YccUserEntity user : nullSearchResult) {
      System.out.println(user);
    }
    System.out.println(nullSearchResult.size());
    System.out.println();
  }
}
