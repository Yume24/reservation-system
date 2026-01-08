package com.ajaros.reservationsystem.auth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtility {
  private static final MessageDigest digest;

  static {
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String hash(String stringToHash) {
    var hash = digest.digest(stringToHash.getBytes());
    return Base64.getEncoder().encodeToString(hash);
  }
}
