package com.roundup.roundupAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class RoundupApiApplication {

    @RequestMapping("/")
    @ResponseBody
    public String home() {
      return "<h1>Hello World!</h1>";
    }
    
	public static void main(String[] args) {
		SpringApplication.run(RoundupApiApplication.class, args);
	}

}
