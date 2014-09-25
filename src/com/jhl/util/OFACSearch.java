package com.jhl.util;

import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;

/**
 * User: jolee
 * Date: 9/17/14
 */
public class OFACSearch {
    public static void main(String[] args) throws Exception {
        //List<String> namesToSearch = Files.readLines(new File(args[0]), Charset.defaultCharset());
        List<String> namesToSearch = Arrays.asList("Haji Mohammad Noor", "Aschraf AL-DAGMA", "Ahmad Zia AGHA",
                                                    "Umar Siddique Kathio", "MARTIN EDA", "GRANT L", "MINER WILLIAM");
        int processors = Runtime.getRuntime().availableProcessors();
        //System.out.println(Integer.toString(processors) + " processor" + (processors != 1 ? "s are " : " is ") + "available");
        OFACSearchTask task = new OFACSearchTask(namesToSearch);

        ForkJoinPool pool = new ForkJoinPool(processors);
        long startTime = System.currentTimeMillis();
        ConcurrentMap<String, List> result = pool.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("OFAC name search took " + (endTime - startTime)/1000 + " seconds");
        for (Map.Entry<String, List> entry : result.entrySet()) {
            List<OFACSearchResult> matches = entry.getValue();
            for (OFACSearchResult searchResult : matches) {
                System.out.println(entry.getKey() + ": " + searchResult);
            }
        }
    }
}
