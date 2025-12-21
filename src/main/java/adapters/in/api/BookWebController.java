package adapters.in.api;

import adapters.in.api.adapter.BookServiceAdapter;
import adapters.in.api.adapter.BooksResult;
import adapters.in.api.models.BookDTO;
import adapters.in.api.utils.Hyperlinks;
import application.domain.results.NoContentResult;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
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

    @Inject
    private Validator validator;

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
        addSelfLinksToBooks(bookPage.getBookDTOs());
        addPaging(page, bookPage, builder, "");
        return builder.build();
    }
    /*@GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks(@DefaultValue("1") @Positive @QueryParam("page") int page)
    {                                                                           //paging or offset+size parameters?
        final var bookPage = bookServiceAdapter.getAllBooks(page);
        final Response.ResponseBuilder builder = Response.ok(bookPage);
        addSelfLinksToBooks(bookPage.getBookDTOs());
        addPaging(page, bookPage, builder);
        return builder.build();
    }*/



    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@Positive @PathParam("id") long id)
    {
        String book = null;
        //book = [find wether that book exists]
        if(book == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(/*the book if found*/"book by id request " + id).build();
    }



    @Path("/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateBook(@Positive @PathParam("id") long id, @Valid BookDTO book)
    {
        //if(getBookById == null)
        //{
            final var result = bookServiceAdapter.createNewBook(book, id);
            return Response.status(HttpResponseStatus.CREATED.code()).header("location", createLocationHeader(result.getBookDTO())).build();
        //}
        /*else
        {
            final var result = book ServiceAdapter.updateBook(id, bookdto);
            return Response.status(HttpResponseStatus.NO_CONTENT.code()).build();
         }
        */
    }

    @Path("/{id}")
    @DELETE
    public Response deleteBook(@Positive @PathParam("id") long id)
    {
        final var res = new NoContentResult(); //this.bookServiceAdapter.deleteById(id);
        if(res.hasError())
        {
            final Response.ResponseBuilder builder = Response.status(res.getErrorCode()).entity(res.getErrorMessage());
            //addBasicLinks();
        }
        final Response.ResponseBuilder builder = Response.status(HttpResponseStatus.NO_CONTENT.code());
        //addBasicLinks();
        return builder.build();
    }


    private String createLocationHeader(BookDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }

    public void addPaging(@DefaultValue("1") @PositiveOrZero int page, BooksResult bookPage, Response.ResponseBuilder builder, String query)
    {
        String path = query.trim().isEmpty() ? "/library/books?page=" : "/library/books?search=" + query + "&page=";
        if (page == 1)
        {
            Hyperlinks.addLink(uriInfo, builder, path + (page + 1), "next", MediaType.APPLICATION_JSON);
        }
        else if(bookPage.getBookDTOs().size() < 20 || bookServiceAdapter.getAllBooks(page + 1).getBookDTOs().isEmpty())  //how to not hardcode server-set size of pages?? Problem if its 20 and next page doesn't have any more books.
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