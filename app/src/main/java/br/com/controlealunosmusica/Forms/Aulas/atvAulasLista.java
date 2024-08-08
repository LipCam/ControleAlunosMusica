package br.com.controlealunosmusica.Forms.Aulas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

import br.com.controlealunosmusica.Adapters.rsvAulasAdapter;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.R;

public class atvAulasLista extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rsvAulas;

    private int DiaIni, MesIni, AnoIni;
    private int DiaFim, MesFim, AnoFim;
    private String DtInicio, DtFim;
    private TextView txtDtInicio;
    private TextView txtDtFim;
    private DecimalFormat formater00 = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_aulas_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fabNovo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(atvAulasLista.this, atvAulasEdit.class), 0);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        txtDtInicio = (TextView) findViewById(R.id.txtDtInicio);
        txtDtFim = (TextView) findViewById(R.id.txtDtFim);

        Calendar calHoje = Calendar.getInstance();

        DiaIni = calHoje.get(Calendar.DAY_OF_MONTH);
        MesIni = calHoje.get(Calendar.MONTH);
        AnoIni = calHoje.get(Calendar.YEAR);

        DtInicio = AnoIni + "-" + formater00.format(MesIni + 1) + "-" + formater00.format(DiaIni);

        txtDtInicio.setText(formater00.format(DiaIni) + "/" + formater00.format(MesIni + 1) + "/" + AnoIni);
        txtDtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtpInicio = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDtInicio.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        DtInicio = year + "-" + formater00.format(month + 1) + "-" + formater00.format(dayOfMonth);
                        CarregaDados();

                        AnoIni = year;
                        MesIni = month;
                        DiaIni = dayOfMonth;
                    }
                }, AnoIni, MesIni, DiaIni);

                dtpInicio.show();
            }
        });

        DiaFim = calHoje.get(Calendar.DAY_OF_MONTH);
        MesFim = calHoje.get(Calendar.MONTH);
        AnoFim = calHoje.get(Calendar.YEAR);

        DtFim = AnoFim + "-" + formater00.format(MesFim + 1) + "-" + formater00.format(DiaFim);

        txtDtFim.setText(formater00.format(DiaFim) + "/" + formater00.format(MesFim + 1) + "/" + AnoFim);
        txtDtFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtpFim = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDtFim.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        DtFim = year + "-" + formater00.format(month + 1) + "-" + formater00.format(dayOfMonth);
                        CarregaDados();

                        AnoFim = year;
                        MesFim = month;
                        DiaFim = dayOfMonth;
                    }
                }, AnoFim, MesFim, DiaFim);

                dtpFim.show();
            }
        });

        rsvAulas = findViewById(R.id.rsvAulas);
        rsvAulas.addItemDecoration(new DividerItemDecoration(rsvAulas.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados();
    }

    private void CarregaDados() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Aulas objAulas = new Aulas(this);
        rsvAulasAdapter adpAulas = new rsvAulasAdapter(this, objAulas.GetByData(DtInicio, DtFim), 0);
        rsvAulas.setAdapter(adpAulas);
        rsvAulas.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        CarregaDados();
    }
}
