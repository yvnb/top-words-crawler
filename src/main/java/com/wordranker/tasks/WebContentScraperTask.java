package com.wordranker.tasks;

import com.wordranker.data.SiteUrl;
import com.wordranker.fetcher.SiteContentFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebContentScraperTask implements Runnable {

    private static final Logger logger = LogManager.getLogger(WebContentScraperTask.class);

    private final List<SiteUrl> siteUrls;

    private final List<String> webContents;

    private final CountDownLatch endController;

    /**
     * Indexes that determines the examples of the train data this task will process
     */
    private final int startIndex, endIndex;

    public WebContentScraperTask(List<SiteUrl> siteUrls, List<String> webContents, CountDownLatch endController, int startIndex, int endIndex) {
        this.siteUrls = siteUrls;
        this.webContents = webContents;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.endController = endController;
    }

    @Override
    public void run() {

        for (int index = startIndex; index < endIndex; index++) {

            String url = siteUrls.get(index).getLink();

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
                webContents.add(pageContent.toString());

            } catch (IOException e) {
                logger.error("error while connecting to the url {}", url);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        endController.countDown();

    }
}
