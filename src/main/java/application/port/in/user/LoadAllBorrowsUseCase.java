package application.port.in.user;

import application.domain.results.BorrowsResult;

public interface LoadAllBorrowsUseCase
{
    BorrowsResult loadAllBorrows(long uid);
}