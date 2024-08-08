package br.com.controlealunosmusica.Forms.ImpDados;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import br.com.controlealunosmusica.Adapters.rsvTempAulasAdapter;
import br.com.controlealunosmusica.Model.TempAulas;
import br.com.controlealunosmusica.R;

public class frgImpDadosAulas extends Fragment {

    private RecyclerView rsvAulas;

    public frgImpDadosAulas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_imp_dados_aulas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rsvAulas = getView().findViewById(R.id.rsvAulas);
        rsvAulas.addItemDecoration(new DividerItemDecoration(rsvAulas.getContext(), DividerItemDecoration.VERTICAL));
        CarregaDados();

        super.onViewCreated(view, savedInstanceState);
    }

    private void CarregaDados() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        TempAulas objTempAulas = new TempAulas(getActivity());
        objTempAulas.ID_TEMP_INT = getActivity().getIntent().getIntExtra("ID_TEMP_INT", 0);

        rsvTempAulasAdapter adpAulas = new rsvTempAulasAdapter(getActivity(), objTempAulas.GetById(true));
        rsvAulas.setAdapter(adpAulas);
        rsvAulas.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
