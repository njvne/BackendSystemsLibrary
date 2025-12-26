package application.port.in.user;

import application.domain.models.Borrow;
import application.domain.results.NoContentResult;

public interface CreateBorrowUseCase
{
    NoContentResult create(Borrow borrow);
}