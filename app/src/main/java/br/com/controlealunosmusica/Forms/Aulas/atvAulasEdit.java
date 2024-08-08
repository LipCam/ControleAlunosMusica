package br.com.controlealunosmusica.Forms.Aulas;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.controlealunosmusica.Adapters.spnAlunosAdapter;
import br.com.controlealunosmusica.Adapters.spnTipoAulaAdapter;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.Model.TiposAula;
import br.com.controlealunosmusica.R;

public class atvAulasEdit extends AppCompatActivity {

    private int Dia, Mes, Ano;
    Spinner spnAlunos;
    TextView txtData;
    CheckBox chbConcluido;
    Spinner spnTipos;
    EditText txtInstrutor;
    EditText txtAssunto;
    EditText txtObsevacao;
    TextView txvImportEm;
    TextView txvDataImport;

    MenuItem mnCopiar;
    MenuItem mnExcluir;

    Aulas objAulas;

    List<Alunos> lstAlunos;
    List<TiposAula> lstTiposAula;

    private DecimalFormat formater00 = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_aulas_edit);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spnAlunos = findViewById(R.id.spnAlunos);
        txtData = findViewById(R.id.txtData);
        chbConcluido = findViewById(R.id.chbConcluido);
        spnTipos = findViewById(R.id.spnTipos);
        txtInstrutor = findViewById(R.id.txtInstrutor);
        txtAssunto = findViewById(R.id.txtAssunto);
        txtObsevacao = findViewById(R.id.txtObsevacao);
        txvImportEm = findViewById(R.id.txvImportEm);
        txvDataImport = findViewById(R.id.txvDataImport);

        txvImportEm.setVisibility(View.GONE);
        txvDataImport.setVisibility(View.GONE);

        Calendar calHoje = Calendar.getInstance();
        Dia = calHoje.get(Calendar.DAY_OF_MONTH);
        Mes = calHoje.get(Calendar.MONTH);
        Ano = calHoje.get(Calendar.YEAR);
        txtData.setText(formater00.format(calHoje.get(Calendar.DAY_OF_MONTH)) + "/" + formater00.format(calHoje.get(Calendar.MONTH) + 1) + "/" + calHoje.get(Calendar.YEAR));
        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtpInicio = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtData.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        Ano = year;
                        Mes = month;
                        Dia = dayOfMonth;
                    }
                }, Ano, Mes, Dia);

                dtpInicio.show();
            }
        });

        CarregaSpinners();

        objAulas = new Aulas(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if(bundle.containsKey("ID_AULA_INT")) {
                objAulas.ID_AULA_INT = bundle.getInt("ID_AULA_INT");
                objAulas = objAulas.Find();
                Carrega();
            }
            else if(bundle.containsKey("ID_ALUNO_INT")) {
                for (int i = 0; i < lstAlunos.size(); i++) {
                    if (lstAlunos.get(i).ID_ALUNO_INT == bundle.getInt("ID_ALUNO_INT")) {
                        spnAlunos.setSelection(i);
                        break;
                    }
                }
            }
            spnAlunos.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        mnCopiar = menu.findItem(R.id.mnCopiar);
        mnExcluir = menu.findItem(R.id.mnExcluir);

        if (objAulas.ID_AULA_INT == 0) {
            mnCopiar.setVisible(false);
            mnExcluir.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnSalvar) {
            OnSalvar();
            return true;
        } else if (id == R.id.mnCopiar) {
            Intent it = new Intent(atvAulasEdit.this, atvAulasCopiar.class);
            it.putExtra("ID_AULA_INT", objAulas.ID_AULA_INT);
            startActivity(it);
        } else if (id == R.id.mnExcluir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.msgConfirmDelete));

            builder.setPositiveButton(this.getString(R.string.lblSim), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    OnExcluir();
                    finish();
                }
            });

            builder.setNegativeButton(this.getString(R.string.lblNao), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CarregaSpinners() {
        lstAlunos = new Alunos(this).GetComboAlunos();
        spnAlunos.setAdapter(new spnAlunosAdapter(this, lstAlunos));

        lstTiposAula = new TiposAula(this).GetCombo();
        spnTipos.setAdapter(new spnTipoAulaAdapter(this, lstTiposAula));
    }

    private void Carrega() {
        for (int i = 0; i < lstAlunos.size(); i++) {
            if (lstAlunos.get(i).ID_ALUNO_INT == objAulas.ID_ALUNO_INT) {
                spnAlunos.setSelection(i);
                break;
            }
        }

        txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAulas.DATA_DTI));
        chbConcluido.setChecked(objAulas.CONCLUIDO_BIT);

        for (int i = 0; i < lstTiposAula.size(); i++) {
            if (lstTiposAula.get(i).ID_TIPO_INT == objAulas.ID_TIPO_INT) {
                spnTipos.setSelection(i);
                break;
            }
        }

        txtInstrutor.setText(String.valueOf(objAulas.INSTRUTOR_STR));
        txtAssunto.setText(String.valueOf(objAulas.ASSUNTO_STR));
        txtObsevacao.setText(String.valueOf(objAulas.OBSERVACAO_STR));

        if (objAulas.DATA_IMPORTACAO_DTI != null) {
            txvImportEm.setVisibility(View.VISIBLE);
            txvDataImport.setVisibility(View.VISIBLE);
            txvDataImport.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAulas.DATA_IMPORTACAO_DTI));
        }
    }

    private void OnSalvar() {
        if (((Alunos) spnAlunos.getSelectedItem()).ID_ALUNO_INT == 0) {
            Toast.makeText(this, "Informe um aluno.", Toast.LENGTH_SHORT).show();
            return;
        }

        objAulas.ID_ALUNO_INT = ((Alunos) spnAlunos.getSelectedItem()).ID_ALUNO_INT;

        try {
            objAulas.DATA_DTI = new SimpleDateFormat("dd/MM/yyyy").parse(txtData.getText().toString());
        } catch (Exception ex)
        {
            String a = ex.getMessage();
        }
        objAulas.CONCLUIDO_BIT = chbConcluido.isChecked();
        objAulas.ID_TIPO_INT = ((TiposAula) spnTipos.getSelectedItem()).ID_TIPO_INT;
        objAulas.INSTRUTOR_STR = txtInstrutor.getText().toString();
        objAulas.ASSUNTO_STR = txtAssunto.getText().toString();
        objAulas.OBSERVACAO_STR = txtObsevacao.getText().toString();

        String Message = "";
        if (objAulas.ID_AULA_INT == 0) {
            Message = objAulas.Add();
            mnCopiar.setVisible(true);
            mnExcluir.setVisible(true);
        }
        else
            Message = objAulas.Update();

        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
        //finish();
    }

    private void OnExcluir() {
        Toast.makeText(this, objAulas.Delete(), Toast.LENGTH_SHORT).show();
    }

}
