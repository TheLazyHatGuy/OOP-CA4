package com.ca4.Server;

import com.ca4.Core.MovieServiceDetails;
import com.ca4.DTO.User;
import org.junit.jupiter.api.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.awaitility.Awaitility.await;
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
        assertTimeout(ofSeconds(5), () -> {
            //Taken from - https://stackoverflow.com/questions/15938538/how-can-i-make-a-junit-test-wait
            await().until(() -> {
                String response = handler.processCommand(MovieServiceDetails.REGISTER +
                        MovieServiceDetails.BREAKING_CHARACTER + user.getEmail() +
                        MovieServiceDetails.BREAKING_CHARACTER + user.getPassword());
                boolean expectedResult = response.contains(MovieServiceDetails.REGISTER_SUCCESS
                        + MovieServiceDetails.BREAKING_CHARACTER);

                userID = Integer.parseInt(response.split(MovieServiceDetails.BREAKING_CHARACTER)[1]);

                assertTrue(expectedResult);
                assertTrue(userID >= 1);

                return true;
            });
        });
    }

    @Order(2)
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