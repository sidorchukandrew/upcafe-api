package upcafe.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
//	@PreAuthorize("hasRole('STAFF')")
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
	}

	@Override
//	@PreAuthorize("hasRole('STAFF')")
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/new", "/active", "/ready", "/complete");
		registry.setApplicationDestinationPrefixes("/app");
	}

}
