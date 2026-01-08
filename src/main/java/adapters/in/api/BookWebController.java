package adapters.in.api;

import application.domain.results.PutStatus;
import application.domain.Authorisation.AuthorizationLevel;
import application.domain.Authorisation.AuthorizationResult;
import adapters.in.api.adapter.BookServiceAdapter;
import adapters.in.api.adapter.BooksResult;
import adapters.in.api.models.BookDTO;
import adapters.in.api.utils.Hyperlinks;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;


@Path("library/books")    //maybe separate controllers for users, books, etc.? Refer to Unit 17 CRUD
public class BookWebController extends AbstractController
{
    @Inject
    private BookServiceAdapter bookServiceAdapter;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks(@DefaultValue("") @QueryParam("search") String search,
                                @DefaultValue("1") @PositiveOrZero @QueryParam("page") int page)
    {
        final var bookPage = search.trim().isEmpty() ? bookServiceAdapter.getAllBooks(page) : bookServiceAdapter.getBooksByQuery(page, search.trim());
        AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(bookPage.getBookDTOs().isEmpty())
        {
            final Response.ResponseBuilder builder = Response.status(HttpResponseStatus.NOT_FOUND.code());
            addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
            return builder.build();
        }
        final Response.ResponseBuilder builder = Response.ok(bookPage);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        addSelfLinksToBooks(bookPage.getBookDTOs());
        addPaging(page, bookPage, builder, search);
        return builder.build();
    }


    @Path("/{isbn}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@Positive @PathParam("isbn") long isbn)
    {
        final var book = this.bookServiceAdapter.loadBookById(isbn);
        final Response.ResponseBuilder builder = Response.ok(book);
        final AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(res.getAuthorizationLevel() == AuthorizationLevel.ADMIN)
        {
            Hyperlinks.addLink(uriInfo, builder, "/library/books/" + isbn, "UpdateBook", MediaType.APPLICATION_JSON);
            Hyperlinks.addLink(uriInfo, builder, "/library/books/" + isbn, "DeleteBook", MediaType.APPLICATION_JSON);
        }
        else if(res.getAuthorizationLevel() == AuthorizationLevel.USER)
        {
            //if(result.getAvailAmount > 0)
            //{
            Hyperlinks.addLink(uriInfo, builder, "/library/users/" + res.getRelatedUserID() + "borrow/" + isbn, "BorrowBook", MediaType.APPLICATION_JSON);
            //}
        }
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }


    @Path("/{isbn}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateOrCreateBook(@Positive @PathParam("isbn") long isbn, @Valid BookDTO book)
    {
        final AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(res.getAuthorizationLevel() == AuthorizationLevel.ADMIN)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        book.setId(isbn);
        int i = this.bookServiceAdapter.updateBook(isbn, book);
        Response.ResponseBuilder r = Response.status(i);
        if(i == PutStatus.CREATED)
        {
            r.header("Location", createLocationHeader(book));
        }
        addDefaultLinksByAuthorizationLevel(uriInfo, r, res);
        return r.build();
    }


    @Path("/{isbn}")
    @DELETE
    public Response deleteBook(@Positive @PathParam("isbn") long isbn)
    {
        final Response.ResponseBuilder builder = Response.status(HttpResponseStatus.NO_CONTENT.code());
        AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(res.getAuthorizationLevel() != AuthorizationLevel.ADMIN)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        this.bookServiceAdapter.deleteBook(isbn);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }






    private String createLocationHeader(BookDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }

    public void addPaging(@DefaultValue("1") @PositiveOrZero int page, BooksResult bookPage, Response.ResponseBuilder builder, String query)
    {
        final String path = query.trim().isEmpty() ? "/library/books?page=" : "/library/books?search=" + query + "&page=";
        if (bookPage.getBookDTOs().size() == 21)
        {
            Hyperlinks.addLink(uriInfo, builder, path + (page + 1), "next", MediaType.APPLICATION_JSON);
            bookPage.getBookDTOs().removeLast();
        }
        if(page > 1)
        {
            Hyperlinks.addLink(uriInfo, builder, path + (page - 1), "prev", MediaType.APPLICATION_JSON);
        }

    }


    public void addSelfLinksToBooks(List<BookDTO> books)
    {
        books.forEach(this::addSelfLinkToBook);
    }

    public void addSelfLinkToBook(BookDTO book)
    {
        final var currentUri = uriInfo.getAbsolutePath();
        final var path = currentUri.getPath();
        final var newPath = path.replaceFirst("/\\d*$", "");
        final var newUri = UriBuilder.fromUri(currentUri).replacePath(newPath + "/" + book.getId()).build();
        book.getSelfLink().setHref(newUri.toASCIIString());
        book.getSelfLink().setRel("self");
        book.getSelfLink().setType(httpHeaders.getHeaderString("Accept"));
    }

    //todo: Update/Creation with put, Delete: ONLY WITH ADMIN PRIVILEGES!!!! Otherwise, return 403 forbidden. hyperlinks also only provided to logged in admins

    // EXAMPLE HYPERLINK MANAGER. TO IMPLEMENT: HYPERLINKS FOR BOOKS (and then for the other webcontrollers too)
    /*
    private String createLocationHeader( UniversityDTO model )
    {
        return uriInfo.getRequestUriBuilder( ).path( Long.toString( model.getId( ) ) ).build( ).toString( );
    }

    private void addSelfLinksToUniversities( List<UniversityDTO> models )
    {
        models.forEach( this::addSelfLinkToUniversity );
    }

    private void addSelfLinkToUniversity( UniversityDTO university )
    {
        final var currentUri = uriInfo.getAbsolutePath( );
        final var path = currentUri.getPath( );
        final var newPath = path.replaceFirst( "/\\d*$", "" );
        final var newUri = UriBuilder.fromUri( currentUri )
                .replacePath( newPath + "/" + university.getId( ) )
                .build( );

        university.getSelfLink( ).setHref( newUri.toASCIIString( ) );
        university.getSelfLink( ).setRel( "self" );
        university.getSelfLink( ).setType( httpHeaders.getHeaderString( "Accept" ) );
    }*/
}