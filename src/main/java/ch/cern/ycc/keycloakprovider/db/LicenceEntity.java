package ch.cern.ycc.keycloakprovider.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a licence belonging to a member. Only the used fields are listed. */
@Entity
@Table(name = "LICENCES")
@NamedQuery(
    name = "LicenceEntity.findAllActiveByMemberId",
    query = "SELECT l from LicenceEntity l WHERE l.memberId = :memberId AND STATUS > 0")
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

  /** Key status, active/inactive, nullable. */
  @Column(name = "STATUS")
  private Boolean status;
}
