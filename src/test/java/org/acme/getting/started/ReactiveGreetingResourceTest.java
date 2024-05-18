package org.acme.getting.started;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ReactiveGreetingResourceTest {

    private static Logger log = Logger.getLogger(ReactiveGreetingResourceTest.class);

    @Test
    public void testHelloEndpoint() {
        log.info("Testing the hello endpoint @@@@@@@@@@");
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
                .pathParam("name", uuid)
                .when().get("/hello/greeting/{name}")
                .then()
                .statusCode(200)
                .body(is("hello " + uuid));
    }


    @Test
    public void testGreetingsEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
                .pathParam("name", uuid)
                .pathParam("count", 2)
                .when().get("/hello/greeting/{count}/{name}")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("hello " + uuid + " - 0"))
                .body(containsString("hello " + uuid + " - 1"));
    }

}
