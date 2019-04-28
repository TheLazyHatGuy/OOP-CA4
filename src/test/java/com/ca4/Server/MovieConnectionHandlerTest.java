package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MovieConnectionHandlerTest {

    private static MovieConnectionHandler handler = new MovieConnectionHandler();
    private String input;
    private String expectedResult;

    public MovieConnectionHandlerTest(String input, String expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<String[]> testConditions() {
        String ironMan = "{\n" +
                "    \"copies\": 3,\n" +
                "    \"plot\": \"Tony Stark. Genius, billionaire, playboy, philanthropist. Son of legendary inventor and weapons contractor Howard Stark. When Tony Stark is assigned to give a weapons presentation to an Iraqi unit led by Lt. Col. James Rhodes, he's given a ride on enemy lines. That ride ends badly when Stark's Humvee that he's riding in is attacked by enemy combatants. He survives - barely - with a chest full of shrapnel and a car battery attached to his heart. In order to survive he comes up with a way to miniaturize the battery and figures out that the battery can power something else. Thus Iron Man is born. He uses the primitive device to escape from the cave in Iraq. Once back home, he then begins work on perfecting the Iron Man suit. But the man who was put in charge of Stark Industries has plans of his own to take over Tony's technology for other matters.\",\n" +
                "    \"year\": \"2008\",\n" +
                "    \"director\": \"Jon Favreau\",\n" +
                "    \"genre\": \"Marvel, Action, Adventure, Sci-Fi\",\n" +
                "    \"rating\": \"PG-13\",\n" +
                "    \"format\": \"DVD\",\n" +
                "    \"runtime\": \"126 min\",\n" +
                "    \"location\": \"\",\n" +
                "    \"id\": 14,\n" +
                "    \"staring\": \"Robert Downey Jr., Terrence Howard, Jeff Bridges, Gwyneth Paltrow\",\n" +
                "    \"title\": \"iron man\"\n" +
                "}";
        String[][] expectedInputsAndOutputs = {
                {MovieServiceDetails.SEARCH_MOVIE_TITLE + MovieServiceDetails.BREAKING_CHARACTER + "Iron Man", ironMan},
                {MovieServiceDetails.SEARCH_MOVIE_TITLE, MovieServiceDetails.FAIL},
        };

        return Arrays.asList(expectedInputsAndOutputs);
    }

    @Test
    public void processRequest() {
        assertEquals(expectedResult, handler.processCommand(input));
    }
}