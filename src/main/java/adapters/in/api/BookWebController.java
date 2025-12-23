package adapters.in.api;

import adapters.in.api.Authorisation.AuthorizationLevel;
import adapters.in.api.Authorisation.AuthorizationResult;
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

import java.net.URI;
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
    {           //[?! exact or containsIgnoreCase (done in persistence layer)]
        final var bookPage = search.trim().isEmpty() ? bookServiceAdapter.getAllBooks(page) : bookServiceAdapter.getBooksByQuery(page, search.trim());
        if(bookPage.getBookDTOs().isEmpty())
        {
            return Response.status(HttpResponseStatus.NOT_FOUND.code()).entity("No books found").build();
        }
        final Response.ResponseBuilder builder = Response.ok(bookPage);
        AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
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
            Hyperlinks.addLink(uriInfo, builder, "/library/users/" + res.getRelatedUserID() + "reservations/" + isbn, "ReserveBook", MediaType.APPLICATION_JSON);
        }
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }



    @Path("/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateBook(@Positive @PathParam("id") long id, @Valid BookDTO book)
    {
        final AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(res.getAuthorizationLevel() != AuthorizationLevel.ADMIN)
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(Math.random() > 0.5)   //getBookById == null
        {
            final var result = bookServiceAdapter.createNewBook(book, id);
            final Response.ResponseBuilder builder = Response.created(createLocationUri(result.getBookDTO()));
            addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
            return builder.build();
        }
        else {
            //final var result = bookServiceAdapter.updateBook(id, bookdto);
            final Response.ResponseBuilder builder = Response.ok();
            addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
            return builder.build();
        }
    }

    @Path("/{isbn}")
    @DELETE
    public Response deleteBook(@Positive @PathParam("isbn") long isbn)
    {
        final Response.ResponseBuilder builder = Response.status(HttpResponseStatus.NO_CONTENT.code());
        AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        if(res.getAuthorizationLevel() == AuthorizationLevel.ADMIN)
        {
            this.bookServiceAdapter.deleteBook(isbn);
        }
        else
        {
            return Response.status(HttpResponseStatus.UNAUTHORIZED.code()).build();
        }
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }




    private URI createLocationUri(BookDTO book)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(book.getId())).build();
    }

    private String createLocationHeader(BookDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }


    public void addPaging(@DefaultValue("1") @PositiveOrZero int page, BooksResult bookPage, Response.ResponseBuilder builder, String query)
    {
        final String path = query.trim().isEmpty() ? "/library/books?page=" : "/library/books?search=" + query + "&page=";
        if (page == 1)
        {
            Hyperlinks.addLink(uriInfo, builder, path + (page + 1), "next", MediaType.APPLICATION_JSON);
        }
        else if(bookPage.getBookDTOs().size() < 21 || bookServiceAdapter.getAllBooks(page + 1).getBookDTOs().isEmpty())  //how to not hardcode server-set size of pages?? Problem if its 20 and next page doesn't have any more books.
        {
            if(page > 1)
            {
                Hyperlinks.addLink(uriInfo, builder, path + (page - 1), "prev", MediaType.APPLICATION_JSON);
            }
        }
        else
        {
            Hyperlinks.addLink(uriInfo, builder, path + (page - 1), "prev", MediaType.APPLICATION_JSON);
            Hyperlinks.addLink(uriInfo, builder, path + (page + 1), "next", MediaType.APPLICATION_JSON);
        }
        bookPage.getBookDTOs().removeLast();    //get 21 to check wether there is a next element but send only 20
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

    //papa


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