package com.practiceBackend.practiceBackend.modules.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accesskey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretkey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {

        //AWS API를 호출하기 위해 필요한 **고정된 AWS 자격 증명(Access Key ID와 Secret Access Key)**을 포함하는 객체입니다
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);


        return (AmazonS3Client)
                //AmazonS3를 위한 Fluent Builder입니다. 동기 및 비동기 클라이언트를 생성할 수 있으며, 클라이언트 클래스의 생성자를 사용하는 것보다 Builder를 사용하는 것이 권장됩니다.
                AmazonS3ClientBuilder.standard()

                //Amazon S3는 글로벌 서비스이지만, 버킷은 특정 리전(Region)에 종속됩니다.
                //이 메서드는 생성되는 S3 클라이언트가 특정 AWS 리전을 대상으로 동작하도록 설정합니다.
                //region은 AWS SDK에서 제공하는 Regions Enum 타입을 사용하거나, 문자열로 특정 리전을 지정할 수 있습니다.
                .withRegion(region)


                //AWSCredentialsProvider는 AWS SDK에서 사용되는 인터페이스로, 애플리케이션이 AWS API 호출을 인증하는 데 필요한
                // **AWS 자격 증명(AWS Access Key ID와 Secret Access Key)**을 제공하는 역할을 합니다.

                //클라이언트가 AWS 리소스에 접근하려면 자격 증명이 필요합니다.
                //withCredentials 메서드는 클라이언트에 자격 증명을 제공하는 AWSCredentialsProvider를 설정합니다.
                //AWSStaticCredentialsProvider는 고정된(Static) AWS 자격 증명을 제공하는 구현체입니
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}