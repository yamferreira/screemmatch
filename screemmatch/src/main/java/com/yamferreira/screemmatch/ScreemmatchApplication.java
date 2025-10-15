package com.yamferreira.screemmatch;

import com.yamferreira.screemmatch.model.DadosEpisodios;
import com.yamferreira.screemmatch.model.DadosSerie;
import com.yamferreira.screemmatch.principal.Principal;
import com.yamferreira.screemmatch.service.ConsumoApi;
import com.yamferreira.screemmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreemmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreemmatchApplication.class, args);
	}

	@Override
	public void run (String...args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}

}
