package org.pipg.control;

import java.util.ArrayList;

import org.pipg.beans.Boletim;
import org.pipg.net.BoletimRepositorio;

import android.content.Context;
import android.widget.Toast;

public class BoletimControl {
	
	/**Atualiza a lista de boletins do sistema.
	 * @param atualizacaoCompleta informa se a atualizacao que será realizada
	 * deverá ser completa ou parcial. A atualização parcial recupera apenas
	 * os 10 últimos boletins.
	 * @param contexto da activity que está atualizando os boletins.
	 * */
	public int atualizaBoletins(Boolean atualizacaoCompleta, Context context) {
		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
		BoletimServico bServico = new BoletimServico();
    	boletins = bServico.baixaPublicacao(atualizacaoCompleta);
    	BoletimRepositorio bRepositorio = new BoletimRepositorio(context);
    	int inseridos = bRepositorio.inserir(boletins);
    	Toast.makeText(context, inseridos + 
				" registros inseridos", Toast.LENGTH_SHORT).show();
    	bRepositorio.fechar();
    	return inseridos;
	}
}
