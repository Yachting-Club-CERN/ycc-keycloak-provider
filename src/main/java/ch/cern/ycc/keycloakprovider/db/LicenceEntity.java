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
