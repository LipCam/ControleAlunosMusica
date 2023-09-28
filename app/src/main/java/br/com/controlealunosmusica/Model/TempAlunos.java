package br.com.controlealunosmusica.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.controlealunosmusica.Classes.GlobalClass;
import br.com.controlealunosmusica.DataBase.DadosOpenHelper;
import br.com.controlealunosmusica.R;

public class TempAlunos {
    public int ID_TEMP_INT;
    public int ID_ALUNO_INT;
    public String NOME_STR;
    public int ID_STATUS_INT = 0;
    public String INSTRUMENTO_STR;
    public String METODO_STR;
    public String FONE_STR;
    public Date DATA_NASCIMENTO_DTI;
    public Date DATA_BATISMO_DTI;
    public Date DATA_INICIO_GEM_DTI;
    public Date DATA_OFICIALIZACAO_DTI;
    public String ENDERECO_STR;
    public String OBSERVACAO_STR;
    public boolean FLAG_BIT = false;
    
    public String STATUS_STR;

    Context context;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TempAlunos(Context context) {
        if(context != null) {
            this.context = context;
        }
    }

    private SQLiteDatabase CriaConexaDb()
    {
        return new DadosOpenHelper(context).getWritableDatabase();
    }

    public List<TempAlunos> Get(boolean SelTodos) {
        GlobalClass globalclass = (GlobalClass) context.getApplicationContext();
        if (globalclass.IntentTempAlunos == 0)
            CarregaTempAlunos(SelTodos);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, S.DESCRICAO_STR AS STATUS_STR " +
                "FROM TEMP_ALUNOS_TAB A " +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB S ON S.ID_STATUS_INT = A.ID_STATUS_INT ");
        return GetList(sql, null);
    }

    public void LimpaTempAlunos(){
        SQLiteDatabase db = CriaConexaDb();
        db.delete("TEMP_ALUNOS_TAB", "", null);
        db.close();
    }

    private void CarregaTempAlunos(boolean SelTodos)
    {
        LimpaTempAlunos();

        Alunos objAlunos = new Alunos(context);
        List<Alunos> lstAlunos = objAlunos.Get();
        for (int i = 0; i < lstAlunos.size(); i++) {
            TempAlunos temp = new TempAlunos(context);
            temp.ID_TEMP_INT = GetMaxCode();
            temp.ID_ALUNO_INT = lstAlunos.get(i).ID_ALUNO_INT;
            temp.NOME_STR = lstAlunos.get(i).NOME_STR;
            temp.INSTRUMENTO_STR = lstAlunos.get(i).INSTRUMENTO_STR;
            temp.ID_STATUS_INT =  lstAlunos.get(i).ID_STATUS_INT;
            temp.FLAG_BIT = SelTodos;
            temp.Add();
        }
    }

    private List<TempAlunos> GetList(StringBuilder sql, String[] param){
        SQLiteDatabase db = CriaConexaDb();
        Cursor result = db.rawQuery(sql.toString(), param);

        List<TempAlunos> lst = new ArrayList<TempAlunos>();

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                TempAlunos alunos = Carrega(result);
                lst.add(alunos);
            } while (result.moveToNext());
        }
        db.close();

        return lst;
    }

    public TempAlunos Find() {
        SQLiteDatabase db = CriaConexaDb();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T.*, S.DESCRICAO_STR AS STATUS_STR " +
                "FROM TEMP_ALUNOS_TAB T " +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB S ON S.ID_STATUS_INT = T.ID_STATUS_INT " +
                "WHERE T.ID_TEMP_INT = ?");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_TEMP_INT);

        Cursor result = db.rawQuery(sql.toString(), param);
        TempAlunos tempAlunos = new TempAlunos(context);

        if (result.getCount() > 0) {
            result.moveToFirst();
            tempAlunos = Carrega(result);
        }
        db.close();

        return tempAlunos;
    }

    private TempAlunos Carrega(Cursor result) {
        TempAlunos alunos = new TempAlunos(context);
        alunos.ID_TEMP_INT = result.getInt(result.getColumnIndexOrThrow("ID_TEMP_INT"));
        alunos.ID_ALUNO_INT = result.getInt(result.getColumnIndexOrThrow("ID_ALUNO_INT"));
        alunos.NOME_STR = result.getString(result.getColumnIndexOrThrow("NOME_STR"));

        String Data = result.getString(result.getColumnIndexOrThrow("DATA_NASCIMENTO_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_NASCIMENTO_DTI = date;
        } catch (Exception e) {

        }

        alunos.ID_STATUS_INT = result.getInt(result.getColumnIndexOrThrow("ID_STATUS_INT"));
        alunos.INSTRUMENTO_STR = result.getString(result.getColumnIndexOrThrow("INSTRUMENTO_STR"));
        alunos.METODO_STR = result.getString(result.getColumnIndexOrThrow("METODO_STR"));
        alunos.FONE_STR = result.getString(result.getColumnIndexOrThrow("FONE_STR"));

        Data = result.getString(result.getColumnIndexOrThrow("DATA_BATISMO_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_BATISMO_DTI = date;
        } catch (Exception e) {

        }

        Data = result.getString(result.getColumnIndexOrThrow("DATA_INICIO_GEM_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_INICIO_GEM_DTI = date;
        } catch (Exception e) {

        }

        Data = result.getString(result.getColumnIndexOrThrow("DATA_OFICIALIZACAO_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_OFICIALIZACAO_DTI = date;
        } catch (Exception e) {

        }

        alunos.ENDERECO_STR = result.getString(result.getColumnIndexOrThrow("ENDERECO_STR"));
        alunos.OBSERVACAO_STR = result.getString(result.getColumnIndexOrThrow("OBSERVACAO_STR"));
        alunos.FLAG_BIT = (result.getString(result.getColumnIndexOrThrow("FLAG_BIT")).equals("1"));

        try {
            alunos.INSTRUMENTO_STR = result.getString(result.getColumnIndexOrThrow("INSTRUMENTO_STR"));
            alunos.STATUS_STR = result.getString(result.getColumnIndexOrThrow("STATUS_STR"));
        }
        catch (Exception ex){}

        return alunos;
    }

    public String Add() {
        SQLiteDatabase db = CriaConexaDb();
        ContentValues contentValues = PutDados();

        long Result = db.insertOrThrow("TEMP_ALUNOS_TAB", null, contentValues);
        db.close();

        if (Result == -1)
            return  context.getString(R.string.msgAddErro);
        else
            return context.getString(R.string.msgAddOk);
    }

    public Integer GetMaxCode() {
        SQLiteDatabase db = CriaConexaDb();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(MAX(ID_TEMP_INT), 0) + 1 AS COD FROM TEMP_ALUNOS_TAB");

        Cursor result = db.rawQuery(sql.toString(), null);
        result.moveToFirst();
        return result.getInt(result.getColumnIndexOrThrow("COD"));
    }

    private ContentValues PutDados()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_TEMP_INT", ID_TEMP_INT);
        contentValues.put("ID_ALUNO_INT", ID_ALUNO_INT);
        contentValues.put("NOME_STR", NOME_STR);
        contentValues.put("ID_STATUS_INT", ID_STATUS_INT);
        contentValues.put("INSTRUMENTO_STR", INSTRUMENTO_STR);
        contentValues.put("METODO_STR", METODO_STR);
        contentValues.put("FONE_STR", FONE_STR);

        if(DATA_NASCIMENTO_DTI != null) {
            String Data = sdf.format(DATA_NASCIMENTO_DTI);
            contentValues.put("DATA_NASCIMENTO_DTI", Data);
        }
        else
            contentValues.putNull("DATA_NASCIMENTO_DTI");

        if(DATA_BATISMO_DTI != null) {
            String Data = sdf.format(DATA_BATISMO_DTI);
            contentValues.put("DATA_BATISMO_DTI", Data);
        }
        else
            contentValues.putNull("DATA_BATISMO_DTI");

        if(DATA_INICIO_GEM_DTI != null) {
            String Data = sdf.format(DATA_INICIO_GEM_DTI);
            contentValues.put("DATA_INICIO_GEM_DTI", Data);
        }
        else
            contentValues.putNull("DATA_INICIO_GEM_DTI");

        if(DATA_OFICIALIZACAO_DTI != null) {
            String Data = sdf.format(DATA_OFICIALIZACAO_DTI);
            contentValues.put("DATA_OFICIALIZACAO_DTI", Data);
        }
        else
            contentValues.putNull("DATA_OFICIALIZACAO_DTI");

        contentValues.put("ENDERECO_STR", ENDERECO_STR);
        contentValues.put("OBSERVACAO_STR", OBSERVACAO_STR);
        contentValues.put("FLAG_BIT", FLAG_BIT);

        return contentValues;
    }

    public String Delete() {
        SQLiteDatabase db = CriaConexaDb();
        long Result = db.delete("TEMP_ALUNOS_TAB", "", null);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgDeleteErro);
        else
            return context.getString(R.string.msgDeleteOk);
    }

    public void SelTemp(int IdTemp, boolean Sel)
    {
        SQLiteDatabase db = CriaConexaDb();
        db.execSQL("UPDATE TEMP_ALUNOS_TAB SET FLAG_BIT = " + (Sel ? 1 : 0) +
                " WHERE ID_TEMP_INT = " + IdTemp);
    }
}
