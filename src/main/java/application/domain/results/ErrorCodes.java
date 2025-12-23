package application.domain.results;

public interface ErrorCodes     //CONSIDER WRITING THIS AS ENUM
{
    int RESOURCE_TO_DELETE_NOT_FOUND = 404;
    int RESOURCE_ID_DOES_NOT_MATCH = 406;
    int RESOURCE_TO_UPDATE_NOT_FOUND = 404;
    int RESOURCE_CONFLICT = 409;
    int RESOURCE_NOT_FOUND = 404;
}