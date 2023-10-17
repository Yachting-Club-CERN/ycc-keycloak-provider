package ch.cern.ycc.keycloakprovider;

/**
 * Constants.
 *
 * @author Lajos Cseppento
 */
public final class Constants {
  /** Provider ID. If changed you need to reconfigure all realms. */
  public static final String ID = "YCC Oracle Users";

  /** Provider help text. */
  public static final String HELP_TEXT =
      "This provider is able to federate users from the YCC Oracle Database";

  /** Code of the honorary membership type. */
  public static final String YCC_HONORARY_MEMBERSHIP_TYPE = "H";

  /**
   * Name of the group containing all members, past and present. Changing this value may trigger a
   * change in some clients.
   */
  public static final String YCC_ALL_MEMBERS_GROUP = "ycc-members-all-past-and-present";

  /**
   * Name of the role containing active members (paid membership fee for this year or honorary
   * member). Changing this triggers a change in all clients.
   */
  public static final String YCC_ACTIVE_MEMBER_ROLE = "ycc-member-active";

  /**
   * Prefix of the roles containing active members with a specific licence. E.g., "ycc-licence-m"
   * for the M key.
   */
  public static final String YCC_LICENCE_ROLE_PREFIX = "ycc-licence-";

  /** Prefix for Helpers App roles. */
  public static final String YCC_HELPERS_APP_ROLE_PREFIX = "ycc-helpers-app-";

  private Constants() {
    throw new UnsupportedOperationException();
  }
}
