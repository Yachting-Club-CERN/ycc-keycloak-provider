package ch.cern.ycc.keycloakprovider;

/**
 * Constants.
 *
 * @author Lajos Cseppento
 */
public final class Constants {
  public static final String ID = "YCC Oracle Users";
  public static final String HELP_TEXT =
      "This provider is able to federate users from the YCC Oracle Database";
  public static final String DATA_SOURCE_PROPERTY_NAME = "dataSource";

  public static final String YCC_ALL_MEMBERS = "ycc-members-all-past-and-present";
  public static final String YCC_ACTIVE_MEMBER_ROLE = "ycc-member-active";
  public static final String YCC_INACTIVE_MEMBER_ROLE = "ycc-member-inactive";

  private Constants() {
    throw new UnsupportedOperationException();
  }
}
