package com.nanoporeqc.r.config;

import org.rosuda.REngine.REngine;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class RConfiguration {

    @Bean
    public RConnection rConnection() throws RserveException {
        return new RConnection();
    }

    @Bean
    public REngine rEngine(RConnection rConnection) {
        return rConnection;
    }
}
