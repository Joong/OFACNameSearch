package com.jhl.util;

import com.google.common.collect.MapMaker;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * User: jolee
 * Date: 9/24/14
 */
public class OFACSearchTask extends RecursiveTask<ConcurrentMap<String, List>> {
    private static final int MAX_NAME_THRESHOLD = 3;
    private static final String URL = "http://sdnsearch.ofac.treas.gov/";
    private static final String TYPE = "Individual";
    private static final String MIN_SCORE = "85";
    private static final int WAIT_TIME = 0;
    private WebDriver driver = null;
    private int numOfNamesToSearch;
    private ConcurrentMap<String, List> concurrentResultMap = null;

    public OFACSearchTask(List<String> names) {
        setConcurrentMap(names);
    }

    @Override
    protected ConcurrentMap<String, List> compute() {
        if (numOfNamesToSearch <= MAX_NAME_THRESHOLD) {
            for (Map.Entry<String, List> entry : concurrentResultMap.entrySet()) {
                driver = new HtmlUnitDriver();
                driver.get(URL);
                setLookupType();
                setMinimumScore();
                setSearchName(entry.getKey());
                entry.setValue(searchAndGetOFACSearchResult());
            }
            return concurrentResultMap;
        } else {
            String[] names = concurrentResultMap.keySet().toArray(new String[0]);
            List<OFACSearchTask> forks = new LinkedList<OFACSearchTask>();

            int split = (names.length / MAX_NAME_THRESHOLD) + (names.length % MAX_NAME_THRESHOLD);
            int start = 0;
            for (int i=0; i < split; i++) {
                int end = (start + MAX_NAME_THRESHOLD > names.length ? names.length : start + MAX_NAME_THRESHOLD);
                OFACSearchTask task = new OFACSearchTask(Arrays.asList(Arrays.copyOfRange(names, start, end)));
                start = end;
                forks.add(task);
                task.fork();
            }

            for (OFACSearchTask task : forks) {
                concurrentResultMap.putAll(task.join());
            }
            return concurrentResultMap;
        }
    }

    private void setLookupType() {
        Select lookupType = new Select(driver.findElement(By.id("ctl00_MainContent_ddlType")));
        lookupType.selectByVisibleText(TYPE);
    }

    private void setMinimumScore() {
        WebElement slider = driver.findElement(By.id("ctl00_MainContent_Slider1"));
        slider.clear();
        slider.sendKeys(MIN_SCORE);
    }

    private void setSearchName(String name) {
        WebElement element = driver.findElement(By.id("ctl00_MainContent_txtLastName"));
        element.sendKeys(name);
    }

    private void setConcurrentMap(List<String> names) {
        numOfNamesToSearch = names.size();
        concurrentResultMap = new MapMaker().concurrencyLevel(MAX_NAME_THRESHOLD).makeMap();
        for (String name : names) {
            concurrentResultMap.put(name, new ArrayList());
        }
    }

    private List<OFACSearchResult> searchAndGetOFACSearchResult() {
        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        driver.findElement(By.id("ctl00_MainContent_btnSearch")).click();
        List<OFACSearchResult> searchResult = new ArrayList<OFACSearchResult>();
        try {
            WebElement element = driver.findElement(By.id("gvSearchResults"));
            List<WebElement> allRows = element.findElements(By.tagName("tr"));
            for (WebElement row : allRows) {
                OFACSearchResult ofacSearchResult = new OFACSearchResult();
                List<WebElement> cells = row.findElements(By.tagName("td"));
                String[] value = new String[6];
                int location = 0;
                for (WebElement cell : cells) {
                    value[location] = cell.getText();
                    location += 1;
                }
                ofacSearchResult.setName(value[0]);
                ofacSearchResult.setAddress(value[1]);
                ofacSearchResult.setType(value[2]);
                ofacSearchResult.setPrograms(value[3]);
                ofacSearchResult.setList(value[4]);
                ofacSearchResult.setScore(value[5]);
                searchResult.add(ofacSearchResult);
            }
        } catch (NoSuchElementException e) {
            // no match found and swallow the exception
        }
        return searchResult;
    }
}
