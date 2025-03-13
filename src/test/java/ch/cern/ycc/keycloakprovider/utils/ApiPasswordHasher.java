package ch.cern.ycc.keycloakprovider.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility intended for only testing to hash passwords with the provided API.
 *
 * @author Lajos Cseppento
 */
class ApiPasswordHasher {
  // Not available as of 2025-02
  // private static final String API_URL = "https://yccres.app.cern.ch/api/v1/hash_password";
  private static final String API_URL = "https://yccres-dev2.app.cern.ch/api/v1/hash_password";

  record Payload(String password) {}

  private ApiPasswordHasher() {
    throw new UnsupportedOperationException();
  }

  /**
   * Hashes a password with the API.
   *
   * @param password password
   * @return hash
   */
  static String hash(String password) {
    var objectMapper = new ObjectMapper();

    try {
      var request =
          HttpRequest.newBuilder()
              .uri(URI.create(API_URL))
              .header("Content-Type", "application/json")
              .POST(
                  HttpRequest.BodyPublishers.ofString(
                      objectMapper.writeValueAsString(new Payload(password))))
              .build();

      var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new RuntimeException(
            String.format(
                "Password hashing failed with code %d: %s", response.statusCode(), response));
      }

      return objectMapper.readValue(response.body(), Payload.class).password();
    } catch (Exception ex) {
      throw new RuntimeException("Password hashing failed", ex);
    }
  }
}
