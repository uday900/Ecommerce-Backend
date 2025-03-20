package com.uday.configuration;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import java.util.Properties;


//@Configuration
public class EnvConfig {
//	
//	@Bean
//	public PropertySource<?> dotenvPropertySource(){
//		Dotenv dotenv = Dotenv.load();
//		Properties properties = new Properties();
//		
//		dotenv.entries().forEach( entry -> properties.put(entry.getKey(), entry.getValue()));
//		
//        return new PropertiesPropertySource("dotenvProperties", properties);
//
//	}
	
}
