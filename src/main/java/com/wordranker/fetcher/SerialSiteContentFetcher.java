package com.wordranker.fetcher;

import com.wordranker.data.SiteUrl;
import com.wordranker.tasks.WebContentScraperTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SerialSiteContentFetcher {

    private static final Logger logger = LogManager.getLogger(SerialSiteContentFetcher.class);
    private final List<SiteUrl> siteUrls;

    public SerialSiteContentFetcher(List<SiteUrl> siteUrls) {
        this.siteUrls = siteUrls;
    }

    public List<String> fetchContent() throws Exception{

        List<String> contentList = new ArrayList<>();
        for (SiteUrl siteUrl : siteUrls){
            contentList.add(scrapContent(siteUrl.getLink()));
        }

        return contentList;
    }

    private String scrapContent(String url){

        StringBuilder pageContent = new StringBuilder();
        try{
            Document doc = Jsoup.connect(url).get();

            // get title of the page
            pageContent.append(doc.title()).append(" ");

            // get all paragraphs in the document
            Elements paragraphs = doc.select("p");
            for (Element paragraph : paragraphs) {

                // get the value from paragraph attribute
                pageContent.append(paragraph.text()).append(" ");

            }

        } catch (IOException e) {
            logger.error("error while connecting to the url {}", url);
        }
        return pageContent.toString();
    }

}
