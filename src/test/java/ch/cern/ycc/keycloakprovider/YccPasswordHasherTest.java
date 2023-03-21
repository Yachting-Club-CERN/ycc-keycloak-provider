package ch.cern.ycc.keycloakprovider;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class YccPasswordHasherTest {
  @ValueSource(
      strings = {
        "password1",
        "A Very Special % Password!!! It is also quite long, since things like this can totally happen"
      })
  @ParameterizedTest
  void testHash(String password) {
    // Given

    // When
    String passwordHash1 = YccPasswordHasher.hash(password);
    String passwordHash2 = YccPasswordHasher.hash(password);

    // Then
    assertThat(YccPasswordHasher.verify(password, passwordHash1)).isTrue();
    assertThat(YccPasswordHasher.verify("invalid password", passwordHash1)).isFalse();

    assertThat(YccPasswordHasher.verify(password, passwordHash2)).isTrue();
    assertThat(YccPasswordHasher.verify("invalid password", passwordHash2)).isFalse();
  }

  @CsvSource({
    "JCRAWFOR,{X-PBKDF2}HMACSHA1:AABOIA:aI2x1tpbi5Gy9t47p3TO2Q:ZczS/Rj2/C8.d81c0lZdATUew8E",
    "APHILLIP,{X-PBKDF2}HMACSHA1:AABOIA:IYSQshYCIERISYnxXstZ6w:oRx4uGApJiA25XFOrNb5q2TreJs",
    "DHARTMAN,{X-PBKDF2}HMACSHA1:AABOIA:hZASwlhrLUWolZLyHkNIyQ:/VAVfiSIGp/RCzhGp1QviPbGOZo",
    "KLOPEZ,{X-PBKDF2}HMACSHA1:AABOIA:WUsJwViL8R6DMAaA8H7POQ:uVPLo9c52uL9pRQm2p9HOOdo6Sw",
    "RWATSON,{X-PBKDF2}HMACSHA1:AABOIA:59wbA0DI.Z/TWotxDmHMuQ:N531OllgPC9YOdql7FleDiHmei4",
    "JAGUIRRE,{X-PBKDF2}HMACSHA1:AABOIA:jfEe45xz7j3HWMsZ43wvhQ:/gRT6PCFLGvotElZX/fkuKYBf0w",
    "WDIXON,{X-PBKDF2}HMACSHA1:AABOIA:7p0zBgAghFCKcY4xBqAUIg:ntHaqlhINaTG/.0gYE8wnLVwojg",
    "MCHAVEZ,{X-PBKDF2}HMACSHA1:AABOIA:wThHKMW4d66VkhICIKSUEg:VwVpmtiJQm5T9MB/Lp3P.2165sM",
    "BSHAFFER,{X-PBKDF2}HMACSHA1:AABOIA:tDZmbG1tLaXUOgfAGENojQ:VNHvUcrQ34pur7Ee/2QBw3Qp7RY",
    "PHALL,{X-PBKDF2}HMACSHA1:AABOIA:bi2FsNY6R0jp/Z8TYgzhfA:ckgciXthkkUarz8soAhKeZho3s8",
    "TCOOLEY,{X-PBKDF2}HMACSHA1:AABOIA:0lqL0RoDQKgVYmwtJaR0Dg:9YGhkh7tGKlyhJI.wmruZG5VFHE",
    "MEDWARDS,{X-PBKDF2}HMACSHA1:AABOIA:bE0JgXBujXGOcQ5ByJlzDg:jqeHyaIcdgwoqyge.CGJgWcyhyw",
    "AMENDOZA,{X-PBKDF2}HMACSHA1:AABOIA:UmoNgfAeA4CQ8p5zbo0xxg:UVYbjsgeIFYV0WZSa9jnUGoVmD4"
  })
  @ParameterizedTest
  void testVerifyValidPassword(String password, String passwordHash) {
    // Given

    // When
    boolean valid = YccPasswordHasher.verify(password, passwordHash);

    // Then
    assertThat(valid).isTrue();
  }

  @CsvSource({
    "not a good password,{X-PBKDF2}HMACSHA1:AABOIA:aI2x1tpbi5Gy9t47p3TO2Q:ZczS/Rj2/C8.d81c0lZdATUew8E",
    "another bad one,{X-PBKDF2}HMACSHA1:AABOIA:IYSQshYCIERISYnxXstZ6w:oRx4uGApJiA25XFOrNb5q2TreJs"
  })
  @ParameterizedTest
  void testVerifyInvalidPassword(String password, String passwordHash) {
    // Given

    // When
    boolean valid = YccPasswordHasher.verify(password, passwordHash);

    // Then
    assertThat(valid).isFalse();
  }
}
