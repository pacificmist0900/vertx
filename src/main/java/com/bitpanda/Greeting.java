package com.bitpanda;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Greeting {
    UUID id;
    String text;

}
