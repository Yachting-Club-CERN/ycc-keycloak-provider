package ch.cern.ycc.keycloakprovider.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordVerifierTest {
  @CsvSource({
    "JCRAWFOR,{X-PBKDF2}HMACSHA1:AABOIA:Xd7KGbFaxX+X9hP3Zyx1WA==:YtFzL/bDaPJ9m4sV7Zm3NU11HKo=",
    "APHILLIP,{X-PBKDF2}HMACSHA1:AABOIA:ItVu9js2SYgOVa+7rX69VQ==:CRfOORZz1Gs19typFb04eGVwK64=",
    "JCRAWFOR,{X-PBKDF2}HMACSHA1:AABOIA:.3iPmRYGRLwrBIysnInciQ==:618DAiPPCs0Z5hoXGPo8smPZAwg=",
    "MEDWARDS,{X-PBKDF2}HMACSHA1:AABOIA:gf/YD0WIjnn0A4vnQ9jGcQ==:/lIV7.UDqdxO/okD/Bg4zZAl9.8=",
  })
  @ParameterizedTest
  void testVerifyValidLegacyPassword(String password, String passwordHash) {
    checkVerify(true, password, passwordHash);
  }

  @CsvSource({
    "not a good password,{X-PBKDF2}HMACSHA1:AABOIA:+3iPmRYGRLwrBIysnInciQ==:618DAiPPCs0Z5hoXGPo8smPZAwg=",
    "another bad one,{X-PBKDF2}HMACSHA1:AABOIA:Oly7YX19WjB0.bqa0sGd1w==:q1siuCguWhkBISBNHmYn/aswp0E=",
  })
  @ParameterizedTest
  void testVerifyInvalidLegacyPassword(String password, String passwordHash) {
    checkVerify(false, password, passwordHash);
  }

  @CsvSource({
    "password,pbkdf2-sha256:537000:1R5OirqAp3x0vjYWdmqNeg==:wHVXhibvBDD5rXQZZYlywSlvNxwWX1x/rCjgfiHTeNg=",
    "password,pbkdf2-sha256:537000:74J6KqLFTQab6Mb8XDBx7w==:zDjq03ZRIy1sK81VRcLv46A0Tnq6Js0um5eVnActbgM=",
    "password,pbkdf2-sha256:537000:1DrbyOvy3rOtenEJ4tUGow==:lqY2gaAlFuRbA6UwZNRdy4+NC4aOk1TaPKTBV94TqrA=",
    "PaSsWoRd123!@#,pbkdf2-sha256:537000:xZTg/2cP8LJIOVFv001S3w==:0ZyiiWm00NnD5ePRQNeEnMbspiZHle/GeDEmogLa0bg=",
    "PaSsWoRd123!@#,pbkdf2-sha256:537000:DBtlihHMCsEDtTweI0K/wg==:H79psEZ840ydfGbPmElUOzA2ay7N8IZcvLedhXoPtuo=",
    "PaSsWoRd123!@#,pbkdf2-sha256:537000:mPaIZsiYCN+140CDfnISmg==:hubYdtEp5gweSLKrMxBs7VGfiJ0R+2las5sRBpsdnts=",
  })
  @ParameterizedTest
  void testVerifyValidPassword(String password, String passwordHash) {
    checkVerify(true, password, passwordHash);
  }

  @CsvSource({
    "not a good password,pbkdf2-sha256:537000:N5BsLbQh73WOEEnPVOircw==:EoEaUrSEuFMGxwGG/5B42dXI+wIU3TvQO77sYD+PEWg=",
    "another bad one,pbkdf2-sha256:537000:z5KY0rLwkuU6j7fOBai8BA==:u3ybdLv3lPtO0snXrrgpiPmI4stAHrewED7VQ84TPKc=",
  })
  @ParameterizedTest
  void testVerifyInvalidPassword(String password, String passwordHash) {
    checkVerify(false, password, passwordHash);
  }

  @ValueSource(
      strings = {
        "totally invalid hash",
        "totally invalid hash:a",
        "totally invalid hash:a:b:c",
        // Legacy
        "{X-PBKDF2}HMACSHA1",
        "{X-PBKDF2}HMACSHA1:a",
        "{X-PBKDF2}HMACSHA1:a:b:c",
        // New
        "pbkdf2-sha256",
        "pBkDf2-sHa256:a",
        "pbkdf2-sha256:a:b:c",
      })
  @ParameterizedTest
  void testVerifyInvalidPasswordFormatDoesNotThrow(String passwordHash) {
    checkVerify(false, "password", passwordHash);
  }

  private static void checkVerify(boolean expected, String password, String passwordHash) {
    assertThat(PasswordVerifier.verify(password, passwordHash)).isEqualTo(expected);
  }
}
