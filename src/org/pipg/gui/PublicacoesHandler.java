/**
 * 
 */
package org.pipg.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimControl;
import org.pipg.gui.PublicacoesGUI.SectionFragment;
import org.pipg.net.BoletimRepositorio;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author ruither
 * 
 */
public class PublicacoesHandler extends Handler {

	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
	public static final int MESSAGE_CONNECTING_STARTED = 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
	public static final int MESSAGE_TERMINOU_UPDATE = 1006;
	public static final int MESSAGE_INICIOU_UPDATE = 1007;
	public static final int MESSAGE_TERMINOU_LIMPAR = 1008;
	public static final int MESSAGE_ARQUIVO_EXISTE = 1009;
	public static final int MESSAGE_CONFIRM_DOWNLOAD = 1010;
	public static final int MESSAGE_NO_PDF_APPLICATION = 1011;

	private PublicacoesGUI thisActivity;
	private static ArrayList<Boletim> boletins;
	private static BoletimAdapter adapter;

	private ProgressDialog progressDialog;

	public PublicacoesHandler(PublicacoesGUI activity, ArrayList<Boletim> boletins, BoletimAdapter adapter) {
		this.thisActivity = activity;
		PublicacoesHandler.boletins = boletins;
		PublicacoesHandler.adapter = adapter;
		this.progressDialog = activity.getProgressDialog();
	}

	public void handleMessage(Message msg) {
		File file = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
		switch (msg.what) {
		/*
		 * Handling MESSAGE_UPDATE_PROGRESS_BAR: 1. Get the current prorfess, as
		 * indicated in arg1 field of the Message. 2. Update the progress bar.
		 */
		case MESSAGE_UPDATE_PROGRESS_BAR:
			if (progressDialog != null) {
				int currentProgress = msg.arg1;
				progressDialog.setProgress(currentProgress);
			}
			break;
		/*
		 * Handling MESSAGE_CONNECTING_STARTED: 1. Get the URL of the file being
		 * downloaded. This is stored in the obj field of the Message. 2. Create
		 * an indeterminate progress bar; 3. Show the progress bar.
		 */
		case MESSAGE_CONNECTING_STARTED:
			if (msg.obj != null && msg.obj instanceof String) {
				String url = (String) msg.obj;
				// truncate the url
				if (url.length() > 16) {
					String tUrl = url.substring(0, 15);
					tUrl += "...";
					url = tUrl;
				}
				String pdTitle = thisActivity.getString(R.string.progress_dialog_title_connecting);
				String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_connecting);
				pdMsg += " " + url;

				dismissCurrentProgressDialog();
				progressDialog = new ProgressDialog(thisActivity);
				progressDialog.setTitle(pdTitle);
				progressDialog.setMessage(pdMsg);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setIndeterminate(true);
				// set the message to be sent when this dialog is canceled
				Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
				progressDialog.setCancelMessage(newMsg);
				progressDialog.show();
			}
			break;
		/*
		 * Handling MESSAGE_DOWNLOAD_STARTED: 1. Create a progress bar with
		 * specified max value and current value 0; assign it to progressDialog.
		 * The arg1 field will contain the max value. 2. Set the title and text
		 * for the progress bar. The obj field of the Message will contain a
		 * String that represents the name of the file being downloaded. 3. Set
		 * the message that shoud be sent if dialog is canceled. 4. Make the
		 * progress bar visible.
		 */
		case MESSAGE_DOWNLOAD_STARTED:
			// obj contém uma string que representa o nome do arquivo
			if (msg.obj != null && msg.obj instanceof String) {
				int maxValue = msg.arg1;
				String fileName = (String) msg.obj;
				String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
				String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_downloading);
				pdMsg += " " + fileName;

				dismissCurrentProgressDialog();
				progressDialog = new ProgressDialog(thisActivity);
				progressDialog.setTitle(pdTitle);
				progressDialog.setMessage(pdMsg);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setProgress(0);
				progressDialog.setMax(maxValue);
				// set the message to be sent when this dialog is canceled
				Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
				progressDialog.setCancelMessage(newMsg);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}
			break;
		/*
		 * Handling MESSAGE_DOWNLOAD_COMPLETE 1. Remove the progress bar from
		 * the screen. 2. Display Toast that says download is complete.
		 */
		case MESSAGE_DOWNLOAD_COMPLETE:
			dismissCurrentProgressDialog();
			displayMessage(thisActivity.getString(R.string.user_message_download_complete));
			file = (File) msg.obj;
			abreArquivo(file);
			break;
		/*
		 * Handling MESSAGE_DOWNLOAD_CANCELED: 1. Interrupt the downloader
		 * thread. 2. Remove the progress bar from the screen. 3. Display Toast
		 * that says download is complete.
		 */
		case MESSAGE_DOWNLOAD_CANCELED:
			// if (downloaderThread != null) {
			// downloaderThread.interrupt();
			// }
			// thisActivity.dismissCurrentProgressDialog();
			// displayMessage(getString(
			// R.string.user_message_download_canceled));
			break;
		/*
		 * Handling MESSAGE_ENCOUNTERED_ERROR: 1. Check the obj field of the
		 * message for the actual error message that will be displayed to the
		 * user. 2. Remove any progress bars from the screen. 3. Display a Toast
		 * with the error message.
		 */
		case MESSAGE_ENCOUNTERED_ERROR:
			// obj will contain a string representing the error message
			if (msg.obj != null && msg.obj instanceof String) {
				String errorMessage = (String) msg.obj;
				dismissCurrentProgressDialog();
				displayMessage(errorMessage);
			}
			break;
		/*
		 * Tratando MESSAGE_INICIOU_UPDATE 1. Verifica se é a atualização
		 * completa e personaliza a mensagem de acordo com o resultado.
		 */
		case MESSAGE_INICIOU_UPDATE:
			boolean atualizacaoCompleta = (Boolean) msg.obj;
			if (atualizacaoCompleta) {
				this.abrirFecharCarregando(R.string.user_info_update_all_of_them, true);
			} else {
				this.abrirFecharCarregando(R.string.user_info_update_last_10, true);
			}
			break;

		/*
		 * Tratando MESSAGE_TERMINOU_UPDATE 1. Instancia o repositorio de
		 * boletins e atualiza a lista de boletins recém cadastrados. 2. Pega a
		 * nova lista de boletins e manda para o método de atualização da tela.
		 */
		case MESSAGE_TERMINOU_UPDATE:
			BoletimRepositorio br = new BoletimRepositorio(thisActivity);
			List<Boletim> bols = br.listarBoletins();
			atualizaAdapter(bols);
			this.abrirFecharCarregando(0, false);
			break;

		/*
		 * Tratando MESSAGE_TERMINOU_LIMPAR 1. Chama o método de atualizar a
		 * tela assim que termina de limpar os registros do banco.
		 */
		case MESSAGE_TERMINOU_LIMPAR:
			Toast.makeText(thisActivity, msg.arg1 + " registros apagados", Toast.LENGTH_SHORT).show();
			atualizaAdapter(new ArrayList<Boletim>());
			break;

		/*
		 * Tratando MESSAGE_ARQUIVO_EXISTE 1. Dispara intent para abrir o
		 * arquivo existente sem fazer download.
		 */
		case MESSAGE_ARQUIVO_EXISTE:
			file = (File) msg.obj;
			abreArquivo(file);
			break;

		case MESSAGE_CONFIRM_DOWNLOAD:
			final String link = (String) msg.obj;

			String m = "O arquivo possui " + msg.arg1 + "Kb. Tem certeza que deseja baixa-lo?";
			builder.setTitle(R.string.baixar);
			builder.setMessage(m);
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					BoletimControl bControl = new BoletimControl();
					bControl.iniciarDownload(thisActivity, link);
				}
			});

			builder.setNegativeButton(R.string.n_o, null);
			AlertDialog alert = builder.create();
			alert.show();
			break;

		case MESSAGE_NO_PDF_APPLICATION:
			dismissCurrentProgressDialog();
			displayMessage(thisActivity.getString(R.string.error_no_pdf_application));
			break;
		}
	}

	private void atualizaAdapter(Collection<Boletim> bols) {
		boletins.clear();
		boletins.addAll(bols);
		ListView l = (ListView) thisActivity.findViewById(SectionFragment.ID_MAIN_LIST);
		l.setAdapter(adapter);
	}

	protected void abreArquivo(File file) {
		try {
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
			thisActivity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Message msg = Message.obtain(this, MESSAGE_NO_PDF_APPLICATION, 0, 0);
			sendMessage(msg);
		}
	}

	/**
	 * If there is a progress dialog, dismiss it and set progressDialog to null.
	 * */
	public void dismissCurrentProgressDialog() {
		if (progressDialog != null) {
			progressDialog.hide();
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * Display a message to the user, in the form of a Toast.
	 * 
	 * @param message
	 *            Message to be displayed.
	 * */
	public void displayMessage(String message) {
		if (message != null) {
			Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Abre ou fecha o carregando na activity;
	 * 
	 * @param descricao
	 *            é a mensagem que será mostrada para o usuário durante o
	 *            alerta.
	 * @param abrir
	 *            é o flag que irá informar se será necessário abrir ou fechar a
	 *            mensagem. 'true' para abrir e 'false' para fechar.
	 * @author delki8
	 * */
	private void abrirFecharCarregando(int descricao, boolean abrir) {
		NotificationManager mNotifyMgr = (NotificationManager) thisActivity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int idNotificacao = 001;
		if (abrir) {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(thisActivity)
					.setSmallIcon(R.drawable.navigationrefresh_holo_dark)
					.setContentTitle(thisActivity.getText(R.string.user_message_update_list))
					.setContentText(thisActivity.getText(descricao));
			mNotifyMgr.notify(idNotificacao, mBuilder.build());
		} else {
			mNotifyMgr.cancel(idNotificacao);
		}
	}
}