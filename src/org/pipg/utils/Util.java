package org.pipg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class Util {
	
	private static String CATEGORIA = "pipg";
	
	/** Recebe uma string no formato "EEE MMM dd HH:mm:ss zzz yyyy" e retorna 
	 * uma java.util.Date.
	 * @param dataString é uma string no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * @return data convertida.
	 * */
	public static Date dataStringDate(String dataString) {
	    try {
	    	SimpleDateFormat sdf = 
	    			new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	    	Date data = sdf.parse(dataString);
	    	return data;
		} catch (ParseException e) {
			Log.e(CATEGORIA, "Erro ao converter. " + e.getMessage());
		}  
	    return null;
	}
	
	/** Pega qualquer java.util.Date e formata para o formato desejado.
	 * @param data com a data a ser formatada.
	 * @param formato com o formato desejado Ex: "dd/MM/yyyy"
	 * @return dataFormatada de acordo com o formato informado. 
	 * */
	public static String dataDateString(Date data, String formato) {
		String dataFormatada = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		dataFormatada = sdf.format(data);
		return dataFormatada;
	}
	
	/** Recebe uma String de data no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * e formata ela para "dd/MM/yyyy"
	 * @param dataString no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * @return dataFormatada no formato "dd/MM/yyyy"
	 * */
	public static String dataStringString(String dataString) {
		Date data = dataStringDate(dataString);
		String dataFormatada = dataDateString(data, "dd/MM/yyyy");
		return dataFormatada;
	}
	
	/** Pega uma String no formato informado e transforma num java.util.Date
	 * @param data no formato informado.
	 * @param formato em que a data está chegando.
	 * @return Data referente ao formato personalizado ou null caso não seja
	 * possível formatar.
	 * */
	public static Date formatDateCustom(String dataString, String formato) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			Date dataRetorno = sdf.parse(dataString);
			return dataRetorno;
		} catch (ParseException e) {
			Log.e(CATEGORIA, "A data: " + dataString + " não está no formato: " 
					+ formato);
		}
		return null;
	}
	
	/** Encontra o domingo mais próximo de uma determinada data.
	 * @param dataDePublicacao é a data usada como parâmetro, a data retornada
	 * é o domingo mais próximo à data informada aqui.
	 * @return java.util.Date correspondente ao domingo mais próximo da data 
	 * informada no parâmetro.
	 * */
	public static Date domingoMaisProximo(Date dataDePublicacao) {
		Date dataDomingo = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataDePublicacao);
		int incremento = 1;
		if (cal.get(Calendar.DAY_OF_WEEK) < Calendar.THURSDAY){
			incremento = -1;
		}
		
		while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
			cal.add(Calendar.DATE, incremento);
		}
		
		dataDomingo = new Date(cal.getTimeInMillis());
		return dataDomingo; 
	}
}
