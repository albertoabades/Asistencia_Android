package com.example.asistencia;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by alberto on 13/5/18.
 */
public class RSA {

    private final static BigInteger one = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();

    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger modulus;

    RSA(){
        int n = 50;
        BigInteger p = BigInteger.probablePrime(n/2, random);
        BigInteger q = BigInteger.probablePrime(n/2, random);
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

        modulus = p.multiply(q);
        publicKey = new BigInteger("65537"); //2^16 + 1
        privateKey = publicKey.modInverse(phi);
    }

    BigInteger encrypt(BigInteger message){
        return message.modPow(publicKey, modulus);
    }

    BigInteger decrypt(BigInteger encrypted){
        return encrypted.modPow(privateKey, modulus);
    }

    public String toString(){
        String s = "";
        s += "public: " + publicKey + "\n";
        s += "private: " + privateKey + "\n";
        s += "modulus: " + modulus;
        return s;
    }
}
