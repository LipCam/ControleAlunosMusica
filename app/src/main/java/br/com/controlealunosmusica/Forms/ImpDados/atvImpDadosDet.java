package br.com.controlealunosmusica.Forms.ImpDados;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.controlealunosmusica.Adapters.spgImpDadoDetAdapter;
import br.com.controlealunosmusica.R;

public class atvImpDadosDet extends AppCompatActivity {

    boolean SelTodos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_imp_dados_det);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spgImpDadoDetAdapter sectionsPagerAdapter = new spgImpDadoDetAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
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
            Intent it = new Intent(this, atvImpDadosOpcoes.class);
            it.putExtra("ID_TEMP_INT", getIntent().getExtras().getInt("ID_TEMP_INT"));
            startActivityForResult(it, 0);
            return true;
        } else if (id == R.id.mnSelTodos) {
            SelTodos = !SelTodos;

            if(SelTodos)
                item.setTitle(this.getString(R.string.lblDeselTodos));
            else
                item.setTitle(this.getString(R.string.lblSelTodos));

            //CarregaDados();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        spgImpDadoDetAdapter sectionsPagerAdapter = new spgImpDadoDetAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }
}