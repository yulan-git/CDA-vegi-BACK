package com.vegi.vegilabback.config;

import org.springframework.context.annotation.Bean;

public class Mapper {

    @Bean
    public Mapper modelMapper() {
        return new Mapper();
    }

}
