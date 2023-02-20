package com.tikitaka.tikitaka.infra.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.BadRequestException;
import com.tikitaka.tikitaka.global.error.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    public List<String> uploadImage(List<MultipartFile> multipartFiles, String dirName) {
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename(), dirName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new InternalServerException(ErrorCode.FILE_UPLOAD_FAILED);
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }


    /**
     * 파일 명 앞에 Random UUID 를 붙인 파일명을 반환합니다
     * @param fileName 파일 명
     * @param dirName 디렉토리 명
     * @return BaseURL 제외한 디렉토리명 과 UUID 를 합한 최종 파일 경로
     * */
    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + getFileExtension(fileName);
    }

    /**
     * 파일 확장자를 가져옵니다
     * @param fileName 파일 명
     * @return 파일 확장자
     * */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new BadRequestException(ErrorCode.INVALID_FILE_EXTENSION);
        }
    }
}