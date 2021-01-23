package com.problem.solving.fizzbuzz.service;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public interface FizzbuzzService {
    Map<String, Object> getFizzBuzzList(Long number);

    Map<String, Object> getFizzBuzzBetweenNumber(Long startNum, Long endNum);
}
