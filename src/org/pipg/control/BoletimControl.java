package org.pipg.control;

import java.util.ArrayList;

import org.pipg.beans.Boletim;
import org.pipg.net.BoletimRepositorio;
import org.pipg.net.TrataConteudo;

import android.content.Context;

public class BoletimControl {
	
	/**Atualiza a lista de boletins do sistema.
	 * @param atualizacaoCompleta informa se a atualizacao que será realizada
	 * deverá ser completa ou parcial. A atualização parcial recupera apenas
	 * os 10 últimos boletins.
	 * @param contexto da activity que está atualizando os boletins.
	 * */
	public void atualizaBoletins(Boolean atualizacaoCompleta, Context context) {
		BoletimRepositorio bRep = new BoletimRepositorio(context);
		String url = "http://pipg.org/index.cfm?p=bulletins";
		ArrayList<Boletim> bols = TrataConteudo.pegarListaBoletim(url);
		bRep.inserir(bols);
	}
}
