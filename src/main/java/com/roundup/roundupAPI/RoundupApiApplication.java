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
      return "Hello World!";
    }
    
	public static void main(String[] args) {
		SpringApplication.run(RoundupApiApplication.class, args);
	}

}
