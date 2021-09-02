package com.api.api_meme.Controller;

import com.api.api_meme.Services.MemeApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemeApiController {
  @GetMapping("memes")
  public Object test(){
    return MemeApi.readArticles();
  }
}
