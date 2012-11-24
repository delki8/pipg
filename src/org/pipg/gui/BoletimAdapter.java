package org.pipg.gui;

import java.io.File;
import java.util.List;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.net.DownloaderThread;
import org.pipg.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
			LayoutInflater li = (LayoutInflater) activityPai.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.boletim_linha, parent, false);
			
			
			bHolder = new BoletimHolder();
			bHolder.pastoral = (TextView) convertView.findViewById(R.id.pastoral);
			bHolder.dataPublicacao = (TextView) convertView.findViewById(R.id.data_publicacao);
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

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView urlInputField =  (TextView) v.findViewById(R.id.link);
				String urlInput = urlInputField.getText().toString();
				downloaderThread = new DownloaderThread(activityPai, urlInput);
				downloaderThread.start();
			}
		});
		
		// Setando o clique para abrir arquivos.
		ImageView openImg = (ImageView) convertView.findViewById(R.id.icone_lateral);
		
		String nomeArquivo = Util.nomeArquivoDaUrl(bHolder.boletim.getLink().toString());
		final String caminho = Environment.getExternalStorageDirectory() + "/pipg/" + nomeArquivo;
		
		openImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				File file = new File(caminho);
				if (file.isFile()) {
					intent.setDataAndType(Uri.fromFile(file), "application/pdf");
					activityPai.startActivity(intent);
				} else {
					Toast.makeText(activityPai, "Arquivo ainda não foi baixado.", 
							Toast.LENGTH_SHORT).show();
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
