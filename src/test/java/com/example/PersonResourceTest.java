package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class PersonResourceTest {
    @Test
    public void testListAllPersons() {
        //List all
        Response response = given()
                .when()
                .get("/person")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();
        assertThat(response.jsonPath().getList("lastname")).containsExactlyInAnyOrder("Ivanov");

        // Update
        given()
                .when()
                .body("{\"lastname\" : \"Иванов\",\"firstname\" : \"Иван\"}")
                .contentType("application/json")
                .put("/person/1")
                .then()
                .statusCode(200)
                .body(
                        containsString("\"id\":"),
                        containsString("\"lastname\":\"Иванов\""));

        //Delete Person:
        given()
                .when()
                .delete("/person/1")
                .then()
                .statusCode(204);

    }
}