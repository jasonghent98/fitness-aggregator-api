package com.jasonghent98.fitness_aggregator_api.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PkceUtil {
    private static final SecureRandom RNG = new SecureRandom();

    private PkceUtil() {}

    public static String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String generateCodeChallenge(String verifier) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(verifier.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to compute PKCE challenge", e);
        }
    }
}