package application.port.out.user;

import application.domain.results.NoContentResult;

public interface ReturnBookPort
{
    NoContentResult returnBook(long uid, long borrowNumber);
}