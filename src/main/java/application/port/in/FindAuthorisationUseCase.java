package application.port.in;

import application.domain.Authorisation.AuthorizationResult;

public interface FindAuthorisationUseCase
{
    public AuthorizationResult checkAuthorisation(String username, String password);
}