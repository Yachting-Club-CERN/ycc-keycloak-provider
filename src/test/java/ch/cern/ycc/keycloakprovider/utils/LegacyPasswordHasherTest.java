package ch.cern.ycc.keycloakprovider.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class LegacyPasswordHasherTest {
  @ValueSource(
      strings = {
        "password1",
        "A Very Special % Password!!! It is also quite long, since things like this can totally happen"
      })
  @ParameterizedTest
  void testHash(String password) {
    // Given

    // When
    String passwordHash1 = LegacyPasswordHasher.hash(password);
    String passwordHash2 = LegacyPasswordHasher.hash(password);

    // Then
    checkVerify(true, password, passwordHash1);
    checkVerify(false, "invalid password", passwordHash1);

    checkVerify(true, password, passwordHash2);
    checkVerify(false, "invalid password", passwordHash2);
  }

  @CsvSource({
    "JCRAWFOR,{X-PBKDF2}HMACSHA1:AABOIA:Xd7KGbFaxX+X9hP3Zyx1WA==:YtFzL/bDaPJ9m4sV7Zm3NU11HKo=",
    "APHILLIP,{X-PBKDF2}HMACSHA1:AABOIA:ItVu9js2SYgOVa+7rX69VQ==:CRfOORZz1Gs19typFb04eGVwK64=",
    "DHARTMAN,{X-PBKDF2}HMACSHA1:AABOIA:Pqk4UehGqAKGTAT9NvubMA==:UuT5/Aj0+RboxuSlr2YWY1+LrWc=",
    "KLOPEZ,{X-PBKDF2}HMACSHA1:AABOIA:YBBNdlHkXXiVrrk59QjCZQ==:a0At5GmF1epbOrwSk8pVusIMJLQ=",
    "RWATSON,{X-PBKDF2}HMACSHA1:AABOIA:nA0mqEv6MrEdC4NulI9o8w==:rxN1dAiwk9f5P4xhiGg2aDrKmjE=",
    "JAGUIRRE,{X-PBKDF2}HMACSHA1:AABOIA:k3Ivwip0Y58uws8e4CNB5g==:gKYRdH67FNeznjSGTKDE648jXJI=",
    "WDIXON,{X-PBKDF2}HMACSHA1:AABOIA:fsbiaN+pNkWcPVUumMwdTA==:Cod6Ka98g0CdjfH7OZFbRLgY0kk=",
    "MCHAVEZ,{X-PBKDF2}HMACSHA1:AABOIA:Nk/IpPpA6ubE7x8UgvkXvw==:1F7KigLbikRFw7q6FzN9f0yAMbA=",
    "BSHAFFER,{X-PBKDF2}HMACSHA1:AABOIA:6tUg48WtZkCmniTD1fXh1w==:lZ5EwLkYJHlJB80v/rMC7MxrNgM=",
    "PHALL,{X-PBKDF2}HMACSHA1:AABOIA:tO2RPi8/ZGSOieIhBMnMiQ==:Z7J95KJbGLnnO+BpBRU8tZ8hWLM=",
    "TCOOLEY,{X-PBKDF2}HMACSHA1:AABOIA:n0XsY+yTQFptbffqh34Emw==:wBwCPeDnclZPMIxaZGjz2qu/vZI=",
    "MEDWARDS,{X-PBKDF2}HMACSHA1:AABOIA:tDqJtWKPlN/OuEAdeZcUxA==:dFvTsHlS/QeqJ01l9D5/Srdo1NA=",
    "AMENDOZA,{X-PBKDF2}HMACSHA1:AABOIA:UN/UU5f230G+apaZAK/gNw==:4XPn8MAE7zrhq1j6WmFBzHle+Mk=",
  })
  @ParameterizedTest
  void testVerifyValidPassword(String password, String passwordHash) {
    checkVerify(true, password, passwordHash);
  }

  @CsvSource({
    "JCRAWFOR,{X-PBKDF2}HMACSHA1:AABOIA:.3iPmRYGRLwrBIysnInciQ==:618DAiPPCs0Z5hoXGPo8smPZAwg=",
    "WDIXON,{X-PBKDF2}HMACSHA1:AABOIA:Oly7YX19WjB0.bqa0sGd1w==:q1siuCguWhkBISBNHmYn/aswp0E=",
    "MCHAVEZ,{X-PBKDF2}HMACSHA1:AABOIA:fb9mms8xufc4kavXsXVHtg==:6tHcAuBbtFlAr/D.FfV0Oj4ghS0=",
    "MEDWARDS,{X-PBKDF2}HMACSHA1:AABOIA:gf/YD0WIjnn0A4vnQ9jGcQ==:/lIV7.UDqdxO/okD/Bg4zZAl9.8=",
  })
  @ParameterizedTest
  void testVerifyValidPasswordWithDots(String password, String passwordHash) {
    checkVerify(true, password, passwordHash);
  }

  @CsvSource({
    "not a good password,{X-PBKDF2}HMACSHA1:AABOIA:+3iPmRYGRLwrBIysnInciQ==:618DAiPPCs0Z5hoXGPo8smPZAwg=",
    "another bad one,{X-PBKDF2}HMACSHA1:AABOIA:Oly7YX19WjB0.bqa0sGd1w==:q1siuCguWhkBISBNHmYn/aswp0E=",
  })
  @ParameterizedTest
  void testVerifyInvalidPassword(String password, String passwordHash) {
    checkVerify(false, password, passwordHash);
  }

  private static void checkVerify(boolean expected, String password, String passwordHash) {
    assertThat(LegacyPasswordHasher.verify(password, passwordHash)).isEqualTo(expected);
  }
}
