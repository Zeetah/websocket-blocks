package com.zeetah.blocks.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.EnableWebSocketMessageBroker;
import org.springframework.messaging.simp.config.MessageBrokerConfigurer;
import org.springframework.messaging.simp.config.StompEndpointRegistry;
import org.springframework.messaging.simp.config.WebSocketMessageBrokerConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@ComponentScan(basePackages="com.zeetah.blocks")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/blocks").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerConfigurer configurer) {
		configurer.enableSimpleBroker("/queue/", "/topic/");
		configurer.setAnnotationMethodDestinationPrefixes("/app");
	}

}
