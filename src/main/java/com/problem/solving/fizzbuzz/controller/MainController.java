package com.problem.solving.fizzbuzz.controller;

import com.problem.solving.fizzbuzz.service.FizzbuzzService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MainController {

    @Autowired
    private FizzbuzzService fizzbuzzService;

    @ApiOperation(value = "API to see the FizzBuzz between 1 and input number", response = Map.class)
    @GetMapping("/getFizzBuzzList")
    public Map<String, Object> getFizzBuzzList(@RequestParam(value = "fizzNumber") Long number) {
        return fizzbuzzService.getFizzBuzzList(number);
    }

    @ApiOperation(value = "API to see the FizzBuzz between two Numbers", response = Map.class)
    @GetMapping("/getFizzBuzzBetweenNumber")
    public Map<String, Object> getFizzBuzzBetweenNumber(@RequestParam(value = "startNumber") Long startNum, @RequestParam(value = "endNumber") Long endNum) {
        return fizzbuzzService.getFizzBuzzBetweenNumber(startNum, endNum);
    }

}
