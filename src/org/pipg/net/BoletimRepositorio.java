package org.pipg.net;

import java.util.ArrayList;
import java.util.List;

import org.pipg.beans.Boletim;
import org.pipg.beans.Boletim.Boletins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class BoletimRepositorio {
	private static final String CATEGORIA = "pipg";
	
	private static final String NOME_BANCO = "pipg";
	private static final int VERSAO_BANCO = 1;
	public static final String NOME_TABELA = "boletim";
	private static final String[] SCRIPT_DATABASE_CREATE =
			new String[] {
		"CREATE TABLE " + NOME_TABELA + "(" +
		Boletins._ID + " INTEGER PRIMARY KEY," +
		Boletins.PASTORAL + " TEXT NOT NULL," +
		Boletins.DATA + " TEXT, " +
		Boletins.DATAPUB + " TEXT NOT NULL, " +
		Boletins.LINK + " TEXT NOT NULL, " +
		Boletins.NUMERO + " INTEGER" +
		");",
		
		"INSERT INTO " + NOME_TABELA +
		"(" + 
			Boletins.PASTORAL + ", " +
			Boletins.DATA + ", " +
			Boletins.DATAPUB + ", " +
			Boletins.LINK + ", " +
			Boletins.NUMERO +
		") " +
		"VALUES ('Deus ama a todos','2012-09-10','2012-10-10'," +
		"'http://www.link.url.com',12);"};
	
	private static final String SCRIPT_DATABASE_DELETE = 
			"DROP TABLE IF EXISTS " + NOME_TABELA;
	
	protected SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	
	public BoletimRepositorio(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, BoletimRepositorio.NOME_BANCO,
				BoletimRepositorio.VERSAO_BANCO,
				BoletimRepositorio.SCRIPT_DATABASE_CREATE,
				BoletimRepositorio.SCRIPT_DATABASE_DELETE);
		db = dbHelper.getWritableDatabase();
	}
	
	private BoletimRepositorio() {
		super();
	}
	
	/* Insere ou atualiza um boletim, privado at'e
	 * eu ter certeza de quem vai acessar.*/
	private long salvar(Boletim boletim) {
		long id = boletim.getId();
		if (id != 0){
			atualizar(boletim);
		} else {
			id = inserir(boletim);
		}
		return id;
	}
	
	private long inserir(Boletim boletim) {
		ContentValues values = new ContentValues();
		values.put(Boletins.PASTORAL, boletim.getPastoral());
		values.put(Boletins.DATAPUB, boletim.getDataPublicacao().toString());
		long id = inserir(values);
		return id;
	}
	
	private long inserir(ContentValues values) {
		long id = db.insert(NOME_TABELA, "", values);
		return id;
	}
	
	private int atualizar(Boletim boletim) {
		ContentValues values = new ContentValues();
		values.put(Boletins.PASTORAL, boletim.getPastoral());
		values.put(Boletins.DATAPUB, boletim.getPastoral());
		String _id = String.valueOf(boletim.getId());
		String where = Boletins._ID + "=? ";
		String whereArgs[] = new String[] { _id };
		int count = atualizar(values, where, whereArgs);
		return count;
	}
	
	private int atualizar(ContentValues values, String where, 
			String[] whereArgs) {
		int count = db.update(NOME_TABELA, values, where, whereArgs);
		Log.i(CATEGORIA, "Atualizou [" + count + "] registros");
		return count;
	}
	
	private int deletar(long id) {
		String _id = String.valueOf(id);
		String where = Boletins._ID + "=?";
		String[] whereArgs = new String[] { _id };
		int count = deletar(where, whereArgs);
		return count;
	}
	
	private int deletar (String where, String[] whereArgs) {
		int count = db.delete(NOME_TABELA, where, whereArgs);
		Log.i(CATEGORIA, "Deletou [" + count + "] registros");
		return count;
	}
	
	private Boletim buscarBoletim(long id) {
		Cursor c = db.query(true, NOME_TABELA, Boletim.colunas, 
				Boletins._ID + "=" + id, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Boletim boletim = new Boletim();
			boletim.setId(c.getLong(c.getColumnIndexOrThrow(Boletins._ID)));
			boletim.setPastoral(c.getString(c.getColumnIndexOrThrow(Boletins.PASTORAL)));
			// TODO tratar a data aqui na linha
//			boletim.setDataPublicacao(c.getString(c.getColumnIndexOrThrow(Boletins.DATAPUB)));
			c.close();
			return boletim;
		}
		c.close();
		return null;
	}
	
	private Cursor getCursor() {
		try {
			return db.query(NOME_TABELA, Boletim.colunas, null, null, null, 
					null, null, null);
		} catch (SQLException e) {
			Log.e(CATEGORIA, "Erro ao buscar os boletins: " + e.toString());
			return null;
		}
	}
	
	public ArrayList<Boletim> listarBoletins() {
		Cursor c = getCursor();
		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
		if (c.moveToFirst()) {
			int idxId = c.getColumnIndexOrThrow(Boletins._ID);
			int idxPastoral = c.getColumnIndexOrThrow(Boletins.PASTORAL);
			int idxDataPub = c.getColumnIndexOrThrow(Boletins.DATAPUB);
			
			do {
				Boletim boletim = new Boletim();
				boletins.add(boletim);
				boletim.setId(c.getLong(idxId));
				boletim.setPastoral(c.getString(idxPastoral));
				// TODO tratar data aqui
//				boletim.setDataPublicacao(c.getString((idxDataPub));
			} while (c.moveToNext());
		}
		return boletins;
	}
	
	private Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
			String where, String[] whereArgs, String groupBy, String having,
			String orderBy){
			Cursor c = queryBuilder.query(this.db, projection, where, whereArgs, 
					groupBy, having, orderBy);
			return c;
	}
	
	private void fechar() {
		if (db != null){
			db.close();
		}
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
