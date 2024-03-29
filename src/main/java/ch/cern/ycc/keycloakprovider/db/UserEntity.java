package ch.cern.ycc.keycloakprovider.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a YCC user.
 *
 * @author Lajos Cseppento
 */
@Entity
@Table(name = "WEB_LOGON")
@SecondaryTable(name = "MEMBERS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
@SecondaryTable(
    name = "HELPERS_APP_PERMISSIONS",
    pkJoinColumns = @PrimaryKeyJoinColumn(name = "MEMBER_ID"))
@NamedQuery(name = "UserEntity.getCount", query = "SELECT COUNT(u) FROM UserEntity u")
@NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u ORDER BY u.username")
@NamedQuery(
    name = "UserEntity.findByUsername",
    query = "SELECT u FROM UserEntity u WHERE lower(u.username) = lower(:username)")
@NamedQuery(
    name = "UserEntity.findByEmail",
    query = "SELECT u FROM UserEntity u WHERE lower(u.email) = lower(:email)")
@NamedQuery(
    name = "UserEntity.findByUsernameOrEmail",
    query =
        "SELECT u FROM UserEntity u "
            + "WHERE lower(u.username) = lower(:usernameOrEmail) "
            + "OR lower(u.email) = lower(:usernameOrEmail)")
@NamedQuery(
    name = "UserEntity.search",
    query =
        "SELECT u FROM UserEntity u "
            + "WHERE lower(u.username) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.email) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.firstName) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.lastName) LIKE '%' || lower(:search) || '%' "
            + "ORDER BY u.username")
@NamedQuery(
    name = "UserEntity.searchByParameters",
    query =
        "SELECT u FROM UserEntity u "
            + "WHERE lower(u.username) LIKE '%' || :username || '%' "
            + "AND lower(u.email) LIKE '%' || :email || '%' "
            + "AND lower(u.firstName) LIKE '%' || :firstName || '%' "
            + "AND lower(u.lastName) LIKE '%' || :lastName || '%' "
            + "ORDER BY u.username")
@Data
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue
  @Column(name = "MEMBER_ID")
  private long id;

  @Column(name = "LOGON_ID")
  private String username;

  @Column(name = "LOGON_PASS2")
  private String password;

  @Column(table = "MEMBERS", name = "E_MAIL")
  private String email;

  @Column(table = "MEMBERS", name = "FIRSTNAME")
  private String firstName;

  @Column(table = "MEMBERS", name = "NAME")
  private String lastName;

  @Column(table = "MEMBERS", name = "MEMBERSHIP")
  private String membershipType;

  @Column(table = "HELPERS_APP_PERMISSIONS", name = "PERMISSION")
  private String helpersAppPermission;
}
