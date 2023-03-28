package ch.cern.ycc.keycloakprovider.db;

import java.io.Serializable;
import java.time.LocalDate;
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
  /** Member ID. */
  @Id
  @Column(name = "MEMBER_ID")
  private long memberId;

  /** Financial year, 4-digit. */
  @Id
  @Column(name = "YEAR_F")
  private int financialYear;

  /** Payment date. */
  @Id
  @Column(name = "PAID_DATE")
  private LocalDate paymentDate;

  /** Payment mode, usually "UBS". */
  @Id
  @Column(name = "PAID_MODE")
  private String paymentMode;

  /** Fee amount paid. */
  @Id
  @Column(name = "FEE")
  private int fee;

  /** Date when the fee record was entered. */
  @Id
  @Column(name = "ENTERED_DATE")
  private ZonedDateTime entryDate;

  /** Payment ID. */
  @Id
  @Column(name = "PAYMENTID")
  private int paymentId;
}
