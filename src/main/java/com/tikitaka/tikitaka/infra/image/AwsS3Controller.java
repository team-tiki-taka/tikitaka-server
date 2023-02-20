package com.tikitaka.tikitaka.infra.image;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.annotation.AuthMember;
import com.tikitaka.tikitaka.global.config.CommonApiResponse;
import com.tikitaka.tikitaka.infra.image.constant.AwsS3Directory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Amazon S3 이미지 업로드 및 삭제 기능을 담당합니다
 * */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3ServiceImpl awsS3ServiceImpl;

    /**
     * Amazon S3에 이미지 업로드
     * @return 성공 시 200 / 함께 업로드 된 파일의 파일명 리스트 반영
     */
    @ApiOperation(value = "Amazon S3에 이미지를 업로드한다", notes = "Amazon S3에 이미지 업로드 ")
    @PostMapping(value = "/image/{dir}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonApiResponse<List<String>> uploadImage(
            @PathVariable("dir") AwsS3Directory dirName,
            @Parameter(
                    description = "업로드 할 파일 리스트",
                    // Won't work without OCTET_STREAM as the mediaType.
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart List<MultipartFile> multipartFiles,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(awsS3ServiceImpl.uploadImage(multipartFiles, dirName.name()));
    }

    /**
     * Amazon S3에 이미지 업로드 된 파일을 삭제
     * @return 성공 시 200 Success
     */
    @ApiOperation(value = "[Admin] Amazon S3에 업로드 된 파일을 삭제한다", notes = "Amazon S3에 업로드된 이미지 삭제")
    @DeleteMapping("/image/{file}")
    public CommonApiResponse<Void> deleteImage(
            @ApiParam(value="삭제할 서버 내 파일 명", required = true)
            @PathVariable("file") String fileName
    ) {
        awsS3ServiceImpl.deleteImage(fileName);
        return CommonApiResponse.of(null);
    }

}