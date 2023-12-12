package com.wordranker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordranker.data.SiteUrl;
import com.wordranker.fetcher.SerialSiteContentFetcher;
import com.wordranker.fetcher.SiteContentFetcher;
import com.wordranker.loader.SiteUrlLoader;
import com.wordranker.loader.WordBankLoader;
import com.wordranker.ranker.TopWordsRanker;
import com.wordranker.ranker.WordCountGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {

        Date start, end;

        SiteUrlLoader urlLoader = new SiteUrlLoader();
        WordBankLoader wordBankLoader = new WordBankLoader();

        //load the urls from site urls file
        List<SiteUrl> urls = urlLoader.load("/sample-site-urls.txt");
        logger.info("Urls Fetched from the file : {} ", urls.size());

        //load bank of valid words from file
        Set<String> validWords = wordBankLoader.load("/words_bank.txt");

        logger.info("valid word bank size : {}", validWords.size());

        start = new Date();
        double currentTime = 0d;

        SiteContentFetcher siteContentFetcher = new SiteContentFetcher(urls);
       // SerialSiteContentFetcher serialSiteContentFetcher = new SerialSiteContentFetcher(urls);
        List<String> contents = null;

        try {
            contents = siteContentFetcher.fetchContent();
        } catch (Exception e) {
            logger.error("exception while fetching site contents : {}", e.getMessage());
        }
        end = new Date();

        currentTime = end.getTime() - start.getTime();

        siteContentFetcher.destroy();

        WordCountGenerator wordCountGenerator = new WordCountGenerator(contents);
        Map<String, Integer> wordCountMap = wordCountGenerator.fetchWordCountGenerator();

        TopWordsRanker topWordsRanker = new TopWordsRanker(10, wordCountMap);
        LinkedHashSet<String> topWords = topWordsRanker.getTopWords();



        logger.info("system execution time for fetching the top words in ms: {}", currentTime);
        logger.info("fetching the top words : {}", convertToJson(topWords));
    }

    private static String convertToJson(Set<String> linkedHashSet) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(linkedHashSet);
        } catch (JsonProcessingException e) {
            logger.error("exception while converting words list to json : {}", e.getMessage());
            return null;
        }
    }
}