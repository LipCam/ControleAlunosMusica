package br.com.controlealunosmusica.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;
import br.com.controlealunosmusica.R;

public class TempAulas {
    public int ID_TEMP_INT;
    public int ID_ITEM_INT;
    public int ID_TIPO_INT = 0;
    public String INSTRUTOR_STR;
    public Date DATA_DTI;
    public boolean CONCLUIDO_BIT = false;
    public String ASSUNTO_STR;
    public String OBSERVACAO_STR;
    public boolean FLAG_BIT = false;
    
    public String TIPO_STR;
    public String STATUS_STR;

    Context context;
    private SQLiteDatabase db;
    private DadosOpenHelper dadosOpenHelper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TempAulas(Context context) {
        if(context != null) {
            this.context = context;
        }
    }

    private SQLiteDatabase CriaConexaDb()
    {
        return new DadosOpenHelper(context).getWritableDatabase();
    }

    public void LimpaTempAulas(){
        SQLiteDatabase db = CriaConexaDb();
        db.delete("TEMP_AULAS_TAB", "", null);
        db.close();
    }

    public List<TempAulas> GetById(boolean SelTodos) {
        SelTodosTempAulas(SelTodos);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, TP.DESCRICAO_STR AS TIPO_STR, " +
                "CASE WHEN A.CONCLUIDO_BIT = 0 THEN 'Pendente' ELSE 'Concluído' END AS STATUS_STR " +
                "FROM TEMP_AULAS_TAB A " +
                "INNER JOIN SIS_TIPOS_AULA_TAB TP ON TP.ID_TIPO_INT = A.ID_TIPO_INT " +
                "WHERE ID_TEMP_INT = ? " +
                "ORDER BY DATA_DTI DESC");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_TEMP_INT);

        return GetList(sql, param);
    }

    private void SelTodosTempAulas(boolean SelTodos)
    {
        SQLiteDatabase db = CriaConexaDb();
        db.execSQL("UPDATE TEMP_AULAS_TAB SET FLAG_BIT = " + (SelTodos ? 1 : 0) +
                " WHERE ID_TEMP_INT = " + ID_TEMP_INT);
    }

    public List<TempAulas> GetByFlagTrue() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * " +
                "FROM TEMP_AULAS_TAB " +
                "WHERE ID_TEMP_INT = ? AND FLAG_BIT = 1");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_TEMP_INT);

        return GetList(sql, param);
    }

    private List<TempAulas> GetList(StringBuilder sql, String[] param){
        SQLiteDatabase db = CriaConexaDb();
        Cursor result = db.rawQuery(sql.toString(), param);

        List<TempAulas> lst = new ArrayList<TempAulas>();

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                TempAulas aulas = Carrega(result);
                lst.add(aulas);
            } while (result.moveToNext());
        }
        db.close();

        return lst;
    }

    public TempAulas Find() {
        SQLiteDatabase db = CriaConexaDb();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM TEMP_AULAS_TAB " +
                "WHERE ID_TEMP_INT = ? AND ID_ITEM_INT = ?");

        String[] param = new String[2];
        param[0] = String.valueOf(ID_TEMP_INT);
        param[1] = String.valueOf(ID_ITEM_INT);

        Cursor result = db.rawQuery(sql.toString(), param);
        TempAulas tempAulas = new TempAulas(context);

        if (result.getCount() > 0) {
            result.moveToFirst();
            tempAulas = Carrega(result);
        }
        db.close();

        return tempAulas;
    }

    private TempAulas Carrega(Cursor result) {
        TempAulas aulas = new TempAulas(context);
        aulas.ID_TEMP_INT = result.getInt(result.getColumnIndexOrThrow("ID_TEMP_INT"));
        aulas.ID_ITEM_INT = result.getInt(result.getColumnIndexOrThrow("ID_ITEM_INT"));
        aulas.ID_TIPO_INT = result.getInt(result.getColumnIndexOrThrow("ID_TIPO_INT"));
        aulas.INSTRUTOR_STR = result.getString(result.getColumnIndexOrThrow("INSTRUTOR_STR"));

        String Data = result.getString(result.getColumnIndexOrThrow("DATA_DTI"));
        try {
            Date date = sdf.parse(Data);
            aulas.DATA_DTI = date;
        } catch (Exception e) {

        }

        aulas.CONCLUIDO_BIT = (result.getString(result.getColumnIndexOrThrow("CONCLUIDO_BIT")).equals("1"));
        aulas.ASSUNTO_STR = result.getString(result.getColumnIndexOrThrow("ASSUNTO_STR"));
        aulas.OBSERVACAO_STR = result.getString(result.getColumnIndexOrThrow("OBSERVACAO_STR"));
        aulas.FLAG_BIT = (result.getString(result.getColumnIndexOrThrow("FLAG_BIT")).equals("1"));

        try {
            aulas.TIPO_STR = result.getString(result.getColumnIndexOrThrow("TIPO_STR"));
            aulas.STATUS_STR = result.getString(result.getColumnIndexOrThrow("STATUS_STR"));
        }
        catch (Exception ex){}

        return aulas;
    }

    public String Add() {
        SQLiteDatabase db = CriaConexaDb();
        ContentValues contentValues = PutDados();

        long Result = db.insertOrThrow("TEMP_AULAS_TAB", null, contentValues);
        db.close();

        if (Result == -1)
            return  context.getString(R.string.msgAddErro);
        else
            return context.getString(R.string.msgAddOk);
    }

    public Integer GetMaxCode() {
        SQLiteDatabase db = CriaConexaDb();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(MAX(ID_ITEM_INT), 0) + 1 AS COD " +
                "FROM TEMP_AULAS_TAB " +
                "WHERE ID_TEMP_INT = ?");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_TEMP_INT);

        Cursor result = db.rawQuery(sql.toString(), param);
        result.moveToFirst();
        return result.getInt(result.getColumnIndexOrThrow("COD"));
    }

    private ContentValues PutDados()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_TEMP_INT", ID_TEMP_INT);
        contentValues.put("ID_ITEM_INT", ID_ITEM_INT);
        contentValues.put("ID_TIPO_INT", ID_TIPO_INT);
        contentValues.put("INSTRUTOR_STR", INSTRUTOR_STR);

        if(DATA_DTI != null) {
            String Data = sdf.format(DATA_DTI);
            contentValues.put("DATA_DTI", Data);
        }
        else
            contentValues.putNull("DATA_DTI");

        contentValues.put("CONCLUIDO_BIT", CONCLUIDO_BIT);
        contentValues.put("ASSUNTO_STR", ASSUNTO_STR);
        contentValues.put("OBSERVACAO_STR", OBSERVACAO_STR);
        contentValues.put("FLAG_BIT", FLAG_BIT);

        return contentValues;
    }

    public String Delete() {
        long Result = db.delete("TEMP_AULAS_TAB", "", null);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgDeleteErro);
        else
            return context.getString(R.string.msgDeleteOk);
    }

    public void SelTemp(int IdTemp, int IdItem, boolean Sel)
    {
        SQLiteDatabase db = CriaConexaDb();
        db.execSQL("UPDATE TEMP_AULAS_TAB SET FLAG_BIT = " + (Sel ? 1 : 0) +
                " WHERE ID_TEMP_INT = " + IdTemp + " AND ID_ITEM_INT = " + IdItem);
    }
}
