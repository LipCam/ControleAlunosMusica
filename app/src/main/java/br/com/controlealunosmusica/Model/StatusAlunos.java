package br.com.controlealunosmusica.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;

public class StatusAlunos {
    public Integer ID_STATUS_INT;
    public String DESCRICAO_STR;

    Context context;
    private SQLiteDatabase db;
    private DadosOpenHelper dadosOpenHelper;

    public StatusAlunos(Context context) {
        this.context = context;
        dadosOpenHelper = new DadosOpenHelper(context);
        db = dadosOpenHelper.getWritableDatabase();
    }

    public List<StatusAlunos> Get() {
        List<StatusAlunos> lst = new ArrayList<StatusAlunos>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SIS_STATUS_ALUNOS_TAB");

        Cursor result = db.rawQuery(sql.toString(), null);

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                StatusAlunos statusalunos = new StatusAlunos(context);
                statusalunos.ID_STATUS_INT = result.getInt(result.getColumnIndexOrThrow("ID_STATUS_INT"));
                statusalunos.DESCRICAO_STR = result.getString(result.getColumnIndexOrThrow("DESCRICAO_STR"));
                lst.add(statusalunos);
            } while (result.moveToNext());
        }

        return lst;
    }
}
