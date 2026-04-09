package org.example;

import org.example.service.PainelTaticoMissaoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class AventureiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(AventureiroApplication.class, args);
    }

    @Bean
    CommandLineRunner testarCache(PainelTaticoMissaoService service) {
        return args -> {
            System.out.println(" CHAMADA 1 (Vai no banco):");
            service.buscarTop10MissoesUltimos15Dias();

            System.out.println(" CHAMADA 2 (Tem que vir do cache):");
            service.buscarTop10MissoesUltimos15Dias();
        };
    }
}