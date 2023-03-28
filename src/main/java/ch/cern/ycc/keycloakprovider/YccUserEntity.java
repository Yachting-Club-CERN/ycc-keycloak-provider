package ch.cern.ycc.keycloakprovider;

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
@NamedQuery(name = "YccUserEntity.getCount", query = "SELECT COUNT(u) FROM YccUserEntity u")
@NamedQuery(
    name = "YccUserEntity.findAll",
    query = "SELECT u FROM YccUserEntity u ORDER BY u.username")
@NamedQuery(
    name = "YccUserEntity.findByUsername",
    query = "SELECT u FROM YccUserEntity u " + "WHERE lower(u.username) = lower(:username)")
@NamedQuery(
    name = "YccUserEntity.findByEmail",
    query = "SELECT u FROM YccUserEntity u " + "WHERE lower(u.email) = lower(:email)")
@NamedQuery(
    name = "YccUserEntity.findByUsernameOrEmail",
    query =
        "SELECT u FROM YccUserEntity u "
            + "WHERE lower(u.username) = lower(:usernameOrEmail) OR lower(u.email) = lower(:usernameOrEmail)")
@NamedQuery(
    name = "YccUserEntity.search",
    query =
        "SELECT u from YccUserEntity u "
            + "WHERE lower(u.username) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.email) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.firstName) LIKE '%' || lower(:search) || '%' "
            + "OR lower(u.lastName) LIKE '%' || lower(:search) || '%' "
            + "ORDER BY u.username")
@NamedQuery(
    name = "YccUserEntity.searchByParameters",
    query =
        "SELECT u from YccUserEntity u "
            + "WHERE lower(u.username) LIKE '%' || :username || '%' "
            + "AND lower(u.email) LIKE '%' || :email || '%' "
            + "AND lower(u.firstName) LIKE '%' || :firstName || '%' "
            + "AND lower(u.lastName) LIKE '%' || :lastName || '%' "
            + "ORDER BY u.username")
@Getter
@Setter
@ToString
public class YccUserEntity {

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
}
