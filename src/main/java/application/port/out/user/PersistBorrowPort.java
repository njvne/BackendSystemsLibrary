package application.port.out.user;

import application.domain.models.Borrow;
import application.domain.results.NoContentResult;

public interface PersistBorrowPort
{
    NoContentResult createBorrow(Borrow borrow);
}