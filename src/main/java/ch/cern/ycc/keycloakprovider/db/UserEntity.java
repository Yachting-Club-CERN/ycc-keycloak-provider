package ch.cern.ycc.keycloakprovider.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a YCC user.
 *
 * @author Lajos Cseppento
 */
@Entity
@Table(name = "WEB_LOGON")
@SecondaryTable(name = "MEMBERS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
@NamedQuery(name = "UserEntity.getCount", query = "SELECT COUNT(u) FROM UserEntity u")
@NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u ORDER BY u.username")
@NamedQuery(
    name = "UserEntity.findByUsername",
    query = "SELECT u FROM UserEntity u " + "WHERE lower(u.username) = lower(:username)")
@NamedQuery(
    name = "UserEntity.findByEmail",
    query = "SELECT u FROM UserEntity u " + "WHERE lower(u.email) = lower(:email)")
@NamedQuery(
    name = "UserEntity.findByUsernameOrEmail",
    query =
        "SELECT u FROM UserEntity u "
            + "WHERE lower(u.username) = lower(:usernameOrEmail) OR lower(u.email) = lower(:usernameOrEmail)")
@NamedQuery(
    name = "UserEntity.search",
    query =
        "SELECT u from UserEntity u "
            + "WHERE lower(u.username) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.email) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.firstName) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.lastName) LIKE '%' || lower(:search) || '%' "
            + "ORDER BY u.username")
@NamedQuery(
    name = "UserEntity.searchByParameters",
    query =
        "SELECT u from UserEntity u "
            + "WHERE lower(u.username) LIKE '%' || :username || '%' "
            + "AND lower(u.email) LIKE '%' || :email || '%' "
            + "AND lower(u.firstName) LIKE '%' || :firstName || '%' "
            + "AND lower(u.lastName) LIKE '%' || :lastName || '%' "
            + "ORDER BY u.username")
@Getter
@Setter
@ToString
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
}
