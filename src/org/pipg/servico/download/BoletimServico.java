package org.pipg.servico.download;

import java.util.ArrayList;

import org.apache.http.impl.conn.tsccm.WaitingThread;
import org.pipg.bean.Boletim;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BoletimServico extends Service implements PublicacaoInterface<Boletim>{
	private final String ERRO = "PIPG-Erro";
	private static final String URL_ATUALIZACAO_PARCIAL = "http://pipg.org/index.cfm?p=bulletins";
	
	ArrayList<Boletim> boletins = new ArrayList<Boletim>();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		baixaPublicacao(false);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public ArrayList<Boletim> baixaPublicacao(Boolean atualizacaoCompleta){
		Thread t = null;
		try {
			if (atualizacaoCompleta){
				t = new Thread("baixaBoletinsCompleta"){
					@Override
					public void run(){
						Boolean euDevoContinuarBuscando = true;
						Integer noPagina = 1;
						while (euDevoContinuarBuscando){
							ArrayList<Boletim> boletinsTemp = TrataConteudo
									.pegarListaBoletim(URL_ATUALIZACAO_PARCIAL + "&d=" + noPagina);
							if (boletinsTemp != null && boletinsTemp.size() > 0){
								boletins.addAll(boletinsTemp);  
							}else{
								euDevoContinuarBuscando = false;
							}
							noPagina++;
						}
						for (Boletim boletim : boletins) {
							Log.i("boletim", boletim.getPastoral() + " " + boletim.getDataPublicacao());
						}
						stopSelf();
					}
				};
			}else{
				t = new Thread("baixaBoletinsParcial"){
					@Override
					public void run(){
						boletins = TrataConteudo.pegarListaBoletim(URL_ATUALIZACAO_PARCIAL);
						for (Boletim boletim : boletins) {
							Log.i("boletim", boletim.getPastoral() + " " + boletim.getDataPublicacao());
						}
						stopSelf();
					}
				};
			}
		
			t.start();
			t.join();
		} catch (InterruptedException e) {
			Log.i(ERRO, "Erro na thread: " + e.getMessage());
		}
		return boletins;
	}
}