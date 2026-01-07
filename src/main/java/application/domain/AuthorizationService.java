package application.domain;

import application.domain.Authorisation.AuthorizationResult;
import application.port.in.FindAuthorisationUseCase;
import application.port.out.FindAuthorisationPort;
import jakarta.inject.Inject;

public class AuthorizationService implements FindAuthorisationUseCase
{
    @Inject
    private FindAuthorisationPort findAuthorisationPort;

    @Override
    public AuthorizationResult checkAuthorisation(String username, String password)
    {
        return this.findAuthorisationPort.checkPasswordCombination(username, password);
    }
}