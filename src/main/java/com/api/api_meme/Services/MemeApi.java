package com.api.api_meme.Services;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MemeApi {
  private static String getAuthToken() {
    String CLIENT_ID = "pEp5Csmni0rPiiPiURtcMw";
    String SECRET_KEY = "5Ii5ZQb_HotdWk-T3QWMP5PRQ5APRw";
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(CLIENT_ID, SECRET_KEY);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.put("User-Agent", Collections.singletonList("tomcat:com.e4developer.e4reddit-test:v1.0 (by /u/bartoszjd)"));
    String body = "grant_type=client_credentials";
    HttpEntity<String> request = new HttpEntity<>(body, headers);
    String authUrl = "https://www.reddit.com/api/v1/access_token";
    ResponseEntity<String> response = restTemplate.postForEntity(authUrl, request, String.class);
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = new HashMap<>();
    try {
      map.putAll(mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
      }));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return String.valueOf(map.get("access_token"));
  }

  public static Object readArticles() {
    String subReddit = extractedRandomSubreddit();
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String authToken = getAuthToken();
    headers.setBearerAuth(authToken);
    headers.put("User-Agent", Collections.singletonList("tomcat:com.e4developer.e4reddit-test:v1.0 (by /u/bartoszjd)"));
    ResponseEntity<String> response = extractedResponse(subReddit, restTemplate, headers);
    return extractedBodyRandom(response);
  }

  private static Object extractedBodyRandom(ResponseEntity<String> response) {
    JSONObject jsonObject = new JSONObject(response.getBody());
    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
    JSONArray jsonArray = jsonObject2.getJSONArray("children");
    List<Object> list = jsonArray.toList();
    int index = ThreadLocalRandom.current().nextInt(0, list.size());
    return list.get(index);
  }

  private static ResponseEntity<String> extractedResponse(String subReddit, RestTemplate restTemplate, HttpHeaders headers) {
    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
    String url = "https://oauth.reddit.com/r/" + subReddit + "/hot";
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    return response;
  }

  private static String extractedRandomSubreddit() {
    List<String> subredditList = List.of(
      "orochinho","ShitpostBR","MemesBrasil",
      "ballutverso","G0ularte","SaikoReddit",
      "Orochisegundo");
    int subRedditIndex = ThreadLocalRandom.current().nextInt(0, subredditList.size());
    // System.out.println(subRedditIndex);
    String subReddit = subredditList.get(subRedditIndex);
    return subReddit;
  }
}
