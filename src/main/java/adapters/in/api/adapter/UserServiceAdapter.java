package adapters.in.api.adapter;

import adapters.in.api.Exceptions.ResourceConflictException;
import adapters.in.api.models.BorrowDTO;
import adapters.in.api.models.UserDTO;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.port.in.user.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;


@ApplicationScoped
public class UserServiceAdapter
{
    @Inject
    CreateBorrowUseCase createBorrowUseCase;

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    LoadAllBorrowsUseCase loadAllBorrowsUseCase;

    @Inject
    LoadUserBorrowByNumberUseCase loadUserBorrowByIdUseCase;

    @Inject
    LoadUserByIdUseCase loadUserByIdUseCase;

    @Inject
    ReturnBookUseCase returnBookUseCase;

    private Mapper mapper;


    public BorrowResult createBorrow(BorrowDTO borrowDTO)
    {
        final var domainResult = this.createBorrowUseCase.create(this.mapper.borrowDTOToDomainModel(borrowDTO));
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_UNAVAILABLE)
            {
                throw new ResourceConflictException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
        else
        {
            return new BorrowResult(borrowDTO);
        }
    }

    public UserResult createUser(UserDTO userDTO)
    {
        final var domainResult = this.createUserUseCase.create(this.mapper.userToDomainModel(userDTO));
        if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new UserResult(userDTO);
        }
    }

    public BorrowsResult getAllBorrows(long uid)
    {
        final var domainResult = this.loadAllBorrowsUseCase.loadAllBorrows(uid);
        if (domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new BorrowsResult(this.mapper.borrowsToApiModels(domainResult.getResult()));
        }
    }

    public BorrowResult getBorrowByNumber(long uid, long number)
    {
        final var domainResult = this.loadUserBorrowByIdUseCase.loadBorrowByNumber(uid, number);

        if(domainResult.isEmpty())
        {
            throw new NotFoundException( );
        }
        else if ( domainResult.hasError( ) )
        {
            throw new InternalServerErrorException( domainResult.getErrorMessage( ) );
        }
        else
        {
            return new BorrowResult(this.mapper.borrowToApiModel(domainResult.getResult()));
        }
    }

    public UserResult getUserById(long id)
    {
        final var domainResult = this.loadUserByIdUseCase.loadUserById(id);

        if(domainResult.isEmpty())
        {
            throw new NotFoundException();
        }
        else if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new UserResult( this.mapper.userToApiModel(domainResult.getResult()));
        }
    }

    public void returnBook(long uid, long borrowNum)
    {
        final var domainResult = this.returnBookUseCase.returnBook(uid, borrowNum);
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_TO_UPDATE_NOT_FOUND)
            {
                throw new NotFoundException(domainResult.getErrorMessage());
            }
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_CONFLICT)
            {
                throw new ResourceConflictException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
    }
}