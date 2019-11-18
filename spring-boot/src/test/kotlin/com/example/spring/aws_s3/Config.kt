package com.example.spring.aws_s3

import cloud.localstack.DockerTestUtils
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.amazonaws.services.s3.transfer.model.UploadResult
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.io.File

@Configuration
class Config {

    @Bean
    fun amazonS3(): AmazonS3 {
        return DockerTestUtils.getClientS3()
    }

    @Bean(destroyMethod = "shutdownNow")
    fun transferManager(amazonS3: AmazonS3): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(amazonS3).build()
    }

    @Component
    class FileUploadService(private val transferManager: TransferManager) {

        fun upload(bucket: String, key: String, file: File): UploadResult {
            val upload = transferManager.upload(bucket, key, file)
            return upload.waitForUploadResult()
        }
    }
}