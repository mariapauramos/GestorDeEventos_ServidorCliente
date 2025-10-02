package com.businessdevelop.POCEvento;

import com.businessdevelop.POCEvento.controller.ServicioEvento;
import com.businessdevelop.POCEvento.model.EventoDeportivo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class PocEventoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocEventoApplication.class, args);

	}

    // Cargar datos de prueba
    @Bean
    public CommandLineRunner cargarDatos(ServicioEvento servicioEvento) {
        return args -> {
            EventoDeportivo e1 = new EventoDeportivo(
                    "1", "F Bogotá", "Bogota", 2000,
                    LocalDate.of(2025, 10, 10), 50000, "Futbol"
            );

            EventoDeportivo e2 = new EventoDeportivo(
                    "2", "Balo Medellín", "Medellin", 1500,
                    LocalDate.of(2025, 11, 5), 40000, "Baloncesto"
            );

            EventoDeportivo e3 = new EventoDeportivo(
                    "3", "Cicli Cali", "Cali", 1000,
                    LocalDate.of(2025, 12, 1), 30000, "Ciclismo"
            );

            EventoDeportivo e4 = new EventoDeportivo(
                    "4", "F Medellín", "Medellin", 2500,
                    LocalDate.of(2025, 9, 20), 60000, "Futbol"
            );

            EventoDeportivo e5 = new EventoDeportivo(
                    "5", "F Tolima", "Medellin", 50000,
                    LocalDate.of(2025, 10, 20), 60000, "Futbol"
            );

            servicioEvento.crearEvento(e1);
            servicioEvento.crearEvento(e2);
            servicioEvento.crearEvento(e3);
            servicioEvento.crearEvento(e4);
            servicioEvento.crearEvento(e5);

            System.out.println("✅ Datos de prueba cargados exitosamente!");
        };
    }

}


