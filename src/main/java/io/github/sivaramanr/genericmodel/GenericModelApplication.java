package io.github.sivaramanr.genericmodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"io.github.sivaramanr.genericmodel"})
public class GenericModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenericModelApplication.class, args);
    }

}
