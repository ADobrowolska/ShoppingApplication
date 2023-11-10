package com.shoppingapp.ShoppingApplication;

import com.shoppingapp.ShoppingApplication.repository.ShoppingListRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ShoppingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(ShoppingApplication.class, args);
		ShoppingListRepository shoppingListRepository = run.getBean(ShoppingListRepository.class);




	}

}
