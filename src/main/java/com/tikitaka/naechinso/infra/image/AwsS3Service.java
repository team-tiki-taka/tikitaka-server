package com.tikitaka.naechinso.infra.image;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface AwsS3Service {

    /**
     * 파일 여러 개를 리스트로 가져와 업로드한 파일명 리스트를 반환합니다
     * @param multipartFiles 멀티 파트 파일 헤더 설정한 업로드 파일 리스트
     * @param dirName 업로드 디렉토릴 경로
     * @return fileNameList BaseURL 을 제외한 파일 경로 리스트
     * */
    List<String> uploadImage(List<MultipartFile> multipartFiles, String dirName);

    /**
     * 지정한 파일 명에 해당하는 파일을 S3 버킷에서 제거합니다
     * @param fileName 디렉토리 경로를 포함한 파일명
     * */
    void deleteImage(String fileName);
}
