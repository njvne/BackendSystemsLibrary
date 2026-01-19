package fullsystemtests.exemplarybooktests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class getbooktestsIT
{
    @BeforeAll
    public static void setup()
    {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testGetAllBooks()
    {
        given().when()
                .get("/library/books")
                .then().statusCode(200);
    }

    @Test
    void testGetSingleBook()
    {
        given().when()
                .get("/library/books/123456789")
                .then().statusCode(200);
    }
}
