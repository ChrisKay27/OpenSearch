package com.search.search;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.List;

public class RobotsURLChecker {

    public static void main(String[] args) {
        String urlToCheck = "https://www.example.com/user/data";
        Set<String> disallowedPatterns = Set.of(
                "/user/*",
                "/admin/page$"
        );
        RobotsURLChecker robotsURLChecker = new RobotsURLChecker();
        boolean isDisallowed = robotsURLChecker.isURLDisallowed(urlToCheck, disallowedPatterns);
        System.out.println("URL is disallowed: " + isDisallowed);
    }

    public boolean isURLDisallowed(String url, Set<String> disallowedPatterns) {
        for (String patternStr : disallowedPatterns) {
            String regex = convertRobotsPatternToRegex(patternStr);
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }

    public static String convertRobotsPatternToRegex(String robotsPattern) {
        // Note: Assuming a simple robots.txt pattern conversion.
        robotsPattern = robotsPattern.replace(".", "\\.");
        robotsPattern = robotsPattern.replace("*", ".*");
        return "^.*" + robotsPattern + ".*$";
    }
}