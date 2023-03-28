package ch.cern.ycc.keycloakprovider.db;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Persistence;

/**
 * User repository demo.
 *
 * @author Lajos Cseppento
 */
class UserRepositoryDemo {
  public static void main(String[] args) {
    var entityManagerFactory = Persistence.createEntityManagerFactory("ycc-db-local");
    var entityManager = entityManagerFactory.createEntityManager();
    var repository = new UserRepository(entityManager);

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
