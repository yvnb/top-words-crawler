package com.wordranker.loader;

import com.wordranker.data.SiteUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SiteUrlLoader {

    private static final Logger logger = LogManager.getLogger(SiteUrlLoader.class);
    public List<SiteUrl> load (String dataPath) {
        List<SiteUrl> urlList=new ArrayList<>();
        try (InputStream in = this.getClass().getResourceAsStream(dataPath);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String link = null;
            while ((link = reader.readLine()) != null) {
                SiteUrl siteUrl=new SiteUrl();
                siteUrl.setLink(link);
                urlList.add(siteUrl);
            }
        } catch (IOException ioException) {
            logger.error("Exception while reading/writing the file, {}", ioException.getMessage());
        } catch (Exception e) {
            logger.error("Exception while fetching urls from text file, {}", e.getMessage());
        }
        return urlList;
    }
}
