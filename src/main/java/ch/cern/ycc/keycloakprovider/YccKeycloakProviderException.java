package ch.cern.ycc.keycloakprovider;

/**
 * Exception type for YCC Keycloak Provider.
 *
 * @author Lajos Cseppento
 */
public class YccKeycloakProviderException extends RuntimeException {

  /**
   * Default constructor.
   *
   * @param message message
   */
  public YccKeycloakProviderException(String message) {
    super(message);
  }

  /**
   * Default constructor.
   *
   * @param message message
   * @param cause cause
   */
  public YccKeycloakProviderException(String message, Throwable cause) {
    super(message, cause);
  }
}
