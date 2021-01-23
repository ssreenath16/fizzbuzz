package com.problem.solving.fizzbuzz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.temporal.ValueRange;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class FizzbuzzServiceImpl implements FizzbuzzService {

    @Value("${max.fizzbuzz.minnumber}")
    private long minFizzBuzzNumber;

    @Value("${max.fizzbuzz.maxnumber}")
    private long maxFizzBuzzNumber;

    @Value("${max.splitnumber}")
    private long splitnumber;

    @Override
    public Map<String, Object> getFizzBuzzList(Long number) {
       long startTime = new Date().getTime();
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("in", number);
        boolean isValidNumber = checkIsValidFizzBuzzNumber(number);
        if(isValidNumber){
            if(splitnumber>number){
                responseMap.put("result", getFizzBuzzListString(1l, number));
            }else{
                List<CompletableFuture<List<String>>> completableFutures = new ArrayList<>();
                ExecutorService customExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                ArrayList<Long> parList = splitNumber(number);
                Long previousNumber = number;
                for (Long parNum :parList) {
                    if(previousNumber==0)
                        break;
                    System.out.println("parNum ::" + parNum+ "  previousNumber ::"+previousNumber);
                    Long finalPreviousNumber = previousNumber;
                    CompletableFuture requestCompletableFuture = CompletableFuture
                            .supplyAsync(() ->getAsyncFizzBuzzListString(parNum, finalPreviousNumber), customExecutor);
                    previousNumber = parNum - 1;
                    completableFutures.add(requestCompletableFuture);
                }
                CompletableFuture<List<List<String>>> responseList = allOf(completableFutures);
                List<String> combined = new ArrayList<String>();
                Collections.reverse(responseList.join());
                for(int i=0; i<responseList.join().size(); i++){
                    combined.addAll(responseList.join().get(i));
                }
                responseMap.put("result", combined);
            }
        }else{
            responseMap.put("error", "Not a valid Input, try again with valid input, maximum number allowed is (2^32 - 1)");
        }
        System.out.println("Total Time Duration in MS ::" + String.valueOf(new Date().getTime() - startTime));

        return responseMap;
    }

    @Override
    public Map<String, Object> getFizzBuzzBetweenNumber(Long startNum, Long endNum) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Map<String, Long> inputRes = new HashMap<>();
        inputRes.put("startNumber", startNum);
        inputRes.put("endNumber", endNum);
        responseMap.put("in", inputRes);
        if(startNum<endNum){
            boolean isValidNumber = checkIsValidFizzBuzzNumber(endNum);
            if(isValidNumber){
                responseMap.put("result", getFizzBuzzListString(startNum, endNum));
            }else{
                responseMap.put("error", "Not a valid Input, maximum endnumber is (2^32 - 1)");
            }
        }else{
            responseMap.put("error", "Not a valid Input, Start Number should be less than end number");
        }
        return responseMap;
    }

    /*
    Performing validation
    */
    private boolean checkIsValidFizzBuzzNumber(Long number){
        return ValueRange.of(minFizzBuzzNumber, maxFizzBuzzNumber, minFizzBuzzNumber, maxFizzBuzzNumber).isValidValue(number);
    }

    /*
    Performing Task based on start and end number
    */
    private List<String> getFizzBuzzListString(Long start, Long end){
        LongStream longStream = LongStream.rangeClosed(start, end);
        List<String> resultList =  longStream.parallel().mapToObj(i -> i % 3 == 0 ? (i % 5 == 0 ? "fizzBuzz" : "fizz") : (i % 5 == 0 ? "buzz" : String.valueOf(i)))
                .collect(Collectors.toList());
        return resultList;
    }


    /*
    Performing asyncExecutor Task
     */
    @Async("asyncExecutor")
    public List<String> getAsyncFizzBuzzListString(Long startNumber, Long endNumber)
    {
        LongStream longStream = LongStream.rangeClosed(startNumber, endNumber);
        List<String> resultList =  longStream.parallel().mapToObj(i -> i % 3 == 0 ? (i % 5 == 0 ? "fizzBuzz" : "fizz") : (i % 5 == 0 ? "buzz" : String.valueOf(i)))
                .collect(Collectors.toList());
        return resultList;
    }


    public <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futuresList) {
        CompletableFuture<Void> allFuturesResult =
                CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
        return allFuturesResult.thenApply(v ->
                futuresList.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }


    private ArrayList<Long> splitNumber(Long num){
        ArrayList<Long> arrLt = new ArrayList<>();
        for(int i=0; i<42949; i++) {
            num = num - splitnumber;
            if(num<1) {
                arrLt.add(1l);
                break;
            }else {
                arrLt.add(num);
            }
        }
        return arrLt;
    }
}
