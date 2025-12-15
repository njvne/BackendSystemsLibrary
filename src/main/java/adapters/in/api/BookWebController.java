package adapters.in.api;

import adapters.in.api.adapter.BookServiceAdapter;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;


@Path("library/books")    //maybe seperate controllers for users, books, etc.? Refer to Unit 17 CRUD
public class BookWebController
{
    @Inject
    private BookServiceAdapter bookServiceAdapter;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByAuthor(@QueryParam("author") String author)
    {
        //list of books = [load all books with matching author (?! exact or containsIgnoreCase)]
        return Response.ok(/*list of books*/"authorrequest " + author).build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks()                                               //this could accept a path parameter, e.g. to search multiple attributes for the same thing
    {                                                                           //paging or offset+size parameters?
        //list of books = [load all books from database as json application]
        return Response.ok(/*list of books*/"paha").build();
    }



    @Path("/{id: \\d+}")
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

    //todo: Update, Delete, Post: ONLY WITH ADMIN PRIVILEGES!!!! Otherwise, return 403 forbidden. hyperlinks also only provided to logged in admins

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