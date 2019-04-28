package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import org.junit.jupiter.api.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieConnectionHandlerTestJunit5 {

    private static MovieConnectionHandler handler = new MovieConnectionHandler();
    private String input;
    private String expectedResult;

    public MovieConnectionHandlerTestJunit5(String input, String expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    static Collection<String[]> testConditions() {
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

        String jonFavreau = "[{\n" +
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
                "},\n" +
                "{\n" +
                "    \"copies\": 3,\n" +
                "    \"plot\": \"With the world now aware of his dual life as the armored superhero Iron Man, billionaire inventor Tony Stark faces pressure from the government, the press, and the public to share his technology with the military. Unwilling to let go of his invention, Stark, along with Pepper Potts, and James \\\"Rhodey\\\" Rhodes at his side, must forge new alliances - and confront powerful enemies.\",\n" +
                "    \"year\": \"2010\",\n" +
                "    \"director\": \"Jon Favreau\",\n" +
                "    \"genre\": \"Disney, marvel,Action, Adventure, Sci-Fi\",\n" +
                "    \"rating\": \"PG-13\",\n" +
                "    \"format\": \"DVD\",\n" +
                "    \"runtime\": \"124 min\",\n" +
                "    \"location\": \"\",\n" +
                "    \"id\": 15,\n" +
                "    \"staring\": \"Robert Downey Jr., Gwyneth Paltrow, Don Cheadle, Scarlett Johansson\",\n" +
                "    \"title\": \"iron man 2\"\n" +
                "},\n" +
                "{\n" +
                "    \"year\": \"2003\",\n" +
                "    \"director\": \"Jon Favreau\",\n" +
                "    \"rating\": \"PG\",\n" +
                "    \"format\": \"DVD\",\n" +
                "    \"user-rating\": \"6.9\",\n" +
                "    \"runtime\": \"97 min\",\n" +
                "    \"title\": \"Elf\",\n" +
                "    \"copies\": 2,\n" +
                "    \"plot\": \"After inadvertently wreaking havoc on the elf community due to his ungainly size, a man raised as an elf at the North Pole is sent to the U.S. in search of his true identity.\",\n" +
                "    \"genre\": \"Comedy, Family, Fantasy\",\n" +
                "    \"location\": \"\",\n" +
                "    \"id\": 606,\n" +
                "    \"staring\": \"Will Ferrell, James Caan, Bob Newhart, Edward Asner\",\n" +
                "    \"barcode\": \"5017239192470\"\n" +
                "},\n" +
                "{\n" +
                "    \"year\": \"2014\",\n" +
                "    \"director\": \"Jon Favreau\",\n" +
                "    \"rating\": \"R\",\n" +
                "    \"format\": \"DVD\",\n" +
                "    \"user-rating\": \"7.3\",\n" +
                "    \"runtime\": \"114 min\",\n" +
                "    \"title\": \"Chef\",\n" +
                "    \"copies\": 1,\n" +
                "    \"plot\": \"Carl Casper is an acclaimed chef with a family life that seems as decaying as his artistic freedom. Those frustrations boil over into a raucous viral-videoed public confrontation against a restaurant critic who panned his cooking of food that his boss ordered him to make against his instincts. Now with his career ruined, Carl's ex-wife offers an unorthodox solution in Miami: refit an old food truck to offer quality cooking on his own terms. Now with his young son, Percy, and old colleague, Martin, helping, Carl takes a working trip across America with that truck to rediscover his gastronomic passion. With Percy's tech savvy and Martin's enthusiasm, Carl finds that he is creating a traveling sensation on the way home. In doing so, Carl discovers he is serving up more than simply food, but also a deeper connection with his life and his family that is truly delicious in its own way.\",\n" +
                "    \"genre\": \"Adventure, Comedy, Drama\",\n" +
                "    \"location\": \"\",\n" +
                "    \"id\": 694,\n" +
                "    \"staring\": \"Jon Favreau, John Leguizamo, Bobby Cannavale, Emjay Anthony\",\n" +
                "    \"barcode\": \"5055761903331\"\n" +
                "},\n" +
                "{\n" +
                "    \"year\": \"2011\",\n" +
                "    \"director\": \"Jon Favreau\",\n" +
                "    \"rating\": \"PG-13\",\n" +
                "    \"format\": \"DVD\",\n" +
                "    \"user-rating\": \"6.0\",\n" +
                "    \"runtime\": \"119 min\",\n" +
                "    \"title\": \"Cowboys & Aliens\",\n" +
                "    \"copies\": 1,\n" +
                "    \"plot\": \"The Old West.. where a lone cowboy leads an uprising against a terror from beyond our world. 1873. New Mexico Territory. A stranger with no memory of his past stumbles into the hard desert town of Absolution. The only hint to his history is a mysterious shackle that encircles one wrist. What he discovers is that the people of Absolution don't welcome strangers, and nobody makes a move on its streets unless ordered to do so by the iron-fisted Colonel Dolarhyde (Ford). It's a town that lives in fear. But Absolution is about to experience fear it can scarcely comprehend as the desolate city is attacked by marauders from the sky. Screaming down with breathtaking velocity and blinding lights to abduct the helpless one by one, these monsters challenge everything the residents have ever known. Now, the stranger they rejected is their only hope for salvation. As this gunslinger slowly starts to remember who he is and where he's been, he realizes he holds a secret that could give the town a fighting chance against the alien force. With the help of the elusive traveler Ella (Olivia Wilde), he pulls together a posse comprised of former opponents-townsfolk, Dolarhyde and his boys, outlaws and Apache warriors-all in danger of annihilation. United against a common enemy, they will prepare for an epic showdown for survival.\",\n" +
                "    \"genre\": \"Action, Sci-Fi, Thriller\",\n" +
                "    \"location\": \"\",\n" +
                "    \"id\": 1015,\n" +
                "    \"staring\": \"Daniel Craig, Abigail Spencer, Buck Taylor, Matthew Taylor\",\n" +
                "    \"barcode\": \"5014437145734\"\n" +
                "}]";

        String[][] expectedInputsAndOutputs = {
                {MovieServiceDetails.SEARCH_MOVIE_TITLE + MovieServiceDetails.BREAKING_CHARACTER + "Iron Man", ironMan},
                {MovieServiceDetails.SEARCH_MOVIE_TITLE + MovieServiceDetails.BREAKING_CHARACTER + "Iron Man", ironMan},
                {MovieServiceDetails.SEARCH_MOVIE_TITLE, MovieServiceDetails.FAIL},
                {MovieServiceDetails.SEARCH_MOVIE_TITLE + MovieServiceDetails.BREAKING_CHARACTER, MovieServiceDetails.FAIL},
                {MovieServiceDetails.SEARCH_MOVIE_DIRECTOR + MovieServiceDetails.BREAKING_CHARACTER + "Jon Favreau", jonFavreau},
                {MovieServiceDetails.SEARCH_MOVIE_DIRECTOR + MovieServiceDetails.BREAKING_CHARACTER + "Jon Favreau", jonFavreau},
                {MovieServiceDetails.SEARCH_MOVIE_DIRECTOR, MovieServiceDetails.FAIL},
                {MovieServiceDetails.SEARCH_MOVIE_DIRECTOR + MovieServiceDetails.BREAKING_CHARACTER, MovieServiceDetails.FAIL},
        };

        return Arrays.asList(expectedInputsAndOutputs);
    }

    @Test
    void processCommand() {
        assertEquals(expectedResult, handler.processCommand(input));
    }
}