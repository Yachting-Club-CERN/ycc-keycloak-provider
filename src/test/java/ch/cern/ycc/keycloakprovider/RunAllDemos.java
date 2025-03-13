package ch.cern.ycc.keycloakprovider;

import ch.cern.ycc.keycloakprovider.db.UserEntityDemo;
import ch.cern.ycc.keycloakprovider.db.UserRepositoryDemo;
import ch.cern.ycc.keycloakprovider.utils.ApiPasswordHasherDemo;

/**
 * Handy script to run all demos to check for no exceptions. Of course running an Oracle XE instance
 * on GitHub Actions would be much better, but #NoTime.
 *
 * @author Lajos Cseppento
 */
public class RunAllDemos {
  public static void main(String[] args) {
    UserEntityDemo.main(args);
    UserRepositoryDemo.main(args);
    ApiPasswordHasherDemo.main(args);
  }
}
