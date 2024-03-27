package com.backend.clinicaDental;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


//anotacion que indica que es la ventana principal para correr la app.
@SpringBootApplication
public class ClinicaDentalApplication {

	private static Logger LOGGER = LoggerFactory.getLogger(ClinicaDentalApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(ClinicaDentalApplication.class, args);

		//crearTabla(); | *** se quita porque no se va a usar, lo dejo como ejemplo grabado en el codigo. ***

		LOGGER.info("Application is now running...");
		//LOGGER.error("este es un mensaje de error");
		//LOGGER.warn("este es un mensaje de warn");

	}

	@Bean //se disponibiliza el Bean. Necesito que springBoot me lo maneje por eso lo declaro.
	//metodo para disponibilizar el model mapper
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	//*** se quita porque no se va a usar, lo dejo como ejemplo grabado en el codigo. ***
	/*
	public static void crearTabla(){
		Connection connection = null;
		try{
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection("jdbc:h2:~/cliDental", "gera", "gerardo");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				connection.close();
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}

	}
	*/


}
