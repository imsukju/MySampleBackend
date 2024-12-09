package com.practiceBackend.practiceBackend.modules.post.dto;
import com.practiceBackend.practiceBackend.entity.Attachment;
import lombok.*;

@Getter
@Setter
@Builder
public class AttachmentResponseDTO {
    String s3Url;
    String realFileName;

    public static AttachmentResponseDTO convertToDTO(Attachment attachment) {
        return AttachmentResponseDTO.builder()
                .realFileName(attachment.getFilename())
                .s3Url(attachment.getS3url())
                .build();
    }
}
