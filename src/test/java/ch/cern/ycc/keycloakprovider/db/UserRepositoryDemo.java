package ch.cern.ycc.keycloakprovider.db;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * User repository demo.
 *
 * @author Lajos Cseppento
 */
public class UserRepositoryDemo {
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
    var repository = new UserRepository(entityManager);

    System.out.println("User 1: " + repository.findById(1));
    System.out.println("User 12345678: " + repository.findById(12345678));

    Map<Boolean, List<MemberInfo>> memberInfosByActivity =
        repository
            .findAll()
            .map(
                user ->
                    new MemberInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getMembershipType(),
                        repository.isActiveMember(user)))
            .collect(Collectors.groupingBy(MemberInfo::active));

    System.out.println("> Active members:");
    for (MemberInfo memberInfo : memberInfosByActivity.get(true)) {
      System.out.println(memberInfo);
    }
    System.out.println();

    System.out.println("> Inactive members:");
    for (MemberInfo memberInfo : memberInfosByActivity.get(false)) {
      System.out.println(memberInfo);
    }
    System.out.println();
  }

  private record MemberInfo(long id, String username, String membershipType, boolean active) {}
}
