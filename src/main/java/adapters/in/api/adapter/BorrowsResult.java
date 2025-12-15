package adapters.in.api.adapter;

import adapters.in.api.models.BorrowDTO;
import java.util.List;

public class BorrowsResult 
{
    private List<BorrowDTO> borrowDTOs;



    public BorrowsResult(){}

    public BorrowsResult(List<BorrowDTO> borrowDTOs)
    {
        this.borrowDTOs = borrowDTOs;
    }



    public List<BorrowDTO> getBorrowDTO()
    {
        return borrowDTOs;
    }
}