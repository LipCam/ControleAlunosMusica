package br.com.controlealunosmusica.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.controlealunosmusica.Model.TiposAula;
import br.com.controlealunosmusica.R;

public class spnTipoAulaAdapter extends BaseAdapter implements SpinnerAdapter {

    private List<TiposAula> lstTiposAtividades;
    Context context;

    public spnTipoAulaAdapter(Context context, List<TiposAula> lstTiposAtividades) {
        this.lstTiposAtividades = lstTiposAtividades;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstTiposAtividades.size();
    }

    @Override
    public Object getItem(int position) {
        return lstTiposAtividades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //É necessário criar um layout para o spinner que é executado aqui
        View view =  View.inflate(context, R.layout.spn_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.txvSpiner);
        textView.setText(lstTiposAtividades.get(position).DESCRICAO_STR);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //É necessário criar um layout para a lista de dados que é executado aqui
        View view =  View.inflate(context, R.layout.spn_lst_layout, null);
        final TextView textView = (TextView) view.findViewById(R.id.txvItemSpinner);
        textView.setText(lstTiposAtividades.get(position).DESCRICAO_STR);
        return view;
    }
}