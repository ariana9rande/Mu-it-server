package com.hjh.muit.controller;

import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.SpotifySearchRequestDto;
import com.hjh.muit.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
@Slf4j
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/album/{id}")
    public ResponseEntity<ApiResponseDto> getAlbum(@PathVariable("id") String albumId) {

        String accessToken = spotifyService.getAccessToken();

        return ResponseEntity.ok(ApiResponseDto.success("앨범 id로 앨범 정보 가져오기", spotifyService.getAlbum(albumId, accessToken)));
    }

    @GetMapping("/albums/new-releases")
    public ResponseEntity<ApiResponseDto> getNewReleases(@RequestParam(defaultValue = "KR") String country, @RequestParam(defaultValue = "10") int limit) {
        String accessToken = spotifyService.getAccessToken();

        Map<String, Object> result = spotifyService.getNewReleases(accessToken, country, limit);
        return ResponseEntity.ok(ApiResponseDto.success("최신 발매 앨범 정보 가져오기", result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto> search(@ParameterObject @ModelAttribute SpotifySearchRequestDto dto) {
        log.info("dto = {}", dto);

        String accessToken = spotifyService.getAccessToken();
        Map<String, Object> result = spotifyService.search(accessToken, dto);

        return ResponseEntity.ok(ApiResponseDto.success("검색 결과", result));
    }
}
