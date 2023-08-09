package com.search.search;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import org.jsoup.Jsoup;
import com.panforge.robotstxt.RobotsTxt;

public class WebCrawler {
    private static final int MAX_PAGES_TO_SEARCH = 100000;
    private static final Set<String> pagesVisited = new HashSet<>();
    public static final Database database = new Database();
    private static final Map<String, Integer> sameContentCount = new HashMap<>();
    private static final Map<String, String> robots = new HashMap<>();
    private static final Map<String, Integer> crawlRate = new HashMap<>();
    private static final Set<String> disallowedURLs = new HashSet<>();

    private static final Set<String> startUrls = new HashSet<>();



    static{ init(); }

    public static void init() {
        database.deleteDuplicates();

        pagesVisited.addAll(getPagesVisitedFromDatabase());

        startUrls.addAll(removeAllBut10(database.getStartUrls()));
//        startUrls.addAll(database.getStartUrls());

        //getRobots(startUrls);
        processRobots();
//        convertRobotsToRegexes();
        //database.saveRobots(robots);
    }


    private static Collection<String> removeAllBut10(Set<String> startUrls) {
        List<String> startUrlsList = new ArrayList<>(startUrls);
        Collections.shuffle(startUrlsList);
        return startUrlsList.subList(0, 10);
    }


    private static void processRobots() {

        for(String startUrl : startUrls ) {

            try (InputStream robotsTxtStream = new URL(startUrl+"robots.txt").openStream()) {
                System.out.println("Robots.txt: " + startUrl+"robots.txt");

                RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);

                crawlRate.put(startUrl, robotsTxt.getCrawlDelay());

                robotsTxt.getDisallowList("*").forEach(disallow -> {
                    if( disallow.startsWith("/") )
                        disallow = disallow.replaceFirst("/", "");

                    disallowedURLs.add(startUrl + disallow);

                    System.out.println("Disallow: " + startUrl + disallow);
                });

                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }
    /**
     * Gets all the robot.txt files from the start sites in the database
     */
      private static void getRobots(Set<String> startUrls) {
            for(String startUrl : startUrls ) {
                try {
                    Document doc = Jsoup.connect(startUrl + "robots.txt").get();
                    String robotsTxt = doc.body().text();
                    robots.put(startUrl, robotsTxt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
      }


//    private static void convertRobotsToRegexes() {
//        //Process disallow list converting the disallowed paths to regular expressions
//        List<String> newDisallowedList = new ArrayList<>();
//        for( String disallowedRootUrl : disallowedRootUrls ){
//            String escapedRegexCharacters = escapeRegexMetacharacters(disallowedRootUrl);
//            newDisallowedList.add(escapedRegexCharacters);
//        }
//
//        List<String> newDisallowedList2 = new ArrayList<>();
//        for (String disallowedRootUrl : newDisallowedList) {
//            String regex = disallowedRootUrl.replace("*", ".*");
//            newDisallowedList2.add(regex);
//        }
//        disallowedRootUrls.clear();
//        disallowedRootUrls.addAll(newDisallowedList2);
//    }

//    public static String escapeRegexMetacharacters(String input) {
//        // Metacharacters that need to be escaped
//        String[] metacharacters = {"\\", ".", "^", "$", "+", "?", "{", "}", "[", "]", "|", "(", ")"};
//
//        for (String metacharacter : metacharacters) {
//            input = input.replace(metacharacter, "\\" + metacharacter);
//        }
//
//        return input;
//    }

    private RobotsURLChecker robotsURLChecker = new RobotsURLChecker();

    public WebCrawler(){

    }

    public void startCrawling(){
//        startUrls.forEach((url) -> {
//        new Thread(()->{
//            crawl(url);
//        });
//        });
    }

    public static void main(String[] args) {
        testCheckIfDissallowedMethod();
    }

    private static void testCheckIfDissallowedMethod() {
        WebCrawler webCrawler = new WebCrawler();


        List<String> testURLs = TestURLs.getTestUrls();



        System.out.println("Checking if the following urls are disallowed:\n");

        //Run test urls through the checkIfDisallowed method
        boolean passed = true;
        for (String testURL : testURLs) {
            webCrawler.robotsURLChecker.isURLDisallowed(testURL, disallowedURLs);
            for (String disallowedRootUrlRegexs : disallowedURLs) {
                boolean matches = testURL.matches(disallowedRootUrlRegexs);
                System.out.println(testURL + " : " + ":" + disallowedRootUrlRegexs + " : " + matches);
                passed &= !matches;
            }
        }
        System.out.println(passed ? "\nPassed" : "\nFailed");
    }


    public void crawl(String url) {
        if( true ) throw new RuntimeException("Uh make sure you know the implications of this method before you use it. It is in its early stages of development." +
                "There are legal repercussions for crawling websites without permission.");

        try {
            int ms_delay = getCrawlDelay(url);

            delay(ms_delay);

            boolean hasNotYetCrawled = false;
            synchronized (pagesVisited) {
                hasNotYetCrawled = !pagesVisited.contains(url);
            }
            if (hasNotYetCrawled && pagesVisited.size() < MAX_PAGES_TO_SEARCH) {

                Document doc = Jsoup.connect(url).get();

                String title = doc.title();
                String content = doc.body().text();

                database.storeInDatabase(url, title, content);

                //Follow links on page

                Elements linksOnPage = doc.select("a[href]");

                System.out.println("Visited: " + url);

                synchronized (pagesVisited) {
                    if( isAllowed(url) )
                        pagesVisited.add(url);
                }

                for (Element page : linksOnPage) {
                    crawl(page.attr("abs:href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAllowed(String url) {
        return disallowedURLs.stream().anyMatch(regex -> url.matches(regex));
    }

    private static Set<String> getPagesVisitedFromDatabase(){
        return database.getVisitedUrls();
    }


    private static int getCrawlDelay(String full_url) {
        String root_url = getRootUrl(full_url);
        synchronized (crawlRate) {
            return crawlRate.getOrDefault(root_url, 0);
        }
    }

    private static String getRootUrl(String url) {
        String[] split = url.split("/");
        return split[2];
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println("Error sleeping");
        }
    }



}