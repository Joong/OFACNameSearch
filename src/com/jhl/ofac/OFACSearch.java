package com.jhl.ofac;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * User: jolee
 * Date: 9/17/14
 */
public class OFACSearch {
    private static final List<String> KNOWN_NAMES = Arrays.asList("Haji Mohammad Noor", "Aschraf AL-DAGMA", "Ahmad Zia AGHA",
                                                    "Umar Siddique Kathio", "MARTIN EDA", "GRANT L", "MINER WILLIAM");


    public static void main(String[] args) throws Exception {
        //List<String> namesToSearch = Files.readLines(new File(args[0]), Charset.defaultCharset());
        int processors = Runtime.getRuntime().availableProcessors();
        //System.out.println(Integer.toString(processors) + " processor" + (processors != 1 ? "s are " : " is ") + "available");
        OFACSearchTask task = new OFACSearchTask(ImmutableList.copyOf(KNOWN_NAMES));

        ForkJoinPool pool = new ForkJoinPool(processors);
        long startTime = System.currentTimeMillis();
        Hashtable<String, List> result = pool.invoke(task);
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
