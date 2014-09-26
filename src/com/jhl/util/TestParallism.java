package com.jhl.util;

import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * User: jolee
 * Date: 9/25/14
 */
public class TestParallism {
    private void testCode() throws Exception {
//        IntStream.range(0, 4999).parallel().forEach(i -> {
//            System.out.println(i + " " + ForkJoinPool.commonPool().getParallelism());
//        });

        List<String> namesToSearch = Files.readLines(new File("/Users/jolee/Downloads/lotsofnames"), Charset.defaultCharset());
        //namesToSearch.parallelStream().forEach(s -> System.out.println(s));
        System.out.println(namesToSearch.size());
        namesToSearch.subList(10, 20);
        System.out.println(namesToSearch.size());
    }

    public static void main(String[] args) throws Exception {
        TestParallism test = new TestParallism();
        test.testCode();
    }
}
