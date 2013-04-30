package org.pipg.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.pipg.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Util {

	public static final String ENDERECO_LOCAL = Environment
			.getExternalStorageDirectory() + "/pipg/";
	public static final String LOG = "pipg";
	public static final String URL_ATUALIZACAO_PARCIAL = "http://pipg.org/index.cfm?p=bulletins";

	/**
	 * Recebe uma string no formato "EEE MMM dd HH:mm:ss zzz yyyy" e retorna uma
	 * java.util.Date.
	 * 
	 * @param dataString
	 *            é uma string no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * @return data convertida.
	 * */
	public static Date dataStringDate(String dataString) {
		try {
			dataString = dataString.replace("BRT", "GMT+00:00");
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
			Date data = sdf.parse(dataString);
			return data;
		} catch (ParseException e) {
			Log.e(LOG, "Erro ao converter. " + e.getMessage());
		}
		return null;
	}

	/**
	 * Pega qualquer java.util.Date e formata para o formato desejado.
	 * 
	 * @param data
	 *            com a data a ser formatada.
	 * @param formato
	 *            com o formato desejado Ex: "dd/MM/yyyy"
	 * @return dataFormatada de acordo com o formato informado.
	 * */
	public static String dataDateString(Date data, String formato) {
		if (data != null) {
			String dataFormatada = null;
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			dataFormatada = sdf.format(data);
			return dataFormatada;
		}
		return null;
	}

	/**
	 * Recebe uma String de data no formato "EEE MMM dd HH:mm:ss zzz yyyy" e
	 * formata ela para "dd/MM/yyyy"
	 * 
	 * @param dataString
	 *            no formato "EEE MMM dd HH:mm:ss zzz yyyy"
	 * @return dataFormatada no formato "dd/MM/yyyy"
	 * */
	public static String dataStringString(String dataString) {
		if (dataString != null) {
			Date data = dataStringDate(dataString);
			String dataFormatada = dataDateString(data, "dd/MM/yyyy");
			return dataFormatada;
		}
		return null;
	}

	/**
	 * Pega uma String no formato informado e transforma num java.util.Date
	 * 
	 * @param data
	 *            no formato informado.
	 * @param formato
	 *            em que a data está chegando.
	 * @return Data referente ao formato personalizado ou null caso não seja
	 *         possível formatar.
	 * */
	public static Date formatDateCustom(String dataString, String formato,
			Locale local) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formato, local);
			Date dataRetorno = sdf.parse(dataString);
			return dataRetorno;
		} catch (ParseException e) {
			Log.e(LOG, "A data: " + dataString + " não está no formato: "
					+ formato);
		}
		return null;
	}

	/**
	 * Encontra o domingo mais próximo de uma determinada data.
	 * 
	 * @param dataDePublicacao
	 *            é a data usada como parâmetro, a data retornada é o domingo
	 *            mais próximo à data informada aqui.
	 * @return java.util.Date correspondente ao domingo mais próximo da data
	 *         informada no parâmetro.
	 * */
	public static Date domingoMaisProximo(Date dataDePublicacao) {
		Date dataDomingo = null;
		Calendar cal = Calendar.getInstance();
		Log.i(LOG, dataDePublicacao.toString());
		cal.setTime(dataDePublicacao);
		int incremento = 1;
		if (cal.get(Calendar.DAY_OF_WEEK) < Calendar.THURSDAY) {
			incremento = -1;
		}

		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal.add(Calendar.DATE, incremento);
		}

		dataDomingo = new Date(cal.getTimeInMillis());
		return dataDomingo;
	}

	/**
	 * Retorna o nome de um arquivo a partir de um endereço. O método usa a
	 * última '/' no endereço para capturar o nome do arquivo.
	 * 
	 * @param Endereço
	 *            do arquivo (local ou na web).
	 * @return Nome do arquivo que está no final do endereço.
	 * */
	public static String nomeArquivoDaUrl(String linkArquivoSite) {
		int lastSlash = linkArquivoSite.toString().lastIndexOf('/');
		String nomeArquivo = "boletim_nao_encontrado.pdf";
		if (lastSlash >= 0) {
			nomeArquivo = linkArquivoSite.toString().substring(lastSlash + 1);
		}
		if (nomeArquivo.equals("")) {
			nomeArquivo = "boletim_nao_encontrado.pdf";
		}
		return nomeArquivo;
	}

	/**
	 * Apaga o diretorio com todos arquivos e subdiretorios.
	 * 
	 * @param diretorio
	 *            raiz que será apagado.
	 * */
	public static void apagaDiretorio(File dir) {
		if (dir.isDirectory()) {
			File[] arquivos = dir.listFiles();
			if (arquivos.length > 0) {
				for (File arquivo : arquivos) {
					if (arquivo.isDirectory()) {
						apagaDiretorio(arquivo);
					} else {
						arquivo.delete();
					}
				}
			}
			dir.delete();
		}
	}

	/**
	 * Informa o tamanho do arquivo referenciado na url. É preciso ser chamado
	 * de dentro de um Thread.start().
	 * 
	 * @author delki8
	 * @data 03/12/2012
	 * @param urlDownload
	 *            é a url de download do arquivo que será calculado.
	 * */
	public static int tamanhoArquivo(String urlDownload) throws IOException {
		final URL url = new URL(urlDownload);
		final URLConnection conn = url.openConnection();
		int tamanhoArquivo = conn.getContentLength();
		return tamanhoArquivo;
	}

	/**
	 * @return true se houver conexão com a internet.
	 * */
	public static boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
