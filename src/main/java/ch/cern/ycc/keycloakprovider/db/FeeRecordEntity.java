package ch.cern.ycc.keycloakprovider.db;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a YCC fee record (membership fee, course fee etc. paid by a user).
 *
 * <p>Note: this class has an inline {@link javax.persistence.IdClass} to create a code-only primary
 * key over all fields. Thus this class was also made {@link Serializable}.
 */
@Entity
@Table(name = "FEESRECORDS")
// @IdClass(FeeRecordEntityId.class) // Code-only primary key
@Getter
@Setter
@ToString
@NamedQuery(
    name = "FeeRecordEntity.findAllByMemberIdAndFinancialYear",
    query =
        "SELECT fr from FeeRecordEntity fr "
            + "WHERE fr.memberId = :memberId AND financialYear = :financialYear")
public class FeeRecordEntity implements Serializable {
  @Id
  @Column(name = "MEMBER_ID")
  private long memberId;

  @Id
  @Column(name = "YEAR_F")
  private int financialYear;

  @Id
  @Column(name = "PAID_DATE")
  private ZonedDateTime paymentDate;

  @Id
  @Column(name = "PAID_MODE")
  private String paymentMode;

  @Id
  @Column(name = "FEE")
  private int fee;

  @Id
  @Column(name = "ENTERED_DATE")
  private ZonedDateTime entryDate;

  @Id
  @Column(name = "PAYMENTID")
  private int paymentId;
}
