package com.gdr.puretawny.dataLoader.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.gdr.puretawny.config", "com.gdr.puretawny.dataLoader","com.gdr.puretawny.dataexport" })
public class DataLoaderConfig {

}
