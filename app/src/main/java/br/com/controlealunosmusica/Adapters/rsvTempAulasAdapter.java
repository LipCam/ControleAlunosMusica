package br.com.controlealunosmusica.Adapters;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.controlealunosmusica.Forms.Aulas.atvAulasEdit;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.Model.TempAulas;
import br.com.controlealunosmusica.R;


public class rsvTempAulasAdapter extends RecyclerView.Adapter<rsvTempAulasAdapter.ViewHolderTestes> {

    private Context context;
    private List<TempAulas> lstTempAulas;

    public rsvTempAulasAdapter(Context context, List<TempAulas> lstTempAulas) {
        this.context = context;
        this.lstTempAulas = lstTempAulas;
    }

    //criando o layout das linhaa conforme template criado
    @Override
    public ViewHolderTestes onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rsv_aulas_temp, viewGroup, false);
        ViewHolderTestes holder = new ViewHolderTestes(view);
        return holder;
    }

    //montando cada linha bindando os campos
    @Override
    public void onBindViewHolder(final ViewHolderTestes holder, final int i) {
        if (lstTempAulas != null && lstTempAulas.size() > 0) {
            TempAulas tempAulas = lstTempAulas.get(i);
            holder.txtrsvData.setText(new SimpleDateFormat("dd/MM/yyyy").format(tempAulas.DATA_DTI));
            holder.txtrsvTipo.setText(tempAulas.TIPO_STR);
            holder.txtrsvConcluido.setText(tempAulas.STATUS_STR);
            holder.txtrsvAssunto.setText(tempAulas.ASSUNTO_STR);
            holder.chbFlag.setChecked(tempAulas.FLAG_BIT);

            holder.rsvLinhaTempAulas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    holder.chbFlag.setChecked(!holder.chbFlag.isChecked());
                    lstTempAulas.get(i).FLAG_BIT = holder.chbFlag.isChecked();
                    new TempAulas(v.getContext()).SelTemp(lstTempAulas.get(i).ID_TEMP_INT, lstTempAulas.get(i).ID_ITEM_INT, holder.chbFlag.isChecked());
                }
            });

            holder.chbFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    lstTempAulas.get(i).FLAG_BIT = holder.chbFlag.isChecked();
                    new TempAulas(v.getContext()).SelTemp(lstTempAulas.get(i).ID_TEMP_INT, lstTempAulas.get(i).ID_ITEM_INT, holder.chbFlag.isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstTempAulas.size();
    }

    public class ViewHolderTestes extends RecyclerView.ViewHolder {
        ConstraintLayout rsvLinhaTempAulas;
        TextView txtrsvData;
        TextView txtrsvTipo;
        TextView txtrsvConcluido;
        TextView txtrsvAssunto;
        CheckBox chbFlag;

        public ViewHolderTestes(View itemView) {
            super(itemView);

            rsvLinhaTempAulas = itemView.findViewById(R.id.rsvLinhaAulas);
            txtrsvData = itemView.findViewById(R.id.txtrsvData);
            txtrsvTipo = itemView.findViewById(R.id.txtrsvTipo);
            txtrsvConcluido = itemView.findViewById(R.id.txtrsvConcluido);
            txtrsvAssunto = itemView.findViewById(R.id.txtrsvAssunto);
            chbFlag = itemView.findViewById(R.id.chbFlag);
        }
    }
}
