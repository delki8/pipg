package org.pipg.servico.download;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pipg.bean.Boletim;

import android.util.Log;

/**
 * @author delki8
 * */
public final class TrataConteudo {
	
	private static Date ultimaPublicacao;
	
	protected static ArrayList<Boletim> pegarListaBoletim(String url){
		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
		Boletim boletim = null;
		try {
			Document doc = Jsoup.connect(url).get();
			Elements elements = doc.select(".grid_10 li");
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
			for (Element li : elements) {
				boletim = new Boletim();
				
				Elements h5 = li.getElementsByTag("h5");
				for (Element element : h5) {
					Elements a = element.getElementsByTag("a");
					boletim.setPastoral(a.get(0).text());
					boletim.setLink(new URL(a.get(0).attr("href")));
				}
				
				Elements p = li.getElementsByTag("p");
				if (p.text() != null && !p.text().trim().equals("")){
					boletim.setDataPublicacao(sdf.parse(p.text().trim()));
					boletim.setDataBoletim(encontraProximoDomingo(boletim.getDataPublicacao()));
					boletim.setNumeroDoBoletim(encontraNumeroBoletim(boletim.getDataBoletim()));
				}
				
				if (boletim.getPastoral() != null 
						&& boletim.getLink() != null 
						&& boletim.getDataPublicacao() != null){
					boletins.add(boletim);
				}
			}
		} catch (IOException e) {
			Log.e("ERRO", "Erro de conexão: " + e.getMessage());
		} catch (ParseException e) {
			Log.e("ERRO", "Erro de conversão de data: " + e.getMessage());
		}
		return boletins;
	}
	
	protected static Integer encontraNumeroBoletim(Date dataBoletim) {
		Calendar diaInicial = new GregorianCalendar();
		diaInicial.set(2012, 4, 13); 

		Calendar diaFinal = new GregorianCalendar();
		diaFinal.setTime(dataBoletim);
		
		Log.i("DEBUG", "Days= " + dataBoletim.toString());
		Integer numero = (daysBetween(diaInicial.getTime(),diaFinal.getTime()) / 7) + 20; 
		return (numero);
	}
	
	private static int daysBetween(Date d1, Date d2){
		 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	protected static Date encontraProximoDomingo(Date dataDePublicacao){
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
		
		if (dataDomingo.equals(ultimaPublicacao)){
			cal.add(Calendar.DATE, 7);
			dataDomingo = new Date(cal.getTimeInMillis());
		}
		
		ultimaPublicacao = dataDomingo;
		return dataDomingo;
	}
}