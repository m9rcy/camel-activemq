package com.example.demo;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ActiveMQConnectionFactory cf() {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUserName("webadmin");
        cf.setPassword("webadmin");
        return cf;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JmsPoolConnectionFactory connectionFactory(ConnectionFactory cf) {
        JmsPoolConnectionFactory jmsCf = new JmsPoolConnectionFactory();
        jmsCf.setConnectionFactory(cf);
        jmsCf.setConnectionIdleTimeout(1000);
        return jmsCf;
    }

    @Bean
    public JmsComponent jmsComponent(JmsPoolConnectionFactory connectionFactory) throws JMSException {
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(connectionFactory);
        jmsConfiguration.setConcurrentConsumers(1);

        // Create the Camel JMS component and wire it to our connectionfactory
        JmsComponent jms = new JmsComponent();
        jms.setConfiguration(jmsConfiguration);
        jms.setConnectionFactory(connectionFactory);

        return jms;
    }

    //@Bean
    public JmsComponent alljmsComponent() throws JMSException {
        // Create the connectionfactory which will be used to connect to
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUserName("webadmin");
        cf.setPassword("webadmin");

        JmsPoolConnectionFactory jmsCf = new JmsPoolConnectionFactory();
        jmsCf.setConnectionFactory(cf);
        jmsCf.setConnectionIdleTimeout(1000);

        // Create the Camel JMS component and wire it to our connectionfactory
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(jmsCf);
        return jms;
    }

}
