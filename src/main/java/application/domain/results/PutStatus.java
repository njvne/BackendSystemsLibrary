package application.domain.results;

public interface PutStatus
{
    int CREATED = 201;
    int UPDATED = 204;
    int RESOURCECONFLICT = 409;
    int ERROR = 500;
}
