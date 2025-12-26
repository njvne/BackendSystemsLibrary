package adapters.in.api.adapter;

import adapters.in.api.models.BorrowDTO;

public class BorrowResult
{
    private BorrowDTO borrow;

    public BorrowResult()
    {

    }

    public BorrowResult(BorrowDTO borrow)
    {
        this.borrow = borrow;
    }




    public BorrowDTO getBorrow()
    {
        return borrow;
    }
}