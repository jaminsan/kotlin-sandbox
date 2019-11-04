package com.example.spring.jms_sqs

import cloud.localstack.docker.LocalstackDockerExtension
import cloud.localstack.docker.annotation.LocalstackDockerProperties
import com.amazonaws.services.sqs.AmazonSQS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.ContextConfiguration

@ExtendWith(LocalstackDockerExtension::class)
@LocalstackDockerProperties(services = ["sqs:4576", "s3:4572"])
@ContextConfiguration(classes = [Config::class])
@SpringBootTest
class ConsumerTest {

    @Autowired
    lateinit var amazonSQS: AmazonSQS

    @Autowired
    lateinit var jmsTemplate: JmsTemplate

    @BeforeEach
    fun setUp() {
        // 可視性タイムアウトとか設定したい
        // 同時実行とかの制御もしたい
        amazonSQS.createQueue("test")
    }

    @Test
    fun `put message to sqs`() {
        jmsTemplate.send("test") { session ->
            println(">>>>> put message")
            session.createTextMessage("test message")
        }
    }

}