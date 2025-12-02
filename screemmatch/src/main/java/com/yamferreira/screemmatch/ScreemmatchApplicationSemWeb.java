//package com.yamferreira.screemmatch;
//
//import com.yamferreira.screemmatch.principal.Principal;
//import com.yamferreira.screemmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreemmatchApplicationSemWeb implements CommandLineRunner {
//
//	@Autowired
//	private SerieRepository repositorio;
//
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreemmatchApplicationSemWeb.class, args);
//	}
//
//	@Override
//	public void run (String...args) throws Exception {
//		Principal principal = new Principal(repositorio);
//		principal.exibeMenu();
//	}
//
//}
