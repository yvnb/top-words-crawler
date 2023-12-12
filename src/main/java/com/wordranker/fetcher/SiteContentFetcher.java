package com.wordranker.fetcher;

import com.wordranker.data.SiteUrl;
import com.wordranker.ranker.TopWordsRanker;
import com.wordranker.ranker.WordCountGenerator;
import com.wordranker.tasks.WebContentScraperTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SiteContentFetcher {

    private static final Logger logger = LogManager.getLogger(SiteContentFetcher.class);
    private final List<SiteUrl> siteUrls;

    private final ThreadPoolExecutor executor;

    /**
     * Number of threads the executor will use internally
     */
    private final int numThreads;

    public SiteContentFetcher(List<SiteUrl> siteUrls) {
        this.siteUrls = siteUrls;
        this.numThreads= Runtime.getRuntime().availableProcessors();
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
    }

    public List<String> fetchContent() throws Exception{

        List<String> contentList = new CopyOnWriteArrayList<String>();
        CountDownLatch endController=new CountDownLatch(numThreads);
        int length = siteUrls.size()/numThreads;
        int startIndex=0, endIndex=length;

        for(int i = 0; i < numThreads; i++) {

            WebContentScraperTask task = new WebContentScraperTask(siteUrls, contentList, endController, startIndex, endIndex);
            startIndex = endIndex;
            if (i < numThreads - 2) {
                endIndex = endIndex + length;
            } else {
                endIndex = siteUrls.size();
            }
            executor.execute(task);
        }
        endController.await();
        return contentList;
    }

    public void destroy(){
        executor.shutdown();
    }
}
