package application.port.in.user;

import application.domain.results.NoContentResult;

public interface ReturnBookUseCase
{
    NoContentResult returnBook(long uid, long borrowNumber);
}