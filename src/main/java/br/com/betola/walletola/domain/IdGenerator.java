package br.com.betola.walletola.domain;

import java.util.UUID;

public class IdGenerator {

    public static String nextId() {
        return UUID.randomUUID().toString();
    }
}
