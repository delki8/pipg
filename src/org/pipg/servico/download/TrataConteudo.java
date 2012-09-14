package org.pipg.servico.download;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
					boletim.setDataDoBoletim(encontraProximoDomingo(boletim.getDataPublicacao()));
				}
				
				if (boletim.getPastoral() != null 
						&& boletim.getLink() != null 
						&& boletim.getDataPublicacao() != null){
					boletins.add(boletim);
				}
			}
		} catch (IOException e) {
			Log.e("ERRO", "Erro de conexão:: " + e.getMessage());
		} catch (ParseException e) {
			Log.e("ERRO", "Erro de conversão de data: " + e.getMessage());
		}
		return boletins;
	}
	
	protected static Date encontraProximoDomingo(Date dataDePublicacao){
		Date dataDomingo = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataDePublicacao);
		
		while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
			dataDomingo = new Date(cal.getTimeInMillis());
			cal.add(Calendar.DATE, 1);
		}
		
		Long tempoAnterior = dataDomingo.getTime();
		Long tempoAtual = cal.getTimeInMillis();
		
		dataDomingo = new Date(cal.getTimeInMillis());
		
		Log.i("DEBUG", ((Long)(tempoAtual - tempoAnterior)).toString());
		return dataDomingo;
	}
}