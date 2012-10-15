package org.pipg.net;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pipg.beans.Boletim;
import org.pipg.utils.Util;

import android.util.Log;

/**
 * @author delki8
 * */
public final class TrataConteudo {
	
	private static Date ultimaPublicacao;
	
	public static ArrayList<Boletim> pegarListaBoletim(String url){
		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
		Boletim boletim = null;
		try {
			Document doc = Jsoup.connect(url).get();
			Elements elements = doc.select(".grid_10 li");
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
					String data = p.text().trim();
					boletim.setDataPublicacao(Util.formatDateCustom(data, 
							"dd MMM, yyyy"));
					boletim.setData(domingoMaisProximo(boletim
							.getDataPublicacao()));
//					boletim.setNumero(encontraNumeroBoletim(boletim.getData()));
				}
				
				if (boletim.getPastoral() != null 
						&& boletim.getLink() != null 
						&& boletim.getDataPublicacao() != null){
					boletins.add(boletim);
				}
			}
		} catch (IOException e) {
			Log.e("ERRO", "Erro de conexão: " + e.getMessage());
		}
		return boletins;
	}
	
	public static Integer encontraNumeroBoletim(Date dataBoletim) {
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

	public static Date domingoMaisProximo(Date dataDePublicacao) {
		Date dataDomingo = Util.domingoMaisProximo(dataDePublicacao);
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dataDomingo.getTime());
		
		if (dataDomingo.equals(ultimaPublicacao)){
			cal.add(Calendar.DATE, 7);
			dataDomingo = new Date(cal.getTimeInMillis());
		}
		
		ultimaPublicacao = dataDomingo;
		return dataDomingo;
	}
}