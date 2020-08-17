package com.nest.zerg.task;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class IncubatorTask implements ApplicationListener<ContextRefreshedEvent> {
    
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent()!= null) {
         
			System.out.println("test ApplicationListener");

        }
	}
}
