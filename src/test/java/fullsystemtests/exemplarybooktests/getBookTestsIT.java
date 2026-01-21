package fullsystemtests.exemplarybooktests;


import adapters.in.api.models.BookDTO;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GetBookTestsIT {

    @TestHTTPResource
    URI baseUri;

    private static final String ADMIN_USER = "1";
    private static final String ADMIN_PASSWORD = "adminpassword123";
    private static final String BOOKS_PATH = "/library/books";

    private final Set<Long> createdBooks = new HashSet<>();

    private BookDTO book(long isbn)
    {
        BookDTO b = new BookDTO();
        b.setId(isbn);
        b.setTitle("Lord Of The Rings");
        b.setAuthor("J. R. R. Tolkien");
        b.setDescription("A book you should read.");
        b.setCopyAmount(1);
        return b;
    }

    private void createAsAdmin(long isbn)
    {
        given()
                .baseUri(baseUri.toString())
                .auth().preemptive().basic(ADMIN_USER, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(book(isbn))
                .when()
                .put(BOOKS_PATH + "/" + isbn)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)));

        createdBooks.add((Long) isbn);
    }

    @Test
    void getAllBooks() {
        given()
                .baseUri(baseUri.toString())
                .when()
                .get(BOOKS_PATH)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    void getSingleBook() {
        long isbn = 9999000002L;
        createAsAdmin(isbn);

        BookDTO result =
                given()
                        .baseUri(baseUri.toString())
                        .accept("application/json")
                        .when()
                        .get(BOOKS_PATH + "/" + isbn)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BookDTO.class);

        assertEquals("Lord Of The Rings", result.getTitle());
        assertEquals("J. R. R. Tolkien", result.getAuthor());
        assertEquals(1, result.getCopyAmount());
        assertNotNull(result.getSelfLink());
    }

    @Test
    void createBook_asAdmin() {
        long isbn = 9999000001L;

        given()
                .baseUri(baseUri.toString())
                .auth().preemptive().basic(ADMIN_USER, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(book(isbn))
                .when()
                .put(BOOKS_PATH + "/" + isbn)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)))
                .header("Location", notNullValue());

        createdBooks.add(Long.valueOf(isbn));
    }

    @Test
    void deleteBook_asAdmin() {
        long isbn = 9999000003L;
        createAsAdmin(isbn);

        given()
                .baseUri(baseUri.toString())
                .auth().preemptive().basic(ADMIN_USER, ADMIN_PASSWORD)
                .when()
                .delete(BOOKS_PATH + "/" + isbn)
                .then()
                .statusCode(204);
    }

    @Test
    void createBook_asNonAdmin_isUnauthorized() {
        long isbn = 9999000004L;

        given()
                .baseUri(baseUri.toString())
                .contentType("application/json")
                .body(book(isbn))
                .when()
                .put(BOOKS_PATH + "/" + isbn)
                .then()
                .statusCode(401);
    }

    @AfterEach
    void cleanup()
    {
        createdBooks.forEach(isbn ->
                given()
                        .baseUri(baseUri.toString())
                        .auth().preemptive().basic(ADMIN_USER, ADMIN_PASSWORD)
                        .when()
                        .delete(BOOKS_PATH + "/" + (long) isbn)
                        .then()
                        .statusCode(anyOf(is(204), is(404)))
        );
        createdBooks.clear();
    }
}