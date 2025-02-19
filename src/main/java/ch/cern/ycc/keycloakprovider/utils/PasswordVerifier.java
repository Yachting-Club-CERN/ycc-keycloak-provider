package ch.cern.ycc.keycloakprovider.utils;

import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * Use this class for verifying YCC passwords. Supports both the legacy YCC Perl/APEX compatible
 * passwords and the new ones from 2025.
 *
 * @author Lajos Cseppento
 */
@Slf4j
public final class PasswordVerifier {
  private static final String HASH_PREFIX = "pbkdf2-sha256";
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

  private static final SecretKeyFactory SECRET_KEY_FACTORY;

  private PasswordVerifier() {
    throw new UnsupportedOperationException();
  }

  static {
    try {
      SECRET_KEY_FACTORY = SecretKeyFactory.getInstance(ALGORITHM);
    } catch (GeneralSecurityException ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

  /**
   * Verifies a password.
   *
   * @param password password
   * @param passwordHash hash
   * @return <code>true</code> if valid, otherwise <code>false</code>
   */
  public static boolean verify(String password, String passwordHash) {
    try {
      if (passwordHash.startsWith(LegacyPasswordHasher.HASH_PREFIX)) {
        return LegacyPasswordHasher.verify(password, passwordHash);
      }

      // New format:
      // pbkdf2-sha256:537000:k6FW7rVieiyclK7jeGkQeg==:FMU33HdMfi/RpQP0Iumtqz4PDUMthyUkXGAB+P5TJhg=
      String[] parts = passwordHash.split(":");
      if (parts.length != 4) {
        throw new IllegalArgumentException("Invalid hash: " + passwordHash);
      } else if (!parts[0].equalsIgnoreCase(HASH_PREFIX)) {
        throw new IllegalArgumentException("Unsupported hash: " + passwordHash);
      }

      int iterations = Integer.parseInt(parts[1]);
      byte[] salt = Base64.getDecoder().decode(parts[2]);
      byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
      byte[] computedHash = hash(password, salt, iterations, expectedHash.length);

      return constantTimeEquals(expectedHash, computedHash);
    } catch (Exception ex) {
      // Psst! Don't tell anyone that the password format is invalid
      log.warn("Failed to verify password, hash: {}", passwordHash, ex);
      return false;
    }
  }

  private static byte[] hash(String password, byte[] salt, int iterations, int keyLength)
      throws GeneralSecurityException {
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength * 8);
    return SECRET_KEY_FACTORY.generateSecret(spec).getEncoded();
  }

  // Constant-time comparison to prevent timing attacks
  // Exposed for LegacyPasswordHasher
  static boolean constantTimeEquals(byte[] a, byte[] b) {
    if (a.length != b.length) {
      return false;
    }

    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }
}
