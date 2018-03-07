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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronHilton {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GeneraRespuestas genRtas;
	
	/* ruta del directorio compartido */
	private static final String PATH = File.separator + "ftp" + File.separator + "touresbalon" + File.separator;
	private static final String EXT = ".txt";

	@Scheduled(fixedDelay = 5000, initialDelay = 5000)
	public void leerDirectorio() {
		File folder = new File(PATH);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				if (file.isFile() && file.getName().toLowerCase().endsWith(EXT)) {
					System.out.println(file.getName());
					genRtas.crearArchivoRta(file.getName());
				}
			}
		} else {			
			log.error("===>>> directorio no existe >>> " + PATH);
		}
	}

}
