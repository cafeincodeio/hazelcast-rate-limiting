package com.cafeincode.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CardDto implements Serializable {
    private String cardNumber;
    private String serial;
    private String expireDate;
    private String cardTelco;
}
