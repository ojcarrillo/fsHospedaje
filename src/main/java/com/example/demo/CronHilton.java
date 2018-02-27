package com.example.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronHilton {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/* ruta del directorio compartido */
	private static final String PATH = File.separator + "ftp" + File.separator + "touresbalon" + File.separator;
	private static final String PREFIJO = "rta_";

	/* formato para la fecha hora de ejecucion */
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@Scheduled(fixedDelay = 5000, initialDelay = 5000)
	public void comprobarDirectorio() {
		log.info("aca esta! " + dateTimeFormatter.format(LocalDateTime.now()));
		log.info(PATH);
		leerDirectorio();
	}

	public void leerDirectorio() {
		File folder = new File(PATH);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					log.info(file.getName());
					routingAccion(file.getName());
				}
			}
		} else {
			log.error("no existe >>> " + PATH);
		}
	}

	public void routingAccion(String fileName) {
		if (fileName.startsWith("query_reservas")) {
			crearArchivoRta(fileName);
		} else if (fileName.startsWith("query_servicios")) {

		} else if (fileName.startsWith("query_compras")) {

		} else if (fileName.startsWith("reservar")) {

		} else if (fileName.startsWith("comprar")) {

		} else if (fileName.startsWith("cancelar")) {

		} else if (fileName.startsWith("cambiar")) {

		}
	}

	public void crearArchivoRta(String fileName) {
		fileName = PATH + PREFIJO + fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/* agrega el nuevo mensaje al archivo */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND)) {
			writer.write("respuesta " + dateTimeFormatter.format(LocalDateTime.now()));
			writer.newLine();
			writer.close();
		} catch (IOException ioe) {
			log.error("IOException: %s%n", ioe);
		}
	}
}
