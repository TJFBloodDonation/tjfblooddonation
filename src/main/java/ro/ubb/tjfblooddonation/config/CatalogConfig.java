package ro.ubb.tjfblooddonation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ro.ubb.tjfblooddonation.repository", "ro.ubb.tjfblooddonation.service", "ro.ubb.tjfblooddonation.ui"})
public class CatalogConfig {


}
