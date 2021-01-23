package com.problem.solving.fizzbuzz.controller;


import com.problem.solving.fizzbuzz.service.FizzbuzzService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FizzbuzzService fizzbuzzService;

    @Test
    public void getFizzBuzzListTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getFizzBuzzList?fizzNumber=5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"result\":[\"1\",\"2\",\"fizz\",\"4\",\"buzz\"],\"in\":5}"));
    }

    @Test
    public void getFizzBuzzBetweenNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getFizzBuzzBetweenNumber?startNumber=9&endNumber=15").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"result\":[\"fizz\",\"buzz\",\"11\",\"fizz\",\"13\",\"14\",\"fizzBuzz\"],\"in\":{\"startNumber\":9,\"endNumber\":15}}"));
    }

}
