package com.github.mihalyfodor.wallet.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletDescription {
    private String name;
    private String port;
}
