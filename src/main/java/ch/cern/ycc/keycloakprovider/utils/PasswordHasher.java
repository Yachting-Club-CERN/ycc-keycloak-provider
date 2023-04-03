package ch.cern.ycc.keycloakprovider.utils;

import ch.cern.ycc.keycloakprovider.YccKeycloakProviderException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import lombok.NonNull;

/**
 * "YCC Perl" and APEX compatible password hashing.
 *
 * @author Lajos Cseppento
 */
public final class PasswordHasher {
  private static final String HASH_PREFIX = "{X-PBKDF2}HMACSHA1";
  private static final int PBKDF2_ITERATIONS = 20000;
  private static final int SALT_BYTE_SIZE = 16;
  private static final int HASH_BIT_SIZE = 160; // 20 bytes

  private static final SecretKeyFactory SECRET_KEY_FACTORY;

  static {
    try {
      SECRET_KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (Exception ex) {
      throw new YccKeycloakProviderException("Failed to create secret key factory", ex);
    }
  }

  private PasswordHasher() {
    throw new UnsupportedOperationException();
  }

  /**
   * Hashes a password.
   *
   * @param password password
   * @return hash
   */
  public static String hash(@NonNull String password) {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_BYTE_SIZE];
    random.nextBytes(salt);
    return hash(password, PBKDF2_ITERATIONS, salt);
  }

  private static String hash(@NonNull String password, int iterations, @NonNull byte[] salt) {
    try {
      PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BIT_SIZE);
      byte[] hash = SECRET_KEY_FACTORY.generateSecret(spec).getEncoded();
      return String.format(
              "%s:%s:%s:%s",
              HASH_PREFIX,
              // Legacy code removes padding
              packInt(PBKDF2_ITERATIONS).replace("=", ""),
              base64Encode(salt),
              base64Encode(hash))
          .replace(".", "+");
    } catch (Exception ex) {
      throw new YccKeycloakProviderException("Password hashing failed", ex);
    }
  }

  /**
   * Verifies a password.
   *
   * @param password password
   * @param passwordHash hash
   * @return <code>true</code> if valid, otherwise <code>false</code>
   */
  public static boolean verify(@NonNull String password, @NonNull String passwordHash) {
    String[] parts = passwordHash.split(":", 4);
    if (parts.length != 4) {
      throw new IllegalArgumentException("Invalid hash: " + passwordHash);
    } else if (!HASH_PREFIX.equals(parts[0])) {
      throw new IllegalArgumentException("Unsupported hash: " + passwordHash);
    }

    int iterations = unpackInt(parts[1] + "=="); // Legacy code removes padding
    byte[] salt = base64Decode(parts[2]);

    // Definitely one could do it better
    String hash = sanitise(passwordHash);
    String hashNew = sanitise(hash(password, iterations, salt));
    return hash.equals(hashNew);
  }

  private static String sanitise(@NonNull String value) {
    // The data is not always consistent on '.' <=> '+' and '=' <=> '', so we are consistently
    // removing them in this code to avoid that members cannot log in.
    return value.replace("+", ".").replace("=", "");
  }

  private static String packInt(int value) {
    ByteBuffer bytes = ByteBuffer.allocate(Integer.BYTES);
    bytes.putInt(value);
    return base64Encode(bytes.array());
  }

  private static int unpackInt(@NonNull String value) {
    try {
      byte[] bytes = base64Decode(value);
      return ByteBuffer.wrap(bytes).getInt();
    } catch (Exception ex) {
      throw new IllegalArgumentException("Cannot unpack integer from value: " + value, ex);
    }
  }

  private static String base64Encode(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  private static byte[] base64Decode(String str) {
    // From LDAP format if needed
    return Base64.getDecoder().decode(str.replace('.', '+'));
  }
}
