package application.port.in.user;

import application.domain.results.BorrowResult;

public interface LoadUserBorrowByNumberUseCase
{
    BorrowResult loadBorrowByNumber(long borrowNumber);
}