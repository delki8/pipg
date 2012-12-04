package org.pipg.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimControl;
import org.pipg.net.AbrirArquivoThread;
import org.pipg.net.DownloaderThread;
import org.pipg.utils.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BoletimAdapter extends BaseAdapter {
	private PublicacoesGUI activityPai;
	private List<Boletim> lista;
	private Thread downloaderThread;

	public BoletimAdapter(PublicacoesGUI activityPai, List<Boletim> lista) {
		super();
		this.activityPai = activityPai;
		this.lista = lista;
		downloaderThread = null;
	}

	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Object getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		BoletimHolder bHolder;

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) activityPai
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.boletim_linha, parent, false);

			bHolder = new BoletimHolder();
			bHolder.pastoral = (TextView) convertView
					.findViewById(R.id.pastoral);
			bHolder.dataPublicacao = (TextView) convertView
					.findViewById(R.id.data_publicacao);
			bHolder.link = (TextView) convertView.findViewById(R.id.link);

			bHolder.boletim = lista.get(position);
			convertView.setTag(bHolder);

		} else {
			bHolder = (BoletimHolder) convertView.getTag();
			bHolder.boletim = lista.get(position);
		}

		bHolder.pastoral.setText(bHolder.boletim.getPastoral());
		bHolder.dataPublicacao.setText(bHolder.boletim.getDataFormatada());
		bHolder.link.setText(bHolder.boletim.getLink().toString());

		// Remover o efeito de clique na parte não-clicável da linha.
		convertView.setOnClickListener(null);

		// Setando o clique para abrir arquivos.
		ImageView openImg = (ImageView) convertView.findViewById(R.id.openImg);

		TextView linkNoSite = (TextView) convertView.findViewById(R.id.link);
		final String caminhoExterno = linkNoSite.getText().toString();

		openImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String caminhoLocal = Util.ENDERECO_LOCAL
						+ Util.nomeArquivoDaUrl(caminhoExterno);
				File f = new File(caminhoLocal);
				/*
				 * Só deve mostrar a opção de baixar o arquivo se ele ainda não
				 * existe caso contrário deve-se apenas abri-lo. Aqui estou
				 * usando o tratamento já feito na Thread de download para abrir
				 * o arquivo.
				 */
				if (f.isFile()) {
					BoletimControl bC = new BoletimControl();
					bC.iniciarDownload(activityPai, caminhoExterno);
				} else {
					AbrirArquivoThread aThread = new AbrirArquivoThread(
							activityPai, caminhoExterno);
					aThread.start();
				}
			}
		});

		return convertView;
	}

	private static class BoletimHolder {
		TextView pastoral;
		TextView dataPublicacao;
		TextView link;

		Boletim boletim;
	}

	public void setLista(List<Boletim> lista) {
		this.lista = lista;
	}

}
