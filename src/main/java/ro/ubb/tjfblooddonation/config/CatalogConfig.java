package ro.ubb.tjfblooddonation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ro.ubb.tjfblooddonation.repository", "ro.ubb.tjfblooddonation.service",
         "ro.ubb.tjfblooddonation.utils"})
public class CatalogConfig {


}
