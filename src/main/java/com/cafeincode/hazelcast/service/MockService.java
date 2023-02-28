package com.cafeincode.hazelcast.service;

import com.cafeincode.hazelcast.model.CardDto;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service(value = "mockService")
public class MockService {

    public List<CardDto> mockCards() {
        CardDto cardOne = generateCardInfo(new Random().nextInt(6));
        CardDto cardTwo = generateCardInfo(new Random().nextInt(7));
        CardDto cardThree = generateCardInfo(new Random().nextInt(8));
        CardDto cardFour = generateCardInfo(new Random().nextInt(9));
        CardDto cardFive = generateCardInfo(new Random().nextInt(10));
        return Arrays.asList(cardOne, cardTwo, cardThree, cardFour, cardFive);
    }

    private CardDto generateCardInfo(int size) {
        return CardDto.builder().cardNumber(RandomString.make(size + 5)).serial(RandomString.make(size + 5)).expireDate("2705-05-27").cardTelco("VTE").build();
    }
}
