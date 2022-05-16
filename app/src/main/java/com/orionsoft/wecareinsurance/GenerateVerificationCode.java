package com.orionsoft.wecareinsurance;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateVerificationCode {

    private String verficationCode = null;

    public String generateCode() {
        SecureRandom random = new SecureRandom();
        String verficationCode = new BigInteger(30, random).toString(32).toUpperCase();
        return verficationCode;
    }
}
