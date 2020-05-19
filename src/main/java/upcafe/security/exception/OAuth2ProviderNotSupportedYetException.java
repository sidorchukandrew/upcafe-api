package upcafe.security.exception;


public class OAuth2ProviderNotSupportedYetException extends RuntimeException{

    public OAuth2ProviderNotSupportedYetException(String provider) {
        super("The provider "  + provider + " is not supported yet.");
    }
}

