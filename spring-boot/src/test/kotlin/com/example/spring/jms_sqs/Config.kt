package com.example.spring.jms_sqs

import cloud.localstack.DockerTestUtils
import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.services.sqs.AmazonSQS
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.JmsException
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.destination.DynamicDestinationResolver
import org.springframework.stereotype.Component
import javax.jms.Session

private val Logger = KotlinLogging.logger {}


@EnableJms
@Configuration
class Config {

    @Bean
    fun amazonSQS(): AmazonSQS {
        return DockerTestUtils.getClientSQS()
    }

    // TODO: 本番のコードから AmazonSQS と ProviderConfiguration だけ置き換えたいので @Bean で分けたい
    @Bean
    fun sqsConnectionFactory(sqs: AmazonSQS): SQSConnectionFactory {
        return SQSConnectionFactory(ProviderConfiguration(), sqs)
    }

    @Bean
    fun jmsTemplate(factory: SQSConnectionFactory): JmsTemplate {
        return JmsTemplate(factory)
    }

    @Bean
    fun jmsListenerContainerFactory(factory: SQSConnectionFactory): DefaultJmsListenerContainerFactory {
        return DefaultJmsListenerContainerFactory().apply {
            setConnectionFactory(factory)
            setDestinationResolver(DynamicDestinationResolver())
            // テスト側から変更したい感がある
            setConcurrency("1")
            setMaxMessagesPerTask(1)
            setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE)
        }
    }

    @Component
    class Consumer {

        @JmsListener(destination = "test")
        @Throws(JmsException::class)
        fun receive(message: String) {
            Logger.info { "message received: $message" }
        }
    }
}