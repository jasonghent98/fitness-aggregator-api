package com.jasonghent98.fitness_aggregator_api.util.auth;

public class EmailVerificationService {

    public static boolean looksLikeEmail(String s) {
        // very light check; keep server permissive and rely on email delivery for “truth”
        int at = s.indexOf('@');
        int dot = s.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < s.length() - 1;
    }

}
