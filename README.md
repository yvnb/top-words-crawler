Steps to Run Top Words Java Application
---------------------------------------------------------------------
1. mvn package

   This Command creates the jar file in the target folder of the project

2. docker image build -t top-words-jar:latest .

   This Command creates docker image based on the Dockerfile present in the project root directory
   
3. docker run top-words-jar:latest

   This Command runs the project image created from the previous step

  
# Evolved/Scalable Top-Words-Crawler Architecture

The top words crawler architecture consists of the following components

Spring Batch Component:

This is responsible for ingesting the crawler websites file since the file cannot be accessed in parallel, its better to periodically poll the ftp or file 
and ingest the urls to the Message Queue (Message Broker). For simplicity, this component can also ingest the data from word bank to in memory map or redis for
validating the words for each crawler worker.

Spring Crawler Workers:

This instances can be spin up as per requirement which dequeues the messages (site url) from the message queue and scrap the required div contents and 
cleanse the data by validating against the bank of words map and place the words with count in the Redis Store by either updating or adding the keys (word:count)

Spring Top Words API:

This API fetches the top 10 words from Redis Server and displays the details to API User. Below is the architecture flow:

![top_words_architecture](https://github.com/yvnb/top-words-crawler/assets/18187490/4f332ad3-f368-49dd-90b4-1dc66075f5c4)



