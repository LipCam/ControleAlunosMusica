package br.com.controlealunosmusica.Forms.ImpDados;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.controlealunosmusica.Adapters.spnAlunosAdapter;
import br.com.controlealunosmusica.Classes.JsonAluno;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.R;

public class atvImpDadosOpcoes extends AppCompatActivity {

    RadioGroup rgrOpcoes;
    RadioButton rbtNovo;
    RadioButton rbtExistente;
    LinearLayout llAluno;
    Spinner spnAlunos;
    CheckBox chbSobreDados;
    CheckBox chbAulas;
    CardView btncvImportar;

    List<Alunos> lstAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_imp_dados_opcoes);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rgrOpcoes = findViewById(R.id.rgrOpcoes);
        rbtNovo = findViewById(R.id.rbtNovo);
        rbtExistente = findViewById(R.id.rbtExistente);
        llAluno = findViewById(R.id.llAluno);
        spnAlunos = findViewById(R.id.spnAlunos);
        chbSobreDados = findViewById(R.id.chbSobreDados);
        chbAulas = findViewById(R.id.chbAulas);
        btncvImportar = findViewById(R.id.btncvImportar);

        llAluno.setVisibility(View.GONE);

        rbtNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAluno.setVisibility(View.GONE);
            }
        });

        rbtExistente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAluno.setVisibility(View.VISIBLE);
            }
        });

        btncvImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(v.getContext().getString(R.string.msgConfirmImport));
                builder.setPositiveButton(v.getContext().getString(R.string.lblSim), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       ImportarDados();
                    }
                });

                builder.setNegativeButton(v.getContext().getString(R.string.lblNao), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                builder.show();
            }
        });

        CarregaSpinners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        finish();
        return true;

        //return super.onOptionsItemSelected(item);
    }

    private void CarregaSpinners() {
        lstAlunos = new Alunos(this).GetComboAlunos();
        spnAlunos.setAdapter(new spnAlunosAdapter(this, lstAlunos));
    }

    private void ImportarDados() {
        int IdTemp = getIntent().getExtras().getInt("ID_TEMP_INT");
        int IdAluno = 0;
        boolean SobreDados = true;
        boolean ImpAulas = true;

        if (rbtExistente.isChecked()) {
            try {
                IdAluno = ((Alunos) spnAlunos.getSelectedItem()).ID_ALUNO_INT;
            } catch (Exception ex) {
            }

            if (IdAluno == 0) {
                Toast.makeText(this, R.string.msgSelecioneAluno, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!chbSobreDados.isChecked() && !chbAulas.isChecked()) {
                Toast.makeText(this, R.string.msgSelecioneOpImport, Toast.LENGTH_SHORT).show();
                return;
            }

            SobreDados = chbSobreDados.isChecked();
            ImpAulas = chbAulas.isChecked();
        }

        new JsonAluno().ImportarDados(this, IdTemp, IdAluno, SobreDados, ImpAulas);

        startActivity(new Intent(atvImpDadosOpcoes.this, atvImpDadosLista.class));
    }
}
