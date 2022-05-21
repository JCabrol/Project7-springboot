package com.nnk.springboot;

import com.nnk.springboot.controllers.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SpringbootApplicationTests {


	@Autowired
	private LoginController controller;

	@Test
	void contextLoads()throws Exception {
		assertThat(controller).isNotNull();
	}
}
