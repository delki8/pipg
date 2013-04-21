package org.pipg.net;

import java.io.IOException;

import org.pipg.R;
import org.pipg.gui.PublicacoesGUI;
import org.pipg.gui.PublicacoesHandler;
import org.pipg.utils.Util;

import android.os.Message;

/**
 * {@link Thread} Para ser chamada no onClick de cada item do adapter da lista
 * de qualquer tipo de conteúdo (eu espero, ainda não testei com áudio/vídeo).
 * 
 * @author delki8
 * @data 03/11/2012
 * */
public class AbrirArquivoThread extends Thread {

	private PublicacoesGUI activityPai;
	private String caminhoExterno;

	public AbrirArquivoThread(PublicacoesGUI activityPai, String caminhoExterno) {
		super();
		this.activityPai = activityPai;
		this.caminhoExterno = caminhoExterno;
	}

	@Override
	public void run() {
		Message msg = null;
		try {
			int sizeB = Util.tamanhoArquivo(caminhoExterno);
			if (sizeB > 0) {
				int sizeKb = sizeB / 1024;
				msg = Message.obtain(PublicacoesGUI.activityHandler,
						PublicacoesHandler.MESSAGE_CONFIRM_DOWNLOAD, sizeKb, 0,
						caminhoExterno);
			} else {
				throw new IOException();
			}
		} catch (IOException e) {
			String errMsg = activityPai
					.getString(R.string.error_connection);
			msg = Message.obtain(PublicacoesGUI.activityHandler,
					PublicacoesHandler.MESSAGE_ENCOUNTERED_ERROR, 0, 0, errMsg);
		} catch (Exception e) {
			String errMsg = activityPai
					.getString(R.string.error_message_general);
			msg = Message.obtain(PublicacoesGUI.activityHandler,
					PublicacoesHandler.MESSAGE_ENCOUNTERED_ERROR, 0, 0, errMsg);
		} finally {
			PublicacoesGUI.activityHandler.sendMessage(msg);
		}
	}
}
