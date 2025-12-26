package application.domain.results;

import application.domain.models.Borrow;

public class BorrowResult extends SingleModelResult<Borrow>
{
    public BorrowResult()
    {
        super();
    }

    public BorrowResult(Borrow borrow)
    {
        super(borrow);
    }
}