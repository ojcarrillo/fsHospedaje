package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GenerarDatosUtils {

	private final static List<String> ciudades = Arrays
			.asList("Puerto Inírida,San José del Guaviare,Neiva,Riohacha,Santa Marta,Villavicencio,Pasto,Cúcuta,Mocoa,Armenia,Pereira,San Andres,Bucaramanga,Sincelejo,Ibagué,Cali,Mitú,Puerto Carreño,Leticia,Medellín,Arauca,Barranquilla,Cartagena,Tunja,Manizales,Florencia,Yopal,Popayán,Valledupar,Quibdó,Montería,Bogotá"
					.split(","));
	private final static List<String> hoteles = Arrays
			.asList("Hotel Candilejas,Hotel Los Girasoles,Hotel Boyaca La 24,Hotel Lagos del Norte,Hotel Inn 72,Hotel Bogota Norte,Hotel Lord,Suites Ejecutivas Economicas,Suites Inn Turisticas Calle 63,Suites Inn Economicas Calle 63,Hotel 63 Inn Ejecutivo,Hotel Elegant Suite Normandia,Hotel del Puente Bogota,Hotel San Jose De Bavaria,Casa Kiwi Resort,Hospedaje Las Palmeras de La 97,Hotel Bogota Royal,Hotel La Mansion,Radisson Royal Bogota Hotel"
					.split(","));
	private final static String FORMATO_FECHA_HORA = "ddMMyyyyHH00";
	private final static String FORMATO_FECHA_ENTRADA = "ddMMyyyy";
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA_HORA);
	private final static String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private final static Integer TOTAL_PISOS = 9;
	private final static Integer TOTAL_HABITACIONES = 20;
	private final static Integer TOTAL_COMPRAS = 5;
	private final static Double IVA = 0.71D; // 29%
	private final static Integer FACTOR_PRECIO = 30;
	private final static Double FACTOR_PRECIO_DINERO = 7000D;

	public String getFechaDisponible(Date beginTime, Date endTime) {
		return dateFormat.format(new Date(getRandomTimeBetweenTwoDates(beginTime, endTime)));
	}

	public String getCiudad() {
		return StringUtils.rightPad(ciudades.get(new Random().nextInt(ciudades.size()) + 1), 21, " ");
	}
	
	public String getHotel() {
		return StringUtils.rightPad(hoteles.get(new Random().nextInt(hoteles.size()) + 1), 31, " ");
	}

	public String getHabitacionDisponible() {
		return (new Random().nextInt(TOTAL_PISOS) + 1)
				+ String.format("%02d", new Random().nextInt(TOTAL_HABITACIONES) + 1);
	}

	public Integer getHabitacionesComprados() {
		return new Random().nextInt(TOTAL_COMPRAS) + 1;
	}

	public Date getFechaEntrada(String fecha) {
		try {
			return new SimpleDateFormat(FORMATO_FECHA_ENTRADA).parse(fecha);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getIdHabitacion() {
		return generateRandomStr(LETRAS, 3) + String.format("%06d", new Random().nextInt(100000) + 1);
	}

	public String getIdCancelacion() {
		return String.format("%07d", new Random().nextInt(1000000) + 1);
	}

	public String getFiltros(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String datos = br.readLine();
		br.close();
		return datos;
	}

	public List<String> getFiltrosVarios(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String opc = null;
		List<String> datos = new ArrayList<String>();
		while ((opc = br.readLine()) != null) {
			datos.add(opc);
		}
		br.close();
		return datos;
	}

	private String generateRandomStr(String aToZ, Integer size) {
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < size; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		return res.toString();
	}

	private Long getRandomTimeBetweenTwoDates(Date beginTime, Date endTime) {
		long diff = endTime.getTime() - beginTime.getTime() + 1;
		return beginTime.getTime() + (long) (Math.random() * diff);
	}

	public Double getValorPto(Integer cantPtos) {
		Double valorPto = (new Random().nextInt(FACTOR_PRECIO) + 1D) * FACTOR_PRECIO_DINERO;
		return Math.floor(cantPtos * valorPto);
	}

	public Double getValorIVA(Double valorTotal) {
		return valorTotal - (valorTotal * IVA);
	}

	public String getValoresMonetarios() {
		Integer cantPuestos = getHabitacionesComprados();
		Double valorPuestos = getValorPto(cantPuestos);
		Double valorIVA = getValorIVA(valorPuestos);
		return StringUtils.leftPad(String.valueOf(cantPuestos), 2, "0")
				+ StringUtils.leftPad(String.valueOf(valorPuestos), 17, "0")
				+ StringUtils.leftPad(String.valueOf(valorIVA), 17, "0");
	}

	public String getValoresMonetarios(Integer cantPuestos) {
		Double valorPuestos = getValorPto(cantPuestos);
		Double valorIVA = getValorIVA(valorPuestos);
		return StringUtils.leftPad(String.valueOf(cantPuestos), 2, "0")
				+ StringUtils.leftPad(String.valueOf(valorPuestos), 17, "0")
				+ StringUtils.leftPad(String.valueOf(valorIVA), 17, "0");
	}

	public Date getFutureDay(Date date, Integer days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days); // number of days to add
		return c.getTime();
	}

	public Date parseDate(String fecha, String formato) throws ParseException {
		SimpleDateFormat formatoDate = new SimpleDateFormat(formato);
		return formatoDate.parse(fecha);
	}
	
	public String getIdReserva() {
		return generateRandomStr(LETRAS, 1) + String.format("%08d", new Random().nextInt(100000) + 1);
	}
}
