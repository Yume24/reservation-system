package com.ajaros.reservationsystem.auth.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtility {
  public static String hash(String stringToHash) {
    try {
      var digest = MessageDigest.getInstance("SHA-256");
      var hash = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 algorithm not found", e);
    }
  }
}
