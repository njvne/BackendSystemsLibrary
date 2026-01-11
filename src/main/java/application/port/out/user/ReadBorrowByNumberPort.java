package application.port.out.user;

import application.domain.results.BorrowResult;

public interface ReadBorrowByNumberPort
{
    BorrowResult readBorrowByNumber(long borrowNumber);
}