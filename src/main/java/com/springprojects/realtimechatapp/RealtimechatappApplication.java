package com.springprojects.realtimechatapp;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class RealtimechatappApplication {

	public static void main(String[] args) {

		SpringApplication.run(RealtimechatappApplication.class, args);
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
