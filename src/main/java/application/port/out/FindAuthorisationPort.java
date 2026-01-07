package application.port.out;

import application.domain.Authorisation.AuthorizationResult;

public interface FindAuthorisationPort
{
    AuthorizationResult checkPasswordCombination(String username, String password);
}