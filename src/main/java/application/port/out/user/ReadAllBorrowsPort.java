package application.port.out.user;

import application.domain.results.BorrowsResult;

public interface ReadAllBorrowsPort
{
    BorrowsResult readAllBorrows(long uid);
}