package com.bitpanda;

import java.util.UUID;

public class GreetingNotFoundException extends RuntimeException {
    public GreetingNotFoundException(UUID id) {
        super("Post id: " + id + " was not found. ");
    }
}
