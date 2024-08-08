package br.com.controlealunosmusica.Forms.ImpDados;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import br.com.controlealunosmusica.Adapters.rsvTempAlunosAdapter;
import br.com.controlealunosmusica.Classes.GlobalClass;
import br.com.controlealunosmusica.Model.TempAlunos;
import br.com.controlealunosmusica.R;

public class atvImpDadosLista extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rsvTempAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_imp_dados_lista);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressbar);

        rsvTempAlunos = findViewById(R.id.rsvTempAlunos);
        rsvTempAlunos.addItemDecoration(new DividerItemDecoration(rsvTempAlunos.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CarregaDados();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ajuda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnAjuda) {
//            Intent it = new Intent(atvExpDados.this, atvExpDadosAjuda.class);
//            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CarregaDados() {
        GlobalClass globalclass = (GlobalClass) this.getApplicationContext();
        globalclass.IntentTempAlunos = 1;

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        TempAlunos objTempAlunos = new TempAlunos(this);
        rsvTempAlunosAdapter adpTempAlunos = new rsvTempAlunosAdapter(this, objTempAlunos.Get(false));

        rsvTempAlunos.setAdapter(adpTempAlunos);
        rsvTempAlunos.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
