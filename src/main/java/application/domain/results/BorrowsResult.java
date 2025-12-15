package application.domain.results;

import application.domain.models.Borrow;

import java.util.List;

public class BorrowsResult extends CollectionModelResult<Borrow>
{
    public BorrowsResult()
    {

    }

    public BorrowsResult(List<Borrow> result)
    {
        super(result);
    }
}