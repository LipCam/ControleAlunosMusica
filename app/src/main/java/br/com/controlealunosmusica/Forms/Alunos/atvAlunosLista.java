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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;

import br.com.controlealunosmusica.Adapters.rsvAlunosAdapter;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.R;

public class atvAlunosLista extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rsvAlunos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_alunos_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(atvAlunosLista.this, atvAlunosEdit.class), 0);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        rsvAlunos = findViewById(R.id.rsvTempAlunos);
        rsvAlunos.addItemDecoration(new DividerItemDecoration(rsvAlunos.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados("");
    }

    private void CarregaDados(String Nome) {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Alunos objAlunos = new Alunos(this);
        rsvAlunosAdapter adpAlunos;
        if(Nome.equals(""))
            adpAlunos = new rsvAlunosAdapter(this, objAlunos.Get());
        else
            adpAlunos = new rsvAlunosAdapter(this, objAlunos.GetByNome(Nome));

        rsvAlunos.setAdapter(adpAlunos);
        rsvAlunos.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);

        MenuItem item = menu.findItem(R.id.mnPesquisar);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CarregaDados(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        CarregaDados("");
    }
}
