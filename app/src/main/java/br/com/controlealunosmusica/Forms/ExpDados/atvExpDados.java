package br.com.controlealunosmusica.Forms.ExpDados;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import br.com.controlealunosmusica.Classes.JsonAluno;
import br.com.controlealunosmusica.Model.TempAlunos;
import br.com.controlealunosmusica.R;

public class atvExpDados extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rsvTempAlunos;
    boolean SelTodos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_exp_dados);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 1000);
            }
        }

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        rsvTempAlunos = findViewById(R.id.rsvTempAlunos);
        rsvTempAlunos.addItemDecoration(new DividerItemDecoration(rsvTempAlunos.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exp_imp_dados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnOk) {
            new JsonAluno().ExportJson(this);
            return true;
        } else if (id == R.id.mnSelTodos) {
            SelTodos = !SelTodos;

            if(SelTodos)
                item.setTitle(this.getString(R.string.lblDeselTodos));
            else
                item.setTitle(this.getString(R.string.lblSelTodos));

            CarregaDados();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CarregaDados() {
        GlobalClass globalclass = (GlobalClass) this.getApplicationContext();
        globalclass.IntentTempAlunos = 0;

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        TempAlunos objTempAlunos = new TempAlunos(this);
        rsvTempAlunosAdapter adpTempAlunos = new rsvTempAlunosAdapter(this, objTempAlunos.Get(SelTodos));

        rsvTempAlunos.setAdapter(adpTempAlunos);
        rsvTempAlunos.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
