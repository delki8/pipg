package org.pipg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainTeste extends Activity{
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		Button b = new Button(this);
		b.setText("Clique em mim para iniciar o servico");
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("BOLETIM");
				startService(intent);
			}
		});
		setContentView(b);
	}
}
