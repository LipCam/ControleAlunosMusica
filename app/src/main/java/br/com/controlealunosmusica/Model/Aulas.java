package br.com.controlealunosmusica.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;
import br.com.controlealunosmusica.R;

public class Aulas {
    public int ID_AULA_INT = 0;
    public int ID_ALUNO_INT;
    public int ID_TIPO_INT = 0;
    public String INSTRUTOR_STR;
    public Date DATA_DTI;
    public boolean CONCLUIDO_BIT = false;
    public String ASSUNTO_STR;
    public String OBSERVACAO_STR;
    public Date DATA_IMPORTACAO_DTI;

    public String ALUNO_STR;
    public String TIPO_STR;
    public String STATUS_STR;

    Context context;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Aulas(Context context) {
        this.context = context;
    }

    private SQLiteDatabase CriaConexaDb() {
        return new DadosOpenHelper(context).getWritableDatabase();
    }

    public List<Aulas> GetByData(String DtInicio, String DtFim) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, AL.NOME_STR AS ALUNO_STR, TP.DESCRICAO_STR AS TIPO_STR, " +
                "CASE WHEN A.CONCLUIDO_BIT = 0 THEN 'Pendente' ELSE 'Concluído' END AS STATUS_STR " +
                "FROM CAD_AULAS_TAB A " +
                "INNER JOIN CAD_ALUNOS_TAB AL ON AL.ID_ALUNO_INT = A.ID_ALUNO_INT " +
                "INNER JOIN SIS_TIPOS_AULA_TAB TP ON TP.ID_TIPO_INT = A.ID_TIPO_INT " +
                "WHERE DATE(DATA_DTI) BETWEEN ? AND ? " +
                "ORDER BY DATA_DTI");

        String[] param = new String[2];
        param[0] = String.valueOf(DtInicio);
        param[1] = String.valueOf(DtFim);

        return GetList(sql, param);
    }

    public List<Aulas> GetByAluno() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT A.*, AL.NOME_STR AS ALUNO_STR, TP.DESCRICAO_STR AS TIPO_STR, " +
                "CASE WHEN A.CONCLUIDO_BIT = 0 THEN 'Pendente' ELSE 'Concluído' END AS STATUS_STR " +
                "FROM CAD_AULAS_TAB A " +
                "INNER JOIN CAD_ALUNOS_TAB AL ON AL.ID_ALUNO_INT = A.ID_ALUNO_INT " +
                "INNER JOIN SIS_TIPOS_AULA_TAB TP ON TP.ID_TIPO_INT = A.ID_TIPO_INT " +
                "WHERE AL.ID_ALUNO_INT = ? " +
                "ORDER BY DATA_DTI DESC");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_ALUNO_INT);

        return GetList(sql, param);
    }

    private List<Aulas> GetList(StringBuilder sql, String[] param) {
        SQLiteDatabase db = CriaConexaDb();
        Cursor result = db.rawQuery(sql.toString(), param);

        List<Aulas> lst = new ArrayList<Aulas>();

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                Aulas aulas = Carrega(result);
                lst.add(aulas);
            } while (result.moveToNext());
        }
        db.close();

        return lst;
    }

    public Aulas Find() {
        SQLiteDatabase db = CriaConexaDb();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM CAD_AULAS_TAB WHERE ID_AULA_INT = ?");

        String[] param = new String[1];
        param[0] = String.valueOf(ID_AULA_INT);

        Cursor result = db.rawQuery(sql.toString(), param);
        Aulas aulas = new Aulas(context);

        if (result.getCount() > 0) {
            result.moveToFirst();
            aulas = Carrega(result);
        }
        db.close();

        return aulas;
    }

    private Aulas Carrega(Cursor result) {
        Aulas aulas = new Aulas(context);
        aulas.ID_AULA_INT = result.getInt(result.getColumnIndexOrThrow("ID_AULA_INT"));
        aulas.ID_ALUNO_INT = result.getInt(result.getColumnIndexOrThrow("ID_ALUNO_INT"));
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

        Data = result.getString(result.getColumnIndexOrThrow("DATA_IMPORTACAO_DTI"));
        try {
            Date date = sdf.parse(Data);
            aulas.DATA_IMPORTACAO_DTI = date;
        } catch (Exception e) {

        }

        try {
            aulas.ALUNO_STR = result.getString(result.getColumnIndexOrThrow("ALUNO_STR"));
            aulas.TIPO_STR = result.getString(result.getColumnIndexOrThrow("TIPO_STR"));
            aulas.STATUS_STR = result.getString(result.getColumnIndexOrThrow("STATUS_STR"));
        } catch (Exception ex) {

        }

        return aulas;
    }

    public String Add() {
        SQLiteDatabase db = CriaConexaDb();
        ID_AULA_INT = GetMaxCode();
        ContentValues contentValues = PutDados();

        long Result = db.insertOrThrow("CAD_AULAS_TAB", null, contentValues);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgAddErro);
        else
            return context.getString(R.string.msgAddOk);
    }

    public Integer GetMaxCode() {
        SQLiteDatabase db = CriaConexaDb();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(MAX(ID_AULA_INT), 0) + 1 AS COD FROM CAD_AULAS_TAB");

        Cursor result = db.rawQuery(sql.toString(), null);
        result.moveToFirst();
        return result.getInt(result.getColumnIndexOrThrow("COD"));
    }

    public String Update() {
        SQLiteDatabase db = CriaConexaDb();
        ContentValues contentValues = PutDados();

        String[] param = new String[1];
        param[0] = String.valueOf(ID_AULA_INT);

        long Result = db.update("CAD_AULAS_TAB", contentValues, "ID_AULA_INT = ?", param);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgUpdateErro);
        else
            return context.getString(R.string.msgUpdateOk);
    }

    private ContentValues PutDados() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_AULA_INT", ID_AULA_INT);
        contentValues.put("ID_ALUNO_INT", ID_ALUNO_INT);
        contentValues.put("ID_TIPO_INT", ID_TIPO_INT);
        contentValues.put("INSTRUTOR_STR", INSTRUTOR_STR);

        if (DATA_DTI != null) {
            String Data = sdf.format(DATA_DTI);
            contentValues.put("DATA_DTI", Data);
        } else
            contentValues.putNull("DATA_DTI");

        contentValues.put("CONCLUIDO_BIT", CONCLUIDO_BIT);
        contentValues.put("ASSUNTO_STR", ASSUNTO_STR);
        contentValues.put("OBSERVACAO_STR", OBSERVACAO_STR);

        if (DATA_IMPORTACAO_DTI != null) {
            String Data = sdf.format(DATA_IMPORTACAO_DTI);
            contentValues.put("DATA_IMPORTACAO_DTI", Data);
        } else
            contentValues.putNull("DATA_IMPORTACAO_DTI");

        return contentValues;
    }

    public String Delete() {
        SQLiteDatabase db = CriaConexaDb();
        String[] param = new String[1];
        param[0] = String.valueOf(ID_AULA_INT);

        long Result = db.delete("CAD_AULAS_TAB", "ID_AULA_INT = ?", param);
        db.close();

        if (Result == -1)
            return context.getString(R.string.msgDeleteErro);
        else
            return context.getString(R.string.msgDeleteOk);
    }

    public void CopiarAulas() {
        SQLiteDatabase db = CriaConexaDb();

        //Temp
        String SQL = "SELECT ID_ALUNO_INT\n" +
                "FROM TEMP_ALUNOS_TAB\n" +
                "WHERE FLAG_BIT = 1";
        Cursor curTemp = db.rawQuery(SQL, null);

        if (curTemp.getCount() == 0) {
            Toast.makeText(context, context.getString(R.string.msgSelecAlunosCopia), Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        curTemp.moveToFirst();
        do {
            int IdAluno = curTemp.getInt(curTemp.getColumnIndexOrThrow("ID_ALUNO_INT"));

            SQL = "SELECT * \n" +
                    "FROM CAD_AULAS_TAB \n" +
                    "WHERE ID_AULA_INT = ?";

            String[] param = new String[1];
            param[0] = String.valueOf(ID_AULA_INT);
            Cursor curAulas = db.rawQuery(SQL, param);

            if (curAulas.getCount() > 0) {
                curAulas.moveToFirst();

                do {
                    Aulas objAulas = new Aulas(context);
                    objAulas.ID_ALUNO_INT = IdAluno;
                    objAulas.ID_TIPO_INT = curAulas.getInt(curAulas.getColumnIndexOrThrow("ID_TIPO_INT"));
                    objAulas.INSTRUTOR_STR = curAulas.getString(curAulas.getColumnIndexOrThrow("INSTRUTOR_STR"));

                    String Data = curAulas.getString(curAulas.getColumnIndexOrThrow("DATA_DTI"));
                    try {
                        Date date = sdf.parse(Data);
                        objAulas.DATA_DTI = date;
                    } catch (Exception e) {

                    }

                    objAulas.CONCLUIDO_BIT = curAulas.getString(curAulas.getColumnIndexOrThrow("CONCLUIDO_BIT")).equals("1");
                    objAulas.ASSUNTO_STR = curAulas.getString(curAulas.getColumnIndexOrThrow("ASSUNTO_STR"));
                    objAulas.OBSERVACAO_STR = curAulas.getString(curAulas.getColumnIndexOrThrow("OBSERVACAO_STR"));
                    objAulas.Add();
                }
                while (curAulas.moveToNext());
            }
        }
        while (curTemp.moveToNext());

        db.close();

        Toast.makeText(context, context.getString(R.string.msgCopiaRealizada), Toast.LENGTH_LONG).show();
    }
}
