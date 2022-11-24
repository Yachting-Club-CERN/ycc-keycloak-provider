package ch.cern.ycc.keycloakprovider;

/**
 * Exception type for YCC Keycloak Provider.
 *
 * @author Lajos Cseppento
 */
public class YccKeycloakProviderException extends RuntimeException {
  public YccKeycloakProviderException(String message) {
    super(message);
  }

  public YccKeycloakProviderException(String message, Throwable cause) {
    super(message, cause);
  }
}
