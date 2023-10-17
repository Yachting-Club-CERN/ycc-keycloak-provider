package ch.cern.ycc.keycloakprovider.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a YCC fee record (membership fee, course fee etc. paid by a user).
 *
 * <p>Note: this class has an inline {@link jakarta.persistence.IdClass} to create a code-only
 * primary key over all fields. Thus, this class was also made {@link Serializable}.
 */
@Entity
@Table(name = "FEESRECORDS")
@NamedQuery(
    name = "FeeRecordEntity.findAllByMemberIdAndFinancialYear",
    query =
        "SELECT fr from FeeRecordEntity fr "
            + "WHERE fr.memberId = :memberId AND financialYear = :financialYear")
@Data
@NoArgsConstructor
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
