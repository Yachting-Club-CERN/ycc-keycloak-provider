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

  public static final String YCC_USERS_GROUP = "ycc-users";
  public static final String YCC_ACTIVE_MEMBER_ROLE = "ycc-active-member";
  public static final String YCC_INACTIVE_MEMBER_ROLE = "ycc-inactive-member";

  private Constants() {
    throw new UnsupportedOperationException();
  }
}
