package org.pipg.control;

import java.util.ArrayList;

import org.pipg.beans.Boletim;
import org.pipg.gui.PublicacoesGUI;
import org.pipg.net.BoletimRepositorio;
import org.pipg.net.TrataConteudo;

import android.os.Message;

public class BoletimControl {
	
	BoletimRepositorio bRep;

	/**Atualiza a lista de boletins do sistema.
	 * @param atualizacaoCompleta informa se a atualizacao que será realizada
	 * deverá ser completa ou parcial. A atualização parcial recupera apenas
	 * os 10 últimos boletins.
	 * @param contexto da activity que está atualizando os boletins.
	 * */
	public void atualizaBoletins(Boolean atualizacaoCompleta, 
			PublicacoesGUI parentActivity) {
		bRep = new BoletimRepositorio(parentActivity);
		String url = "http://pipg.org/index.cfm?p=bulletins";
		ArrayList<Boletim> bols = TrataConteudo.pegarListaBoletim(url);
		bRep.inserir(bols);
		
		/* Sinalizar o término do método para a activity*/
		Message msg = Message.obtain(parentActivity.activityHandler, 
				PublicacoesGUI.MESSAGE_TERMINOU_UPDATE, 0, 0, bols);
		parentActivity.activityHandler.sendMessage(msg);
	}
	
	public void limparBanco(PublicacoesGUI parentActivity) {
		bRep = new BoletimRepositorio(parentActivity);
		bRep.limparBanco();
		Message msg = Message.obtain(parentActivity.activityHandler, 
				PublicacoesGUI.MESSAGE_TERMINOU_LIMPAR);
		parentActivity.activityHandler.sendMessage(msg);
	}
}
