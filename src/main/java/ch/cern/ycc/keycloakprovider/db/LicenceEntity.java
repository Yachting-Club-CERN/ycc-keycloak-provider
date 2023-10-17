package ch.cern.ycc.keycloakprovider.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a licence belonging to a member. Only the used fields are listed. */
@Entity
@Table(name = "LICENCES")
@NamedQuery(
    name = "LicenceEntity.findAllActiveByMemberId",
    query = "SELECT l FROM LicenceEntity l WHERE l.memberId = :memberId AND l.status > 0")
@Data
@NoArgsConstructor
public class LicenceEntity implements Serializable {
  /** Member ID. */
  @Id
  @Column(name = "MEMBER_ID")
  private long memberId;

  /** Licence info ID. */
  @Id
  @Column(name = "LICENCE_ID")
  private long licenceInfoId;

  /** Licence info. */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "LICENCE_ID")
  private LicenceInfoEntity licenceInfo;

  /**
   * Key status, active=1, inactive=0. Other values/null is not present in the database as of 2023.
   */
  @Column(name = "STATUS")
  private Integer status;
}
