package raf.sk.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JMSconfig {
    @Bean
    public MessageConverter jacksonMessageConverter() {
        var mc = new MappingJackson2MessageConverter();
        mc.setTypeIdPropertyName("_type");
        mc.setTargetType(MessageType.TEXT);
        mc.setObjectMapper(
                new ObjectMapper()
                        .findAndRegisterModules()
        );
        return mc;
    }

    @Bean
    public JmsListenerContainerFactory<?> configureJmsListener(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer
    ) {
        var factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        factory.setSessionAcknowledgeMode(
                Session.CLIENT_ACKNOWLEDGE
        );

        return factory;
    }
}
