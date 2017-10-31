package com.david.musicgroup.rest;

import com.david.musicgroup.response.GreetingResponse;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/artist")
public class ArtistDescriptionRestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody GreetingResponse sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new GreetingResponse(counter.incrementAndGet(), String.format(template, name));
    }
}
