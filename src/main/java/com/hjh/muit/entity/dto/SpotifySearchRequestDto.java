package com.hjh.muit.entity.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SpotifySearchRequestDto {

    @Schema(description = "검색어 (예: 'Ariana Grande', 'Kendrick lamar', 'genre:\"pop\"')", example = "Ariana Grande")
    @NotBlank(message = "검색어는 필수입니다.")
    private String q;


    @ArraySchema(
            schema = @Schema(
                    description = "검색 유형",
                    allowableValues = {"album", "artist", "track", "playlist", "show", "episode", "audiobook"},
                    example = "[\"album, artist\"]"
            )
    )
    @NotBlank(message = "검색 유형은 필수입니다.")
    private List<String> type;

    @Schema(description = "국가 코드 (예: KR, US)", example = "KR")
    private String market;

    @Schema(description = "결과 개수 (1~50)", example = "10", minimum = "1", maximum = "50")
    private Integer limit;

    @Schema(description = "페이지 시작 위치", example = "0", minimum = "0", maximum = "1000")
    private Integer offset;

}
