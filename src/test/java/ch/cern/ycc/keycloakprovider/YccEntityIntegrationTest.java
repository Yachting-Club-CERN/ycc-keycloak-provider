package ch.cern.ycc.keycloakprovider;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class YccEntityIntegrationTest {

  YccEntityIntegrationTest() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ycc-db-local");
    EntityManager entityManager = emf.createEntityManager();
    //
    //    CriteriaBuilder cb =entityManager.getCriteriaBuilder();
    //    CriteriaQuery<YccUserEntity> cq = cb.createQuery(YccUserEntity.class);
    //    Root<YccUserEntity> rootEntry = cq.from(YccUserEntity.class);
    //    CriteriaQuery<YccUserEntity> all = cq.select(rootEntry);

    //    TypedQuery<YccUserEntity> allQuery = entityManager.createQuery(all);
    //    List<YccUserEntity> res = allQuery.getResultList();

    List<YccUserEntity> res =
        entityManager
            .createQuery("SELECT u FROM YccUserEntity u", YccUserEntity.class)
            .getResultList();

    for (YccUserEntity re : res) {
      System.out.println(re);
    }

    int res2 =
        entityManager
            .createQuery("SELECT COUNT(u) FROM YccUserEntity u", Long.class)
            .getSingleResult()
            .intValue();
    System.out.println(res2);

    YccUserEntity res3 =
        entityManager
            .createQuery(
                "SELECT u FROM YccUserEntity u WHERE lower(u.username)=lower(:usernameOrEmail) OR lower(u.email)=lower(:usernameOrEmail)",
                YccUserEntity.class)
            .setParameter("usernameOrEmail", "DHOGaN")
            .getResultStream()
            .findAny()
            .orElse(null);

    System.out.println(res3);

    String search = null;
    List<YccUserEntity> res4 =
        entityManager
            .createNamedQuery("YccUserEntity.search", YccUserEntity.class)
            .setParameter("search", search)
            .getResultStream()
            .toList();
    for (YccUserEntity re : res4) {
      System.out.println(re);
    }
    System.out.println(res4.size());
  }

  public static void main(String[] args) {
    new YccEntityIntegrationTest();
    //
  }
}
