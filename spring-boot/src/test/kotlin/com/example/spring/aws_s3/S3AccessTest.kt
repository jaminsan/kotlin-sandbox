package com.example.spring.aws_s3

import cloud.localstack.docker.LocalstackDockerExtension
import cloud.localstack.docker.annotation.LocalstackDockerProperties
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.S3Object
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths


@ExtendWith(LocalstackDockerExtension::class)
@LocalstackDockerProperties(services = ["s3:4572"])
@ContextConfiguration(classes = [Config::class])
@SpringBootTest
class S3AccessTest {

    @Autowired
    lateinit var amazonS3: AmazonS3

    @Autowired
    lateinit var uploadService: Config.FileUploadService

    private val baseDir = "src/test/resources/s3"
    private val bucketName = "test"

    @BeforeEach
    fun setUp() {
        amazonS3.createBucket(bucketName)
        Files.createDirectory(Paths.get(baseDir))
    }

    @AfterEach
    fun tearDown() {
        File(baseDir).deleteRecursively()
    }

    @Test
    fun `upload file to s3`() {
        val file = File("${baseDir}/test.txt")
        file.writeText("hogehoge")

        uploadService.upload("test", "test.txt", file)

        assert(amazonS3.doesObjectExist("test", "test.txt"))
        val s3Object: S3Object = amazonS3.getObject("test", "test.txt")
        assert(BufferedReader(InputStreamReader(s3Object.objectContent)).readLine() == "hogehoge")
    }
}