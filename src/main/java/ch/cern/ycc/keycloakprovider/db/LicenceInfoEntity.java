package ch.cern.ycc.keycloakprovider.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Represents a licence belonging to a member. Only the used fields are listed. */
@Entity
@Table(name = "INFOLICENCES")
@Getter
@Setter
@ToString
public class LicenceInfoEntity {
  /** Licence info ID. */
  @Id
  @Column(name = "INFOID")
  private long id;

  /**
   * Licence, e.g., "M". This is what we call "key" in the club, but this table has something else
   * defined as NKEY.
   */
  @Column(name = "NLICENCE")
  private String licence;
}
