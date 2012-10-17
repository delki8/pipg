package org.pipg.net;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.pipg.beans.Boletim;
import org.pipg.beans.Boletim.Boletins;
import org.pipg.utils.Util;

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
		");"};
	
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
	
	/**
	 * Assinatura do método inserir para inserir uma lista de boletins;
	 * @param boletins é um ArrayList com todos os boletins que serão inseridos. Se algum
	 * deles já possuir id ele será atualizado.
	 * @return True se deu tudo certo e false se deu algum problema;
	 * */
	
	public int inserir(ArrayList<Boletim> boletins){
		int qtdRegistros = 0;
		for (Boletim boletim : boletins) {
			Boletim b = buscarPelaPastoral(boletim);
			if (b == null){
				long id = inserir(boletim);
				if (id > 0) {
					qtdRegistros++;
				}
			}
		}
		return qtdRegistros;
	}
	
	private long inserir(Boletim boletim) {
		ContentValues values = new ContentValues();
		values.put(Boletins.PASTORAL, boletim.getPastoral());
		values.put(Boletins.DATAPUB, boletim.getDataPublicacao().toString());
		values.put(Boletins.LINK, boletim.getLink().toString());
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
	
	/**Apaga todos os boletins do banco.
	 * @return linhasAfetadas é a quantidade de linhas afetadas.
	 * */
	public int limparBanco() {
		Cursor c = getCursor();
		int linhasAfetadas = 0;
		while (c.moveToNext()) {
			Boletim boletim = new Boletim();
			boletim = populaBoletim(c);
			linhasAfetadas += deletar(boletim.getId());
		}
		c.close();
		return linhasAfetadas;
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
			boletim = populaBoletim(c);
			c.close();
			return boletim;
		}
		c.close();
		return null;
	}
	
	private Boletim buscarPelaPastoral(Boletim boletim) {
		String where = Boletins.PASTORAL + "=?";
		String[] whereArgs = new String[] {boletim.getPastoral()};
		Cursor c = db.query(true, NOME_TABELA, Boletim.colunas, where, 
				whereArgs, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Boletim bRecuperado = populaBoletim(c);
			return bRecuperado;
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
			do {
				Boletim boletim = new Boletim();
				boletim = populaBoletim(c);
				boletins.add(boletim);
			} while (c.moveToNext());
		}
		c.close();
		return boletins;
	}
	
	private Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection,
			String where, String[] whereArgs, String groupBy, String having,
			String orderBy){
			Cursor c = queryBuilder.query(this.db, projection, where, whereArgs, 
					groupBy, having, orderBy);
			return c;
	}
	
	/**
	 * Popula um boletim com dados que vierem de um cursor.
	 * @param c é um cursor que possui TODAS as colunas da tabela de boletins.
	 * @return Boletim com todos os dados preenchidos.
	 * */
	private Boletim populaBoletim(Cursor c) {
		Boletim boletim = new Boletim();
		try {
			boletim.setId(c.getLong(c.getColumnIndexOrThrow(
					Boletins._ID)));
			boletim.setPastoral(c.getString(c.getColumnIndexOrThrow(
					Boletins.PASTORAL)));
//			boletim.setDataBoletim(c.getString(c.getColumnIndexOrThrow(Boletins.DATA)));
			Date dataPublicacao = Util.dataStringDate(
					c.getString(c.getColumnIndexOrThrow(Boletins.DATAPUB)));
			boletim.setDataPublicacao(dataPublicacao);
			
			String dataPubFormatada = Util.dataStringString(c.getString(
					c.getColumnIndexOrThrow(Boletins.DATAPUB)));
			boletim.setDataFormatada(dataPubFormatada);
			
			boletim.setLink(new URL(c.getString(c.getColumnIndexOrThrow(
					Boletins.LINK))));
			boletim.setNumero(c.getInt(c.getColumnIndexOrThrow(
					Boletins.NUMERO)));
			return boletim;
		} catch (MalformedURLException e) {
			Log.e(CATEGORIA, "URL inválida." + e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e(CATEGORIA, "Não foi possível popular o boletim]." + 
					e.getMessage());
		}
		return null;
	}
	
	public void fechar() {
		if (dbHelper != null) {
			dbHelper.close();
		}
		if (db != null){
			db.close();
		}
	}
}
