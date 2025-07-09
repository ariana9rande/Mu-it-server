package com.hjh.muit.service;

import com.hjh.muit.entity.dto.SpotifySearchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    public String getAccessToken() {

        String tokenUrl = "https://accounts.spotify.com/api/token";
        String accessToken = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic 인증 헤더 구성
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        accessToken = (String) response.getBody().get("access_token");

        return accessToken;
    }

    public Map<String, Object> getAlbum(String albumId, String accessToken) {
        if (accessToken == null) {
            accessToken = getAccessToken();
        }

        String url = UriComponentsBuilder
                .fromUriString("https://api.spotify.com/v1/albums/{albumId}")
                .buildAndExpand(albumId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "앨범을 찾을 수 없습니다.");
            }
            throw new ResponseStatusException(e.getStatusCode(), "Spotify API 오류: " + e.getMessage());
        }
    }

    public Map<String, Object> getNewReleases(String accessToken, String country, int limit) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.spotify.com/v1/browse/new-releases")
                .queryParam("country", country)
                .queryParam("limit", limit)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "앨범을 찾을 수 없습니다.");
            }
            throw new ResponseStatusException(e.getStatusCode(), "Spotify API 오류: " + e.getMessage());
        }
    }

    public Map<String, Object> search(String accessToken, SpotifySearchRequestDto dto) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.spotify.com/v1/search")
                .queryParam("q", dto.getQ())
                .queryParam("type", String.join(",", dto.getType()))
                .queryParamIfPresent("market", Optional.ofNullable(dto.getMarket()))
                .queryParamIfPresent("limit", Optional.ofNullable(dto.getLimit()))
                .queryParamIfPresent("offset", Optional.ofNullable(dto.getOffset()))
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "검색 결과가 없습니다.");
            }
            throw new ResponseStatusException(e.getStatusCode(), "Spotify API 오류: " + e.getMessage());
        }
    }
}
