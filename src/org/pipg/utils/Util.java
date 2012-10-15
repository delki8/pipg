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
	 * @return java.util.Date convertida.
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
	
	/** Recebe uma String de data no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * e formata ela para "dd/MM/yyyy"
	 * @param dataString no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * @return dataFormatada no formato "dd/MM/yyyy"
	 * */
	public static String dataStringString(String dataString) {
		Date data = dataStringDate(dataString);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = sdf.format(data);
		return dataFormatada;
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
