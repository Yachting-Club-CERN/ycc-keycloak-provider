package ch.cern.ycc.keycloakprovider.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User repository demo.
 *
 * @author Lajos Cseppento
 */
public class UserRepositoryDemo {
  public static void main(String[] args) {
    EntityManagerFactory entityManagerFactory =
        Persistence.createEntityManagerFactory("ycc-db-local");

    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      demo(entityManager);
    }
  }

  private static void demo(EntityManager entityManager) {
    var repository = new UserRepository(entityManager);

    System.out.println("User 1: " + repository.findById(1));
    System.out.println("User 12345678: " + repository.findById(12345678));
    System.out.println();

    Map<Boolean, List<MemberInfo>> memberInfosByActivity =
        repository
            .findAll()
            .map(
                user ->
                    new MemberInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getMembershipType(),
                        repository.isActiveMember(user),
                        repository.findActiveLicences(user)))
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

  private record MemberInfo(
      long id,
      String username,
      String membershipType,
      boolean active,
      Collection<String> activeLicences) {}
}
