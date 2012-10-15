package org.pipg.net;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String CATEGORIA = "pipg";
	private String[] scriptSQLCreate;
	private String scriptSQLDelete;
	
	public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, 
			String[] scriptSQLCreate, String scriptSQLDelete) {
		super(context, nomeBanco, null, versaoBanco);
		this.scriptSQLCreate = scriptSQLCreate;
		this.scriptSQLDelete = scriptSQLDelete;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(CATEGORIA, "Criando banco com sql");
		int qtdeScripts = scriptSQLCreate.length;
		
		for (int j = 0; j < qtdeScripts; j++) {
			String sql = scriptSQLCreate[j];
			Log.i(CATEGORIA, sql);
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versaoAntiga, 
			int novaVersao) {
		Log.w(CATEGORIA, "Atualizando a versão " + versaoAntiga + " para " +
				novaVersao + ". Todos os registros serão deletados.");
		Log.i(CATEGORIA, scriptSQLDelete);
		//Deleta as tabelas
		db.execSQL(scriptSQLDelete);
		//Cria novamente...
		onCreate(db);
	}

}
