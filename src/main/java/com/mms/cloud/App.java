package com.mms.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mms.quartz.service.QuartzTableService;

@SpringBootApplication
@ComponentScan(basePackages={"com.mms","com.quartz"})  
//@ImportResource("classpath:/META-INF/spring.xml")
public class App implements  CommandLineRunner
{
	
    @Autowired
    QuartzTableService quartzTableService;
    
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    	
    }
    
    @Override
    public void run(String... strings) throws Exception {
        quartzTableService.startJobs();
    }
}
