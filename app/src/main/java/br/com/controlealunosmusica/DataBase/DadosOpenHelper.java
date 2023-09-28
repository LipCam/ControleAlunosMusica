package br.com.controlealunosmusica.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DadosOpenHelper extends SQLiteOpenHelper {

    public DadosOpenHelper(Context context) {
        super(context, "CONTROLE_ALUNOS", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CriarTabelas(db);
        InserirDados(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //CriarTabelas(db);
    }

    private void CriarTabelas(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS SIS_STATUS_ALUNOS_TAB (" +
                "ID_STATUS_INT INT, " +
                "DESCRICAO_STR NVARCHAR(150))");

        db.execSQL("CREATE TABLE IF NOT EXISTS CAD_ALUNOS_TAB (" +
                "ID_ALUNO_INT INT, " +
                "NOME_STR NVARCHAR(150), " +
                "ID_STATUS_INT INT, " +
                "INSTRUMENTO_STR NVARCHAR(150), " +
                "METODO_STR NVARCHAR(150), " +
                "FONE_STR NVARCHAR(15), " +
                "DATA_NASCIMENTO_DTI DATETIME, " +
                "DATA_BATISMO_DTI DATETIME, " +
                "DATA_INICIO_GEM_DTI DATETIME, " +
                "DATA_OFICIALIZACAO_DTI DATETIME, " +
                "ENDERECO_STR NVARCHAR(255), " +
                "OBSERVACAO_STR NVARCHAR(255)," +
                "DATA_IMPORTACAO_DTI DATETIME, " +
                "FOREIGN KEY(ID_STATUS_INT) REFERENCES SIS_STATUS_ALUNOS_TAB(ID_STATUS_INT))");

        db.execSQL("CREATE TABLE IF NOT EXISTS SIS_TIPOS_AULA_TAB (" +
                "ID_TIPO_INT INT, " +
                "DESCRICAO_STR NVARCHAR(150))");

        db.execSQL("CREATE TABLE IF NOT EXISTS CAD_AULAS_TAB (" +
                "ID_AULA_INT INT, " +
                "ID_ALUNO_INT INT, " +
                "ID_TIPO_INT INT, " +
                "INSTRUTOR_STR NVARCHAR(150), " +
                "DATA_DTI DATETIME, " +
                "CONCLUIDO_BIT BIT, " +
                "ASSUNTO_STR NVARCHAR(255)," +
                "OBSERVACAO_STR NVARCHAR(255)," +
                "DATA_IMPORTACAO_DTI DATETIME, " +
                "FOREIGN KEY(ID_ALUNO_INT) REFERENCES CAD_ALUNOS_TAB(ID_ALUNO_INT))");


        db.execSQL("CREATE TABLE IF NOT EXISTS TEMP_ALUNOS_TAB (" +
                "ID_TEMP_INT INT, " +
                "ID_ALUNO_INT INT, " +
                "NOME_STR NVARCHAR(150), " +
                "ID_STATUS_INT INT, " +
                "INSTRUMENTO_STR NVARCHAR(150), " +
                "METODO_STR NVARCHAR(150), " +
                "FONE_STR NVARCHAR(15), " +
                "DATA_NASCIMENTO_DTI DATETIME, " +
                "DATA_BATISMO_DTI DATETIME, " +
                "DATA_INICIO_GEM_DTI DATETIME, " +
                "DATA_OFICIALIZACAO_DTI DATETIME, " +
                "ENDERECO_STR NVARCHAR(255), " +
                "OBSERVACAO_STR NVARCHAR(255)," +
                "FLAG_BIT BIT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS TEMP_AULAS_TAB (" +
                "ID_TEMP_INT INT, " +
                "ID_ITEM_INT INT, " +
                "ID_TIPO_INT INT, " +
                "INSTRUTOR_STR NVARCHAR(150), " +
                "DATA_DTI DATETIME, " +
                "CONCLUIDO_BIT BIT, " +
                "ASSUNTO_STR NVARCHAR(255)," +
                "OBSERVACAO_STR NVARCHAR(255)," +
                "FLAG_BIT BIT)");
    }

    private void InserirDados(SQLiteDatabase db){
        db.execSQL("DELETE FROM SIS_STATUS_ALUNOS_TAB");
        db.execSQL("INSERT INTO SIS_STATUS_ALUNOS_TAB (ID_STATUS_INT, DESCRICAO_STR) VALUES (1,'Aprendiz')");
        db.execSQL("INSERT INTO SIS_STATUS_ALUNOS_TAB (ID_STATUS_INT, DESCRICAO_STR) VALUES (2,'Ensaio musical')");
        db.execSQL("INSERT INTO SIS_STATUS_ALUNOS_TAB (ID_STATUS_INT, DESCRICAO_STR) VALUES (3,'Culto de jovens')");
        db.execSQL("INSERT INTO SIS_STATUS_ALUNOS_TAB (ID_STATUS_INT, DESCRICAO_STR) VALUES (4,'Culto oficial')");
        db.execSQL("INSERT INTO SIS_STATUS_ALUNOS_TAB (ID_STATUS_INT, DESCRICAO_STR) VALUES (5,'Oficializado(a)')");

        db.execSQL("DELETE FROM SIS_TIPOS_AULA_TAB");
        db.execSQL("INSERT INTO SIS_TIPOS_AULA_TAB (ID_TIPO_INT, DESCRICAO_STR) VALUES (1,'Teoria')");
        db.execSQL("INSERT INTO SIS_TIPOS_AULA_TAB (ID_TIPO_INT, DESCRICAO_STR) VALUES (2,'Método')");
        db.execSQL("INSERT INTO SIS_TIPOS_AULA_TAB (ID_TIPO_INT, DESCRICAO_STR) VALUES (3,'Hino')");
        db.execSQL("INSERT INTO SIS_TIPOS_AULA_TAB (ID_TIPO_INT, DESCRICAO_STR) VALUES (4,'Outros')");
    }
}
