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

public class Alunos {
    public int ID_ALUNO_INT = 0;
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
    public Date DATA_IMPORTACAO_DTI;

    public String STATUS_STR;

    Context context;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Alunos(Context context) {
        if(context != null) {
            this.context = context;
        }
    }

    private SQLiteDatabase CriaConexaDb()
    {
        return new DadosOpenHelper(context).getWritableDatabase();
    }

    public List<Alunos> Get() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, S.DESCRICAO_STR AS STATUS_STR " +
                "FROM CAD_ALUNOS_TAB A " +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB S ON S.ID_STATUS_INT = A.ID_STATUS_INT " +
                "ORDER BY NOME_STR");
        return GetList(sql, null);
    }

    public List<Alunos> GetByNome(String Nome) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, S.DESCRICAO_STR AS STATUS_STR " +
                "FROM CAD_ALUNOS_TAB A " +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB S ON S.ID_STATUS_INT = A.ID_STATUS_INT " +
                "WHERE A.NOME_STR LIKE ? " +
                "ORDER BY NOME_STR");

        String[] param = new String[1];
        param[0] = "%" + Nome + "%";

        return GetList(sql, param);
    }

    public List<Alunos> GetComboAlunos() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM CAD_ALUNOS_TAB" +
                " ORDER BY NOME_STR");
        return GetList(sql, null);
//        List<Alunos> lst = new ArrayList<>();
//        Alunos alunos = new Alunos(null);
//        alunos.ID_ALUNO_INT = 0;
//        alunos.NOME_STR = "Selecione";
//        lst.add(alunos);
//
//        List<Alunos> lstAux = GetList(sql, null);
//        for(int i = 0; i < lstAux.size(); i++){
//            lst.add(lstAux.get(i));
//        }
//
//        return lst;
    }

    private List<Alunos> GetList(StringBuilder sql, String[] param){
        SQLiteDatabase db = CriaConexaDb();
        Cursor result = db.rawQuery(sql.toString(), param);

        List<Alunos> lst = new ArrayList<Alunos>();

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                Alunos alunos = Carrega(result);
                lst.add(alunos);
            } while (result.moveToNext());
        }
        db.close();

        return lst;
    }

    public Alunos Find() {
        SQLiteDatabase db = CriaConexaDb();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM CAD_ALUNOS_TAB " +
                    "WHERE ID_ALUNO_INT = ?");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_ALUNO_INT);

        Cursor result = db.rawQuery(sql.toString(), param);
        Alunos alunos = new Alunos(context);

        if (result.getCount() > 0) {
            result.moveToFirst();
            alunos = Carrega(result);
        }
        db.close();

        return alunos;
    }

    private Alunos Carrega(Cursor result) {
        Alunos alunos = new Alunos(context);
        alunos.ID_ALUNO_INT = result.getInt(result.getColumnIndexOrThrow("ID_ALUNO_INT"));
        alunos.NOME_STR = result.getString(result.getColumnIndexOrThrow("NOME_STR"));
        alunos.ID_STATUS_INT = result.getInt(result.getColumnIndexOrThrow("ID_STATUS_INT"));
        alunos.INSTRUMENTO_STR = result.getString(result.getColumnIndexOrThrow("INSTRUMENTO_STR"));
        alunos.METODO_STR = result.getString(result.getColumnIndexOrThrow("METODO_STR"));
        alunos.FONE_STR = result.getString(result.getColumnIndexOrThrow("FONE_STR"));

        String Data = result.getString(result.getColumnIndexOrThrow("DATA_NASCIMENTO_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_NASCIMENTO_DTI = date;
        } catch (Exception e) {

        }

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

        Data = result.getString(result.getColumnIndexOrThrow("DATA_IMPORTACAO_DTI"));
        try {
            Date date = sdf.parse(Data);
            alunos.DATA_IMPORTACAO_DTI = date;
        } catch (Exception e) {

        }

        try {
            alunos.STATUS_STR = result.getString(result.getColumnIndexOrThrow("STATUS_STR"));
        }
        catch (Exception ex){}

        return alunos;
    }

    public String Add() {
        SQLiteDatabase db = CriaConexaDb();
        ID_ALUNO_INT = GetMaxCode();
        ContentValues contentValues = PutDados();

        long Result = db.insertOrThrow("CAD_ALUNOS_TAB", null, contentValues);
        db.close();

        if (Result == -1)
            return  context.getString(R.string.msgAddErro);
        else
            return context.getString(R.string.msgAddOk);
    }

    public Integer GetMaxCode() {
        SQLiteDatabase db = CriaConexaDb();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(MAX(ID_ALUNO_INT), 0) + 1 AS COD FROM CAD_ALUNOS_TAB");

        Cursor result = db.rawQuery(sql.toString(), null);
        result.moveToFirst();
        return result.getInt(result.getColumnIndexOrThrow("COD"));
    }

    public String Update() {
        SQLiteDatabase db = CriaConexaDb();
        ContentValues contentValues = PutDados();

        String[] param = new String[1];
        param[0] = String.valueOf(ID_ALUNO_INT);

        long Result = db.update("CAD_ALUNOS_TAB", contentValues, "ID_ALUNO_INT = ?", param);
        db.close();

        if (Result == -1)
            return "Erro ao atualizar o registro";
            //return context.getString(R.string.msgUpdateErro);
        else
            return "Registro atualizado";
            //return context.getString(R.string.msgUpdateOk);
    }

    private ContentValues PutDados()
    {
        ContentValues contentValues = new ContentValues();
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

        if(DATA_IMPORTACAO_DTI != null) {
            String Data = sdf.format(DATA_IMPORTACAO_DTI);
            contentValues.put("DATA_IMPORTACAO_DTI", Data);
        }
        else
            contentValues.putNull("DATA_IMPORTACAO_DTI");

        return contentValues;
    }

    public String Delete() {
        SQLiteDatabase db = CriaConexaDb();
        String[] param = new String[1];
        param[0] = String.valueOf(ID_ALUNO_INT);

        db.delete("CAD_AULAS_TAB", "ID_ALUNO_INT = ?", param);
        long Result = db.delete("CAD_ALUNOS_TAB", "ID_ALUNO_INT = ?", param);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgDeleteErro);
        else
            return context.getString(R.string.msgDeleteOk);
    }
}
