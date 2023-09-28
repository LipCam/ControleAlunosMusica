package br.com.controlealunosmusica.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.Model.TempAlunos;
import br.com.controlealunosmusica.Model.TempAulas;
import br.com.controlealunosmusica.R;

public class JsonAluno {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void ExportJson(Context context) {
        SQLiteDatabase db = new DadosOpenHelper(context).getWritableDatabase();

        //Temp
        String SQL = "SELECT ID_ALUNO_INT\n" +
                "FROM TEMP_ALUNOS_TAB\n" +
                "WHERE FLAG_BIT = 1";
        Cursor curTemp = db.rawQuery(SQL, null);

        if (curTemp.getCount() == 0) {
            Toast.makeText(context, context.getString(R.string.msgSelecRegExp), Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        curTemp.moveToFirst();
        JSONObject jsDados = new JSONObject();
        JSONArray jsArrayDados = new JSONArray();

        try {
            do {
                int IdAluno = curTemp.getInt(curTemp.getColumnIndexOrThrow("ID_ALUNO_INT"));

                String[] param = new String[1];
                param[0] = String.valueOf(IdAluno);

                //Aluno
                SQL = "SELECT ID_ALUNO_INT, NOME_STR, ID_STATUS_INT, INSTRUMENTO_STR,\n" +
                        "       METODO_STR, FONE_STR,\n" +
                        "       IFNULL(DATA_NASCIMENTO_DTI, '') AS DATA_NASCIMENTO_DTI,\n" +
                        "       IFNULL(DATA_BATISMO_DTI, '') AS DATA_BATISMO_DTI,\n" +
                        "       IFNULL(DATA_INICIO_GEM_DTI, '') AS DATA_INICIO_GEM_DTI,\n" +
                        "       IFNULL(DATA_OFICIALIZACAO_DTI, '') AS DATA_OFICIALIZACAO_DTI,\n" +
                        "       ENDERECO_STR,\n" +
                        "       OBSERVACAO_STR\n" +
                        "  FROM CAD_ALUNOS_TAB\n" +
                        "WHERE ID_ALUNO_INT = ?";

                Cursor curAluno = db.rawQuery(SQL, param);
                curAluno.moveToFirst();

                JSONObject jsDadosAluno = new JSONObject();

                //Aluno
                jsDadosAluno.put("Nome", curAluno.getString(curAluno.getColumnIndexOrThrow("NOME_STR")));
                jsDadosAluno.put("IdStatus", curAluno.getInt(curAluno.getColumnIndexOrThrow("ID_STATUS_INT")));
                jsDadosAluno.put("Instrumento", curAluno.getString(curAluno.getColumnIndexOrThrow("INSTRUMENTO_STR")));
                jsDadosAluno.put("Metodo", curAluno.getString(curAluno.getColumnIndexOrThrow("METODO_STR")));
                jsDadosAluno.put("Fone", curAluno.getString(curAluno.getColumnIndexOrThrow("FONE_STR")));

                try {
                    jsDadosAluno.put("Nascimento", curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_NASCIMENTO_DTI")));
                } catch (Exception ex) {
                    jsDadosAluno.put("Nascimento", "");
                }

                try {
                    jsDadosAluno.put("Batismo", curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_BATISMO_DTI")));
                } catch (Exception ex) {
                    jsDadosAluno.put("Batismo", "");
                }

                try {
                    jsDadosAluno.put("Inicio", curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_INICIO_GEM_DTI")));
                } catch (Exception ex) {
                    jsDadosAluno.put("Inicio", "");
                }

                try {
                    jsDadosAluno.put("Oficializacao", curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_OFICIALIZACAO_DTI")));
                } catch (Exception ex) {
                    jsDadosAluno.put("Oficializacao", "");
                }

                jsDadosAluno.put("Endereco", curAluno.getString(curAluno.getColumnIndexOrThrow("ENDERECO_STR")));
                jsDadosAluno.put("Observacao", curAluno.getString(curAluno.getColumnIndexOrThrow("OBSERVACAO_STR")));

                //Aulas
                SQL = "SELECT * \n" +
                        "FROM CAD_AULAS_TAB \n" +
                        "WHERE ID_ALUNO_INT = ?";

                Cursor curAulas = db.rawQuery(SQL, param);

                JSONArray jsArrayAulas = new JSONArray();
                if (curAulas.getCount() > 0) {
                    curAulas.moveToFirst();

                    do {
                        JSONObject jsAula = new JSONObject();

                        jsAula.put("IdTipo", curAulas.getInt(curAulas.getColumnIndexOrThrow("ID_TIPO_INT")));
                        jsAula.put("Instrutor", curAulas.getString(curAulas.getColumnIndexOrThrow("INSTRUTOR_STR")));

                        try {
                            jsAula.put("Data", curAulas.getString(curAulas.getColumnIndexOrThrow("DATA_DTI")));
                        } catch (Exception ex) {
                            jsAula.put("Data", "");
                        }

                        jsAula.put("Concluido", curAulas.getString(curAulas.getColumnIndexOrThrow("CONCLUIDO_BIT")));
                        jsAula.put("Assunto", curAulas.getString(curAulas.getColumnIndexOrThrow("ASSUNTO_STR")));
                        jsAula.put("Observacao", curAulas.getString(curAulas.getColumnIndexOrThrow("OBSERVACAO_STR")));
                        jsArrayAulas.put(jsAula);
                    }
                    while (curAulas.moveToNext());
                }
                jsDadosAluno.put("Aulas", jsArrayAulas);

                jsArrayDados.put(jsDadosAluno);
            }
            while (curTemp.moveToNext());

            jsDados.put("Dados", jsArrayDados);

        } catch (JSONException ex) {
            String message = ex.getMessage();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        db.close();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "//";
        //String NomeArq = "Dados " + curAluno.getString(curAluno.getColumnIndexOrThrow("NOME_STR")) + " " + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".json";
        String NomeArq = "DadosAlunos_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".cal";

        try {
            File file = new File(path + NomeArq);
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            bufferedWriter.write(jsDados.toString());
            bufferedWriter.close();

            Toast.makeText(context, context.getString(R.string.msgArqSalvoDownload).replace("@arq", NomeArq), Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            String message = ex.getMessage();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public String LerJson(Context context, String Arquivo) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), Arquivo);
            StringBuilder output = new StringBuilder();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            try {
                try {

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        output.append(line);
                    }

                    new TempAlunos(context).LimpaTempAlunos();
                    new TempAulas(context).LimpaTempAulas();

                    JSONObject jsDados = new JSONObject(output.toString());
                    JSONArray jsDadosArray = new JSONArray(jsDados.getString("Dados"));
                    for (int a = 0; a < jsDadosArray.length(); a++) {
                        JSONObject jsAluno = jsDadosArray.getJSONObject(a);

                        TempAlunos tempAlunos = new TempAlunos(context);
                        tempAlunos.ID_TEMP_INT = tempAlunos.GetMaxCode();
                        tempAlunos.NOME_STR = jsAluno.getString("Nome");
                        tempAlunos.ID_STATUS_INT = Integer.parseInt(jsAluno.getString("IdStatus"));
                        tempAlunos.INSTRUMENTO_STR = jsAluno.getString("Instrumento");
                        tempAlunos.METODO_STR = jsAluno.getString("Metodo");
                        tempAlunos.FONE_STR = jsAluno.getString("Fone");

                        String Data = jsAluno.getString("Nascimento");
                        try {
                            Date date = sdf.parse(Data);
                            tempAlunos.DATA_NASCIMENTO_DTI = date;
                        } catch (Exception e) {

                        }

                        Data = jsAluno.getString("Batismo");
                        try {
                            Date date = sdf.parse(Data);
                            tempAlunos.DATA_BATISMO_DTI = date;
                        } catch (Exception e) {

                        }

                        Data = jsAluno.getString("Inicio");
                        try {
                            Date date = sdf.parse(Data);
                            tempAlunos.DATA_INICIO_GEM_DTI = date;
                        } catch (Exception e) {

                        }

                        Data = jsAluno.getString("Oficializacao");
                        try {
                            Date date = sdf.parse(Data);
                            tempAlunos.DATA_OFICIALIZACAO_DTI = date;
                        } catch (Exception e) {

                        }

                        tempAlunos.ENDERECO_STR = jsAluno.getString("Endereco");
                        tempAlunos.OBSERVACAO_STR = jsAluno.getString("Observacao");
                        tempAlunos.FLAG_BIT = false;
                        tempAlunos.Add();

                        JSONArray jsAlunoArray = new JSONArray(jsAluno.getString("Aulas"));
                        for (int b = 0; b < jsAlunoArray.length(); b++) {
                            JSONObject jsAula = jsAlunoArray.getJSONObject(b);

                            TempAulas tempAulas = new TempAulas(context);
                            tempAulas.ID_TEMP_INT = tempAlunos.ID_TEMP_INT;
                            tempAulas.ID_ITEM_INT = tempAulas.GetMaxCode();
                            tempAulas.ID_TIPO_INT = Integer.parseInt(jsAula.getString("IdTipo"));
                            tempAulas.INSTRUTOR_STR = jsAula.getString("Instrutor");

                            Data = jsAula.getString("Data");
                            try {
                                Date date = sdf.parse(Data);
                                tempAulas.DATA_DTI = date;
                            } catch (Exception e) {

                            }

                            tempAulas.CONCLUIDO_BIT = jsAula.getString("Concluido").equals("1");
                            tempAulas.ASSUNTO_STR = jsAula.getString("Assunto");
                            tempAulas.OBSERVACAO_STR = jsAula.getString("Observacao");
                            tempAulas.FLAG_BIT = true;
                            tempAulas.Add();
                        }
                    }
                } catch (JSONException ex) {
                    return "Erro: " + ex.getMessage();
                }
            }
            catch (Exception ex)
            {
                return "Erro: " + ex.getMessage();
            }

            bufferedReader.close();
        } catch (IOException ex) {
            return "Erro: " + ex.getMessage();
        }

        return "";
    }

    public void ImportarDados(Context context, int IdTemp, int IdAluno, boolean SobreescDados, boolean ImpAulas) {
        TempAlunos tempAlunos = new TempAlunos(context);
        tempAlunos.ID_TEMP_INT = IdTemp;
        tempAlunos = tempAlunos.Find();

        Alunos alunos = new Alunos(context);

        if (IdAluno == 0) {
            alunos.ID_ALUNO_INT = alunos.GetMaxCode();
            alunos.DATA_IMPORTACAO_DTI = new Date();
        } else {
            alunos.ID_ALUNO_INT = IdAluno;
            alunos = alunos.Find();
        }

        alunos.NOME_STR = tempAlunos.NOME_STR;
        alunos.ID_STATUS_INT = tempAlunos.ID_STATUS_INT;
        alunos.INSTRUMENTO_STR = tempAlunos.INSTRUMENTO_STR;
        alunos.METODO_STR = tempAlunos.METODO_STR;
        alunos.FONE_STR = tempAlunos.FONE_STR;
        alunos.DATA_NASCIMENTO_DTI = tempAlunos.DATA_NASCIMENTO_DTI;
        alunos.DATA_BATISMO_DTI = tempAlunos.DATA_BATISMO_DTI;
        alunos.DATA_INICIO_GEM_DTI = tempAlunos.DATA_INICIO_GEM_DTI;
        alunos.DATA_OFICIALIZACAO_DTI = tempAlunos.DATA_OFICIALIZACAO_DTI;
        alunos.ENDERECO_STR = tempAlunos.ENDERECO_STR;
        alunos.OBSERVACAO_STR = tempAlunos.OBSERVACAO_STR;

        if (IdAluno == 0)
            alunos.Add();
        else if (SobreescDados)
            alunos.Update();

        if (ImpAulas) {
            TempAulas tempAulas = new TempAulas(context);
            tempAulas.ID_TEMP_INT = IdTemp;
            List<TempAulas> lstTempAulas = tempAulas.GetByFlagTrue();

            for (int i = 0; i < lstTempAulas.size(); i++) {
                Aulas aulas = new Aulas(context);
                aulas.ID_AULA_INT = aulas.GetMaxCode();
                aulas.ID_ALUNO_INT = alunos.ID_ALUNO_INT;
                aulas.ID_TIPO_INT = lstTempAulas.get(i).ID_TIPO_INT;
                aulas.INSTRUTOR_STR = lstTempAulas.get(i).INSTRUTOR_STR;
                aulas.DATA_DTI = lstTempAulas.get(i).DATA_DTI;
                aulas.CONCLUIDO_BIT = lstTempAulas.get(i).CONCLUIDO_BIT;
                aulas.ASSUNTO_STR = lstTempAulas.get(i).ASSUNTO_STR;
                aulas.OBSERVACAO_STR = lstTempAulas.get(i).OBSERVACAO_STR;
                aulas.DATA_IMPORTACAO_DTI = new Date();
                aulas.Add();
            }
        }

        Toast.makeText(context,R.string.msgImportacaoRealizada, Toast.LENGTH_SHORT).show();
    }
}
