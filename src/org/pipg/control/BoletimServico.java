package org.pipg.control;

import java.util.ArrayList;

import org.pipg.beans.Boletim;
import org.pipg.net.BoletimRepositorio;
import org.pipg.net.TrataConteudo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BoletimServico extends Service implements PublicacaoInterface<Boletim>{
	private static final String URL_ATUALIZACAO_PARCIAL = 
			"http://pipg.org/index.cfm?p=bulletins";
	
	ArrayList<Boletim> boletins = new ArrayList<Boletim>();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void baixaPublicacao(Boolean completa, Context ctx){
		final BoletimRepositorio bRepositorio = new BoletimRepositorio(ctx);
			if (completa){
				new Thread("baixaBoletinsCompleta"){
					@Override
					public void run(){
						Boolean euDevoContinuarBuscando = true;
						Integer nPagina = 1;
						while (euDevoContinuarBuscando) {
							ArrayList<Boletim> boletinsTemp = TrataConteudo
									.pegarListaBoletim(URL_ATUALIZACAO_PARCIAL + "&d=" + nPagina);
							if (boletinsTemp != null && boletinsTemp.size() > 0){
								boletins.addAll(boletinsTemp);  
							}else{
								euDevoContinuarBuscando = false;
							}
							nPagina++;
						}
						
						stopSelf();
					}
				}.start();
			} else {
				new Thread("baixaBoletinsParcial"){
					@Override
					public void run(){
						boletins = TrataConteudo.pegarListaBoletim(URL_ATUALIZACAO_PARCIAL);
						for (Boletim boletim : boletins) {
							Log.i("boletim", boletim.getPastoral() + " " + boletim.getDataPublicacao() + " " + boletim.getData());
						}
						stopSelf();
					}
				}.start();
			}
		
			bRepositorio.inserir(boletins);
	}

	@Override
	public PublicacaoInterface<Boletim> getInstance() {
		// TODO Auto-generated method stub
		return null;
	}
}