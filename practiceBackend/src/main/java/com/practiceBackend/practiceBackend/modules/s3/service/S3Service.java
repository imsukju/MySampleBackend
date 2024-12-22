package com.practiceBackend.practiceBackend.modules.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
   public AmazonS3Client amazonS3Client;

    @Value(value="${cloud.aws.s3.bucket}")
    private String bucket;
   //S3 버킷에 파일 업로드
    public void upLoadFile()
    {
        ObjectMetadata metadata = new ObjectMetadata();
    }

    public String uploadFileToS3(MultipartFile file)
    {
        try{
            String key = UUID.randomUUID().toString();
            String uuid = "image/" + key;
            return this.uploadFileToS3(file,uuid);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadFileToS3FromPost(MultipartFile file)
    {
        try{
            String key = UUID.randomUUID().toString();
            String uuid = "image/post/" + key;
            return this.uploadFileToS3(file,uuid);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadFileToS3ReturnFileName(MultipartFile file){
        try{
            String key = UUID.randomUUID().toString();
            String uuid = "image/post/" + key;
            return this.uploadReturnFilename(file,uuid);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public String uploadFileToS3(MultipartFile file, String uuid) throws IOException {
        if (this.isValidFile(file)) {
            throw new RuntimeException("File already exists");
        } else {

            //객체의 메타데이터 정보를 담고 있는 객체입니다.
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength((long)file.getSize());

            // public PutObjectResult putObject(String bucketName, String key, InputStream input, ObjectMetadata metadata)

            //bucketName (String):
            //객체가 업로드될 S3 버킷의 이름을 지정합니다.
            //S3 버킷은 객체를 저장하는 논리적 컨테이너이며, 하나의 버킷에 여러 객체를 저장할 수 있습니다.

            //key (String):
            //업로드할 객체의 고유한 이름을 지정합니다.
            //객체의 키는 버킷 내에서 유일해야 하며, S3에서 이 키를 통해 객체를 식별하고 접근합니다.
            // 키는 파일의 경로나 이름처럼 사용할 수 있으며, 예를 들어 "images/photo.jpg" 같은 형식으로 디렉터리 구조처럼 사용할 수 있습니다.

            //input (InputStream):
            //업로드할 데이터의 스트림입니다.
            //객체의 실제 콘텐츠를 포함하고 있으며, 파일, 이미지, 텍스트 데이터 등 다양한 형식의 데이터를 S3에 전송할 수 있습니다.
            // InputStream을 사용해 데이터를 버퍼링하여 전송하므로 대용량 파일도 효율적으로 업로드할 수 있습니다.

            //metadata (ObjectMetadata):
            //객체의 메타데이터 정보를 담고 있는 객체입니다.
            //이 메타데이터에는 Content-Type (파일 MIME 타입), Content-Length (파일 크기), Cache-Control (캐싱 설정) 등의 정보를 포함할 수 있습니다.
            //메타데이터는 S3에 저장된 파일의 속성을 클라이언트에게 전달하며, 이를 통해 클라이언트가 파일을 적절히 처리할 수 있도록 돕습니다.

            this.amazonS3Client.putObject(this.bucket, uuid, file.getInputStream(), metadata);
            return this.amazonS3Client.getUrl(this.bucket, uuid).toString();// 파일을 뺴고 쓰는 url
        }
    }

    public String uploadReturnFilename(MultipartFile file, String uuid) throws IOException {
        if (this.isValidFile(file)) {
            throw new RuntimeException("File already exists");
        } else {

            //객체의 메타데이터 정보를 담고 있는 객체입니다.
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength((long)file.getSize());
            this.amazonS3Client.putObject(this.bucket, uuid, file.getInputStream(), metadata);
            return file.getOriginalFilename();
        }
    }


    public boolean isValidFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        //확장자를 추출하는 방식:
        //fileName.lastIndexOf(".") → 파일 이름에서 마지막 .의 인덱스를 찾음.
        //substring(lastIndex + 1) → 마지막 점 이후의 문자열(확장자)을 반환.
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equals("abc") || extension.equals("ddd");
    }

    public String getfilename(MultipartFile file){
        try{
            String key = UUID.randomUUID().toString();
            String uuid = "image/" + key;
            this.uploadFileToS3(file,uuid);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

//    public String deleteS3file(MultipartFile file){
//        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//    }



    public S3Service(AmazonS3Client amazonS3Client){
        this.amazonS3Client = amazonS3Client;
    }





}
