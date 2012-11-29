package org.pipg.control;

import java.io.File;
import java.util.ArrayList;

import org.pipg.beans.Boletim;
import org.pipg.gui.PublicacoesGUI;
import org.pipg.net.BoletimRepositorio;
import org.pipg.net.TrataConteudo;
import org.pipg.utils.Util;

import android.os.Message;

public class BoletimControl {
	
	BoletimRepositorio bRep;

	/** Atualiza a lista de boletins do sistema.
	 * @param atualizacaoCompleta informa se a atualizacao que será realizada
	 * deverá ser completa ou parcial. A atualização parcial recupera apenas
	 * os 10 últimos boletins.
	 * @param parentActivity a activity que está atualizando os boletins.
	 * */
	public void atualizaBoletins(Boolean atualizacaoCompleta, 
			PublicacoesGUI parentActivity) {
		bRep = new BoletimRepositorio(parentActivity);
		ArrayList<Boletim> boletins = new ArrayList<Boletim>(); 
		
		if (!atualizacaoCompleta) {
			String url = "http://pipg.org/index.cfm?p=bulletins";
			boletins = TrataConteudo.pegarListaBoletim(url);
		} else {
			Boolean euDevoContinuarBuscando = true;
			Integer nPagina = 1;
			while (euDevoContinuarBuscando) {
				String url = Util.URL_ATUALIZACAO_PARCIAL + "&d=" + nPagina;
				ArrayList<Boletim> boletinsTemp = TrataConteudo
						.pegarListaBoletim(url);
				if (boletinsTemp != null && boletinsTemp.size() > 0) {
					boletins.addAll(boletinsTemp);  
				} else {
					euDevoContinuarBuscando = false;
				}
				nPagina++;
			}
		}
		bRep.inserir(boletins);
		
		/* Sinalizar o término do método para a activity*/
		Message msg = Message.obtain(parentActivity.activityHandler, 
				PublicacoesGUI.MESSAGE_TERMINOU_UPDATE, 0, 0, boletins);
		parentActivity.activityHandler.sendMessage(msg);
	}
	
	public void limparBoletins(PublicacoesGUI parentActivity) {
		bRep = new BoletimRepositorio(parentActivity);
		int registrosApagados = bRep.limparBanco();
		Message msg = Message.obtain(parentActivity.activityHandler, 
				PublicacoesGUI.MESSAGE_TERMINOU_LIMPAR, registrosApagados, 0);
		File dirPadrao = new File(Util.ENDERECO_LOCAL);
		if (dirPadrao.isDirectory()) {
			Util.apagaDiretorio(dirPadrao);
		}
		parentActivity.activityHandler.sendMessage(msg);
	}
}
