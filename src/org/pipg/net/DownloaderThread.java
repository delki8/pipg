package org.pipg.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Connection;
import org.pipg.R;
import org.pipg.gui.PublicacoesGUI;
import org.pipg.utils.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Message;
import android.widget.Toast;

public class DownloaderThread extends Thread {

	// constantes
	private static final int DOWNLOAD_BUFFER_SIZE = 4096;
	private static final File DIR_EXTERNO = new File(Util.ENDERECO_LOCAL);

	private PublicacoesGUI parentActivity;
	private String downloadUrl;

	/**
	 * Instantiates a new DownloadThread object.
	 * 
	 * @param activityPai
	 *            Reference to AndroidFileDownloader activity.
	 * @param enderecoArquivoSite
	 *            String representing the URL of the file to be downloaded.
	 */
	public DownloaderThread(PublicacoesGUI activityPai,
			String enderecoArquivoSite) {
		downloadUrl = "";
		if (enderecoArquivoSite != null) {
			downloadUrl = enderecoArquivoSite;
		}
		parentActivity = activityPai;
	}

	/*
	 * Connects to the URL of the file, begins the download, and notifies
	 * AndroidFileDownloader activity of changes in state. Writes the file of
	 * the root of the SD card.
	 */
	@Override
	public void run() {
		URL url;
		URLConnection conn;
		int tamanhoArquivo;
		String nomeArquivo;
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		File arquivoSaida;
		FileOutputStream fileStream;
		Message msg;

		try {
			url = new URL(downloadUrl);
			conn = url.openConnection();
			conn.setUseCaches(false);
			tamanhoArquivo = Util.tamanhoArquivo(downloadUrl);

			// Pegando o nome do arquivo a partir da url
			nomeArquivo = Util.nomeArquivoDaUrl(downloadUrl);

			// Verificar se o arquivo já existe
			File arquivoLocal = new File(Util.ENDERECO_LOCAL + nomeArquivo);
			if (!arquivoLocal.isFile()) {
				// Iniciando a conexão aqui
				msg = Message.obtain(parentActivity.activityHandler,
						PublicacoesGUI.MESSAGE_CONNECTING_STARTED, 0, 0,
						downloadUrl);
				parentActivity.activityHandler.sendMessage(msg);

				// Notificar à activity que o download iniciou.
				int tamanhoArquivoEmKB = tamanhoArquivo / 1024;
				msg = Message.obtain(parentActivity.activityHandler,
						PublicacoesGUI.MESSAGE_DOWNLOAD_STARTED,
						tamanhoArquivoEmKB, 0, nomeArquivo);
				parentActivity.activityHandler.sendMessage(msg);

				// Iniciar o download
				inStream = new BufferedInputStream(conn.getInputStream());
				DIR_EXTERNO.mkdirs();
				arquivoSaida = new File(DIR_EXTERNO, nomeArquivo);
				fileStream = new FileOutputStream(arquivoSaida);
				outStream = new BufferedOutputStream(fileStream,
						DOWNLOAD_BUFFER_SIZE);
				byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
				int bytesLidos = 0;
				int totalRead = 0;
				while (!isInterrupted()
						&& (bytesLidos = inStream.read(data, 0, data.length)) >= 0) {
					outStream.write(data, 0, bytesLidos);

					// Atualizar a barra de progresso
					totalRead += bytesLidos;
					int totalReadInKB = totalRead / 1024;
					msg = Message.obtain(parentActivity.activityHandler,
							PublicacoesGUI.MESSAGE_UPDATE_PROGRESS_BAR,
							totalReadInKB, 0);
					parentActivity.activityHandler.sendMessage(msg);
				}

				outStream.close();
				fileStream.close();
				inStream.close();

				if (isInterrupted()) {
					// Se o download foi cancelado, vamos apagar o arquivo
					// que foi baixado pelas metades.
					arquivoSaida.delete();
				} else {
					// Notificar que terminou o download
					msg = Message.obtain(parentActivity.activityHandler,
							PublicacoesGUI.MESSAGE_DOWNLOAD_COMPLETE, 0, 0,
							arquivoLocal);
					parentActivity.activityHandler.sendMessage(msg);
				}

			} else {
				// Notificar à activity que o arquivo existe.
				msg = Message.obtain(parentActivity.activityHandler,
						PublicacoesGUI.MESSAGE_ARQUIVO_EXISTE, 0, 0,
						arquivoLocal);
				parentActivity.activityHandler.sendMessage(msg);
			}
		} catch (MalformedURLException e) {
			String errMsg = parentActivity
					.getString(R.string.error_message_bad_url);
			msg = Message.obtain(parentActivity.activityHandler,
					PublicacoesGUI.MESSAGE_ENCOUNTERED_ERROR, 0, 0, errMsg);
			parentActivity.activityHandler.sendMessage(msg);
		} catch (FileNotFoundException e) {
			String errMsg = parentActivity
					.getString(R.string.error_message_file_not_found);
			msg = Message.obtain(parentActivity.activityHandler,
					PublicacoesGUI.MESSAGE_ENCOUNTERED_ERROR, 0, 0, errMsg);
			parentActivity.activityHandler.sendMessage(msg);
		} catch (Exception e) {
			String errMsg = parentActivity
					.getString(R.string.error_message_general);
			msg = Message.obtain(parentActivity.activityHandler,
					PublicacoesGUI.MESSAGE_ENCOUNTERED_ERROR, 0, 0, errMsg);
			parentActivity.activityHandler.sendMessage(msg);
		}
	}
}