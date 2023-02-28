package com.cafeincode.hazelcast.controller;

import com.cafeincode.hazelcast.model.CardDto;
import com.cafeincode.hazelcast.model.ConfigRateLimit;
import com.cafeincode.hazelcast.service.MockService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author hungtv27
 * @date 28/02/2023
 * @source cafeincode.com
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cards")
public class HzRestController {

    private final HazelcastInstance hazelcastClient;
    private final ConfigRateLimit configRateLimit;
    private final MockService mockService;

    private static final String MAPS_USER = "USERS";
    private static final String AUTHENTICATED_USER = "cafeincode-user";


    @GetMapping
    public ResponseEntity<List<CardDto>> getListCards(@RequestHeader Map<String, String> headers) {

        String userRequest = getUserInfoFromHeader(headers);   //get user info from header -> for test
        IMap<String, Long> hzMapUser = hazelcastClient.getMap(MAPS_USER);   //get map config from hazelcast
        if (isAccessResource(userRequest, hzMapUser)) {   //check access resource
            savedCounterRequestOfUser(userRequest, incrementAndGetCounterOfUser(userRequest), configRateLimit.getSecond(), hzMapUser);
            log.info("User:[{}] has access resource, currentCounter={}", userRequest, getCurrentCounter(userRequest, hzMapUser));
            return ResponseEntity.ok(mockService.mockCards());
        }
        log.info("User:[{}] was rejected because too many request in [{}] s", userRequest, configRateLimit.getSecond());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    private boolean isAccessResource(String userRequest, IMap<String, Long> mapHz) {
        long limit = configRateLimit.getLimit();
        long currentCounter = getCurrentCounter(userRequest, mapHz);
        return currentCounter < limit;
    }

    private long getCurrentCounter(String userRequest, IMap<String, Long> mapHz) {
        if (Objects.isNull(mapHz.get(userRequest))) {
            resetCounter(userRequest);
            return 0L;
        }
        return mapHz.get(userRequest);
    }

    private long incrementAndGetCounterOfUser(String userRequest) {
        IAtomicLong counter = hazelcastClient.getCPSubsystem().getAtomicLong(userRequest);
        return counter.incrementAndGet();
    }

    private void savedCounterRequestOfUser(String userRequest, long counter, long secondExpired, IMap<String, Long> mapHz) {
        mapHz.put(userRequest, counter, secondExpired, TimeUnit.SECONDS);
    }

    private void resetCounter(String userRequest) {
        IAtomicLong counter = hazelcastClient.getCPSubsystem().getAtomicLong(userRequest);
        counter.set(0);
        log.info("Reset counter of user: [{}] successfully counter:[{}]", userRequest, counter.get());
    }

    private String getUserInfoFromHeader(Map<String, String> headers) {
        return headers.get(AUTHENTICATED_USER);
    }
}
