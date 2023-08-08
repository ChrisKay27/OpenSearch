package com.search.search;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;



    private String[] urls1 = {"https://www.weather.com/",
            "https://www.mlb.com/",
            "https://www.nba.com/",
            "https://www.npr.org/",
            "https://www.cnet.com/",
            "https://www.stackoverflow.com/",
            "https://www.medium.com/",
            "https://www.quora.com/",
            "https://www.slack.com/",
            "https://www.salesforce.com/",
            "https://www.apple.com/",
            "https://www.netflix.com/",
            "https://www.spotify.com/",
            "https://www.dropbox.com/",
            "https://www.yelp.com/",
            "https://www.zillow.com/",
            "https://www.tripadvisor.com/",
            "https://www.etsy.com/",
            "https://www.target.com/",
            "https://www.costco.com/",
            "https://www.mcdonalds.com/",
            "https://www.starbucks.com/",
            "https://www.linux.org/",
            "https://www.python.org/",
            "https://www.ruby-lang.org/",
            "https://www.javaworld.com/",
            "https://www.arduino.cc/",
            "https://www.raspberrypi.org/",
            "https://www.wikipedia.org/",
            "https://www.gitlab.com/",
            "https://www.kaggle.com/",
            "https://www.tensorflow.org/"};
     private String[] urls2 =   {"https://www.huffingtonpost.com/",
            "https://www.businessinsider.com/",
            "https://www.buzzfeed.com/",
            "https://www.foxnews.com/",
            "https://www.economist.com/",
            "https://www.aljazeera.com/",
            "https://www.techcrunch.com/",
            "https://www.sciencedaily.com/",
            "https://www.vice.com/",
            "https://www.time.com/",
            "https://www.theguardian.com/",
            "https://www.cnbc.com/",
            "https://www.mayoclinic.org/",
            "https://www.khanacademy.org/",
            "https://www.instructables.com/",
            "https://www.gamestop.com/",
            "https://www.behance.net/",
            "https://www.kickstarter.com/",
            "https://www.w3schools.com/",
            "https://www.duolingo.com/",
            "https://www.rottentomatoes.com/",
            "https://www.pinterest.com/",
            "https://www.ikea.com/",
            "https://www.nike.com/",
            "https://www.ferrari.com/",
            "https://www.rolls-royce.com/",
            "https://www.nasa.gov/",
            "https://www.greenpeace.org/",
            "https://www.unicef.org/",
            "https://www.goodreads.com/",
            "https://www.reddit.com/r/all",
            "https://www.github.com/"};

    private String[] urls3 = {"https://www.bloomberg.com/",
            "https://www.nationalgeographic.com/",
            "https://www.bbcearth.com/",
            "https://www.vogue.com/",
            "https://www.gq.com/",
            "https://www.wired.com/",
            "https://www.rollingstone.com/",
            "https://www.vanityfair.com/",
            "https://www.mtv.com/",
            "https://www.foodnetwork.com/",
            "https://www.espn.com/",
            "https://www.latimes.com/",
            "https://www.wsj.com/",
            "https://www.homedepot.com/",
            "https://www.bestbuy.com/",
            "https://www.samsung.com/",
            "https://www.walmart.com/",
            "https://www.microsoft.com/",
            "https://www.ibm.com/",
            "https://www.oracle.com/",
            "https://www.uber.com/",
            "https://www.airbnb.com/",
            "https://www.lynda.com/",
            "https://www.udemy.com/",
            "https://www.coursera.org/",
            "https://www.edx.org/",
            "https://www.lumosity.com/",
            "https://www.ted.com/",
            "https://www.soundcloud.com/",
            "https://www.patreon.com/",
            "https://www.evernote.com/",
            "https://www.trello.com/",};


    @FXML
    protected void onHelloButtonClick() {
        new Thread(()->{

            WebCrawler crawler = new WebCrawler();

            //crawler.startCrawling();

        }).start();
    }
}