package br.com.controlealunosmusica.Forms.Alunos;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import br.com.controlealunosmusica.Adapters.rsvAulasAdapter;
import br.com.controlealunosmusica.Forms.Aulas.atvAulasEdit;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.R;

public class atvAlunosAulasLista extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rsvAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_alunos_aulas_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.lblAulas) + " - " + getIntent().getExtras().getString("ALUNO_STR"));
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fabNovo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(atvAlunosAulasLista.this, atvAulasEdit.class);
                it.putExtra("ID_ALUNO_INT", getIntent().getExtras().getInt("ID_ALUNO_INT"));
                startActivityForResult(it, 0);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        rsvAulas = findViewById(R.id.rsvAulas);
        rsvAulas.addItemDecoration(new DividerItemDecoration(rsvAulas.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados();
    }

    private void CarregaDados() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Aulas objAulas = new Aulas(this);
        objAulas.ID_ALUNO_INT = getIntent().getExtras().getInt("ID_ALUNO_INT");
        rsvAulasAdapter adpAulas = new rsvAulasAdapter(this, objAulas.GetByAluno(), 1);
        rsvAulas.setAdapter(adpAulas);
        rsvAulas.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CarregaDados();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        finish();
        return true;

        //return super.onOptionsItemSelected(item);
    }
}
