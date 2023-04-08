package ch.cern.ycc.keycloakprovider.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a licence belonging to a member. Only the used fields are listed. */
@Entity
@Table(name = "INFOLICENCES")
@Data
@NoArgsConstructor
// Serializable: referred by LicenceEntity, which is Serializable
public class LicenceInfoEntity implements Serializable {
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
