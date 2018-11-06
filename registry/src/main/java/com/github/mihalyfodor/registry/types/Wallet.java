package com.github.mihalyfodor.registry.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    private String port;
    private String name;

}
