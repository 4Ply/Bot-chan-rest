package com.netply.web.security.oauth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleOAuthValidator {
    boolean validate(String idTokenString) throws GeneralSecurityException, IOException;
}
