package ch.cern.ycc.keycloakprovider.utils;

/**
 * API password hasher demo.
 *
 * @author Lajos Cseppento
 */
public class ApiPasswordHasherDemo {
  public static void main(String[] args) {
    System.out.println("> Hashing passwords...");
    String[] passwords = {"password", "PaSsWoRd123!@#"};

    for (String password : passwords) {
      System.out.println("> " + password);
      System.out.println(ApiPasswordHasher.hash(password));
      System.out.println(ApiPasswordHasher.hash(password));
      System.out.println(ApiPasswordHasher.hash(password));
    }

    System.out.println();
  }
}
