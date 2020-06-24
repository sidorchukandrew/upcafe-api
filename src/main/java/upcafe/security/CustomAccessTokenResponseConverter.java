package upcafe.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

public class CustomAccessTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

	private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<>(Arrays.asList(
			OAuth2ParameterNames.ACCESS_TOKEN,
			OAuth2ParameterNames.TOKEN_TYPE,
			OAuth2ParameterNames.EXPIRES_IN,
			OAuth2ParameterNames.REFRESH_TOKEN,
			OAuth2ParameterNames.SCOPE
			));
	
	@Override
	public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
		
		System.out.println("\n\n-----------------------------------------------------------------------------------------\n|\n|\n|\n|\n|\n|\n");
		System.out.println("\t" + tokenResponseParameters);
		System.out.println("\n|\n|\n|\n|\n|\n|\n-----------------------------------------------------------------------------------------\n\n");
		
		String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);

		OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;
		long expiresIn = 0;
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
			try {
				expiresIn = Long.parseLong(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));
			} catch (NumberFormatException ex) { }
		}

		Set<String> scopes = Collections.emptySet();
		if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
			String scope = tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
			scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		String refreshToken = tokenResponseParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);

		Map<String, Object> additionalParameters = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : tokenResponseParameters.entrySet()) {
			if (!TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) {
				additionalParameters.put(entry.getKey(), entry.getValue());
			}
		}

		return OAuth2AccessTokenResponse.withToken("")
				.build();
	}

}


// This redirect failed because the redirect URI is not whitelisted in the app’s Client OAuth Settings. Make sure Client and Web OAuth Login are on and add all your app domains as Valid OAuth Redirect URIs.