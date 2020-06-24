package upcafe.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	  private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

	  public CustomRequestEntityConverter() {
	      defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
	  }

	  @Override
	  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
	      RequestEntity<?> entity = defaultConverter.convert(req);
	      MultiValueMap<String, String> params =  (MultiValueMap<String, String>) entity.getBody();
	      String url = params.getFirst("redirect_uri");
	      if(url.contains("facebook")){
	          url = url.replace("http", "https");
	      }
	      params.set("redirect_uri", url);
	      System.out.println("Callback Request Parameters: "+params.toSingleValueMap().toString());
	      return new RequestEntity<>(params, entity.getHeaders(), 
	        entity.getMethod(), entity.getUrl());
	  }

	}
