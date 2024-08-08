package br.com.controlealunosmusica.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;

public class TiposAula {
    public Integer ID_TIPO_INT;    
    public String DESCRICAO_STR;

    Context context;
    private SQLiteDatabase db;
    private DadosOpenHelper dadosOpenHelper;

    public TiposAula(Context context) {
        if(context != null) {
            this.context = context;
            dadosOpenHelper = new DadosOpenHelper(context);
            db = dadosOpenHelper.getWritableDatabase();
        }
    }

    public List<TiposAula> GetCombo() {
        List<TiposAula> lst = new ArrayList<TiposAula>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM SIS_TIPOS_AULA_TAB");

        Cursor result = db.rawQuery(sql.toString(), null);

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {
                TiposAula tiposaula = new TiposAula(null);
                tiposaula.ID_TIPO_INT = result.getInt(result.getColumnIndexOrThrow("ID_TIPO_INT"));
                tiposaula.DESCRICAO_STR = result.getString(result.getColumnIndexOrThrow("DESCRICAO_STR"));
                lst.add(tiposaula);
            } while (result.moveToNext());
        }

        return lst;
    }
}
