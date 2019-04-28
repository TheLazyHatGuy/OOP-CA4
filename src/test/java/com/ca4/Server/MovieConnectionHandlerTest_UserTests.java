package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DTO.User;
import org.junit.jupiter.api.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

//Taken from - https://blog.codeleak.pl/2019/03/test-execution-order-in-junit-5.html
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieConnectionHandlerTest_UserTests {

    private static Lock sequential;
    private static MovieConnectionHandler handler;
    private static User user;
    private static int userID;

    @BeforeAll
    static void setUp() {
        sequential = new ReentrantLock();
        handler = new MovieConnectionHandler();
        user = new User(0, "test@example.com", "password");
    }

    //Taken from - https://stackoverflow.com/questions/545194/force-junit-to-run-one-test-case-at-a-time
    @BeforeEach
    void setUpEachTest() {
        sequential.lock();
    }

    @AfterEach
    void tearDown() {
        sequential.unlock();
    }

    @Order(1)
    @Test
    @DisplayName("Register new user")
    void processCommand_RegisterNewUser() {
        //First test takes a while to initialise cache
        //This test also takes a long time due to sending an email
        assertTimeout(ofSeconds(7), () -> {
            String response = handler.processCommand(MovieServiceDetails.REGISTER +
                    MovieServiceDetails.BREAKING_CHARACTER + user.getEmail() +
                    MovieServiceDetails.BREAKING_CHARACTER + user.getPassword());
            boolean expectedResult = response.contains(MovieServiceDetails.REGISTER_SUCCESS
                    + MovieServiceDetails.BREAKING_CHARACTER);

            userID = Integer.parseInt(response.split(MovieServiceDetails.BREAKING_CHARACTER)[1]);

            assertTrue(expectedResult);
            assertTrue(userID >= 1);
        });
    }

    @Order(2)
    @Test
    @DisplayName("Register same user")
    void processCommand_RegisterSameUser() {
        //Second test is always delayed due to waiting
        assertTimeout(ofMillis(400), () -> {
            String response = handler.processCommand(MovieServiceDetails.REGISTER +
                    MovieServiceDetails.BREAKING_CHARACTER + user.getEmail() +
                    MovieServiceDetails.BREAKING_CHARACTER + user.getPassword());

            assertEquals(MovieServiceDetails.REGISTER_ALREADY_REGISTERED, response);
        });
    }

    @Order(3)
    @Test
    @DisplayName("Register with only email")
    void processCommand_RegisterWithOnlyEmail() {
        assertTimeout(ofMillis(5), () -> {
            String response = handler.processCommand(MovieServiceDetails.REGISTER +
                    MovieServiceDetails.BREAKING_CHARACTER + user.getEmail());

            assertEquals(MovieServiceDetails.FAIL, response);
        });
    }

    @Order(4)
    @Test
    @DisplayName("Register without any parameters")
    void processCommand_RegisterWithoutParams() {
        assertTimeout(ofMillis(5), () -> {
            String response = handler.processCommand(MovieServiceDetails.REGISTER);
            assertEquals(MovieServiceDetails.FAIL, response);
        });
    }

    @Order(5)
    @Test
    @DisplayName("Delete User")
    void processCommand_2_DeleteUser() {
        assertTimeout(ofMillis(50), () -> {
            String response = handler.processCommand(MovieServiceDetails.DELETE_USER +
                    MovieServiceDetails.BREAKING_CHARACTER + userID);
            assertEquals(MovieServiceDetails.DELETE_USER_SUCCESS, response);
        });
    }
}