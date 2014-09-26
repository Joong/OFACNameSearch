package com.jhl.util;

import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * User: jolee
 * Date: 9/24/14
 */
public class OFACSearchTask extends RecursiveTask<Hashtable<String, List>> {
    private static final int MAX_NAME_THRESHOLD = 3;
    private static final String URL = "http://sdnsearch.ofac.treas.gov/";
    private static final String TYPE = "Individual";
    private static final String MIN_SCORE = "85";
    private static final int WAIT_TIME = 0;
    private static List<String> listOfNames = null;
    private WebDriver driver = null;
    private int numOfNamesToSearch;
    private Hashtable<String, List> searchResult;

    public OFACSearchTask(List<String> names) {
        listOfNames = names;
        numOfNamesToSearch = listOfNames.size();
        searchResult = new Hashtable<>(numOfNamesToSearch);
    }

    @Override
    protected Hashtable<String, List> compute() {
        if (numOfNamesToSearch <= MAX_NAME_THRESHOLD) {
            for (String name : listOfNames) {
                driver = new HtmlUnitDriver();
                driver.get(URL);
                setLookupType();
                setMinimumScore();
                setSearchName(name);
                searchResult.put(name, searchAndGetOFACSearchResult());
            }
            return searchResult;
        } else {
            List<OFACSearchTask> forks = new LinkedList<>();
            List<List<String>> partition = Lists.partition(listOfNames, MAX_NAME_THRESHOLD);
            partition.parallelStream().forEach(n -> {
                OFACSearchTask task = new OFACSearchTask(n);
                forks.add(task);
                task.fork();
            });

            for (OFACSearchTask task : forks) {
                searchResult.putAll(task.join());
            }
            return searchResult;
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
