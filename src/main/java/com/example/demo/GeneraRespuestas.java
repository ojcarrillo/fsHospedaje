package com.example.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneraRespuestas {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GenerarDatosUtils gendata;
	
	/* ruta del directorio compartido */
	private static final String PATH = File.separator + "ftp" + File.separator + "touresbalon" + File.separator;
	private static final String PREFIJO = "rta_";
	private static final String END_LINE = "\r\n";

	/* formato para la fecha hora de ejecucion */
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public void crearArchivoRta(String fileNameInput) {
		File fileInput = new File(PATH + fileNameInput);
		String fileNameOutput = PATH + PREFIJO + fileNameInput;
		File fileOutput = new File(fileNameOutput);
		if (!fileOutput.exists()) {
			try {
				fileOutput.createNewFile();
			} catch (IOException e) {
				log.error("IOException: %s%n", e);
			}
		}	
		/* respuesta */
		List<String> rtas = getRespuestas(fileInput);
		/* agrega el nuevo mensaje al archivo */
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileNameOutput), StandardOpenOption.APPEND)) {
			for(String x : rtas) {
				writer.write(x);
				writer.newLine();
			}
			writer.close();
		} catch (IOException ioe) {
			log.error("IOException: %s%n", ioe);
		} 
		/* elimina archivo de entrada */
		try {
			Files.deleteIfExists(fileInput.toPath());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public List<String> getRespuestas(File fileInput) {
		try {			
			if (fileInput.getName().startsWith("query_reservas_")) {
				return getDatosRtaReservasServicios(fileInput);
			} else if (fileInput.getName().startsWith("query_habitaciones_")) {
				return getDatosRtaReservasServicios(fileInput);
			} else if (fileInput.getName().startsWith("query_compras_")) {
				return getDatosRtaCompras(fileInput);
			} else if (fileInput.getName().startsWith("reservar_")) {
				return getDatosRtaReservar(fileInput);
			} else if (fileInput.getName().startsWith("comprar_")) {
				return getDatosRtaComprar(fileInput);
			} else if (fileInput.getName().startsWith("anular_")) {
				return getDatosRtaAnular(fileInput);
			} 	
		}catch(Exception ex) {
			log.error(ex.getMessage());
		}
		return null;
	}
	
	public List<String> getDatosRtaReservasServicios(File file) throws IOException {
		List<String> rtas = new ArrayList<String>();
		String opc = gendata.getFiltros(file);
		if(opc!=null) {
			String fechaInicial = opc.substring(0, 8);
			String fechaFinal = opc.substring(8, 16).trim();
			String ciudadOrigen = opc.substring(16, 37).trim();			
			for(int i=1;i<new Random().nextInt(10)+1;i++) {
				StringBuilder salida = new StringBuilder();
				salida.append(gendata.getIdHabitacion());
				salida.append(gendata.getFechaDisponible(gendata.getFechaEntrada(fechaInicial), gendata.getFechaEntrada(fechaFinal)));
				salida.append(ciudadOrigen.length()>0 ? ciudadOrigen : gendata.getCiudad());
				salida.append(gendata.getHabitacionDisponible());
				rtas.add(salida.toString());
			}
		}
		return rtas;
	}
	
	public List<String> getDatosRtaCompras(File file) throws IOException{
		List<String> rtas = new ArrayList<String>();
		String opc = gendata.getFiltros(file);
		if(opc!=null) {
			String fechaInicial = opc.substring(0, 8);
			String fechaFinal = opc.substring(8, 16).trim();
			String ciudadOrigen = opc.substring(16, 37).trim();
			for(int i=1;i<new Random().nextInt(10)+1;i++) {
				StringBuilder salida = new StringBuilder();
				salida.append(gendata.getIdHabitacion());
				salida.append(gendata.getFechaDisponible(gendata.getFechaEntrada(fechaInicial), gendata.getFechaEntrada(fechaFinal)));
				salida.append(ciudadOrigen.length()>0 ? ciudadOrigen : gendata.getCiudad());
				/* valores monetarios */
				salida.append(gendata.getValoresMonetarios());				
				rtas.add(salida.toString());
			}
		}
		return rtas;
	}
	
	public List<String> getDatosRtaReservar(File file) throws IOException {
		List<String> rtas = new ArrayList<String>();
		for(String opc : gendata.getFiltrosVarios(file)) {
			String fechaInicio = opc.substring(0, 12).trim();
			String fechaFinal = opc.substring(13, 24).trim();
			String ciudadOrigen = opc.substring(25, 46).trim();
			String habitacion = opc.substring(47, 49).trim();			
			StringBuilder salida = new StringBuilder();
			salida.append(gendata.getIdHabitacion());
			salida.append(ciudadOrigen);
			salida.append(fechaInicio);
			salida.append(fechaFinal);
			salida.append(habitacion);
			rtas.add(salida.toString());
		}
		return rtas;
	}
	
	public List<String> getDatosRtaComprar(File file) throws IOException {
		List<String> rtas = new ArrayList<String>();
		for(String opc : gendata.getFiltrosVarios(file)) {
			String fechaInicio = opc.substring(0, 12).trim();
			String fechaFinal = opc.substring(13, 24).trim();
			String ciudadOrigen = opc.substring(25, 46).trim();
			String habitacion = opc.substring(47, 49).trim();
			StringBuilder salida = new StringBuilder();
			salida.append(gendata.getIdHabitacion());
			salida.append(ciudadOrigen);
			salida.append(fechaInicio);
			salida.append(fechaFinal);
			salida.append(habitacion);
			/* valores monetarios */
			salida.append(gendata.getValoresMonetarios(1));	
			rtas.add(salida.toString());
		}
		return rtas;
	}
	
	public List<String> getDatosRtaAnular(File file) throws IOException {
		List<String> rtas = new ArrayList<String>();
		for(String opc : gendata.getFiltrosVarios(file)) {
			String idViaje = opc.substring(0, 9).trim();
			StringBuilder salida = new StringBuilder();
			salida.append(idViaje);
			salida.append(gendata.getIdCancelacion());			
			rtas.add(salida.toString());
		}
		return rtas;
	}
	
}

