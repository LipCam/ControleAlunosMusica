package br.com.controlealunosmusica.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.controlealunosmusica.Classes.GlobalClass;
import br.com.controlealunosmusica.Forms.ImpDados.atvImpDadosDet;
import br.com.controlealunosmusica.Model.TempAlunos;
import br.com.controlealunosmusica.R;


public class rsvTempAlunosAdapter extends RecyclerView.Adapter<rsvTempAlunosAdapter.ViewHolderTestes> {

    private Context context;
    private List<TempAlunos> lstTempAlunos;

    public rsvTempAlunosAdapter(Context context, List<TempAlunos> lstTempAlunos) {
        this.context = context;
        this.lstTempAlunos = lstTempAlunos;
    }

    //criando o layout das linhaa conforme template criado
    @Override
    public ViewHolderTestes onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rsv_alunos_temp, viewGroup  , false);
        ViewHolderTestes holder = new  ViewHolderTestes(view);
        return holder;
    }

    //montando cada linha bindando os campos
    @Override
    public void onBindViewHolder(final ViewHolderTestes holder, final int i) {
        if (lstTempAlunos != null && lstTempAlunos.size() > 0) {
            TempAlunos alunos = lstTempAlunos.get(i);
            holder.txtrsvNome.setText(alunos.NOME_STR);
            holder.chbFlag.setChecked(alunos.FLAG_BIT);
            holder.txtrsvInstrumento.setText(alunos.INSTRUMENTO_STR);

            GlobalClass globalclass = (GlobalClass) context.getApplicationContext();
            if (globalclass.IntentTempAlunos == 0) {
                holder.rsvLinhaTempAlunos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        holder.chbFlag.setChecked(!holder.chbFlag.isChecked());
                        new TempAlunos(v.getContext()).SelTemp(lstTempAlunos.get(i).ID_TEMP_INT, holder.chbFlag.isChecked());
                    }
                });

                holder.chbFlag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new TempAlunos(v.getContext()).SelTemp(lstTempAlunos.get(i).ID_TEMP_INT, holder.chbFlag.isChecked());
                    }
                });
            }
            else {
                holder.chbFlag.setVisibility(View.INVISIBLE);

                holder.rsvLinhaTempAlunos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Intent it = new Intent(v.getContext(), atvImpDadosDet.class);
                        it.putExtra("ID_TEMP_INT", lstTempAlunos.get(i).ID_TEMP_INT);
                        ((AppCompatActivity) context).startActivityForResult(it, 0);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return lstTempAlunos.size();
    }

    public class ViewHolderTestes extends RecyclerView.ViewHolder {
        ConstraintLayout rsvLinhaTempAlunos;
        TextView txtrsvNome;
        CheckBox chbFlag;
        TextView txtrsvInstrumento;

        public ViewHolderTestes(View itemView) {
            super(itemView);

            rsvLinhaTempAlunos = itemView.findViewById(R.id.rsvLinhaTempAlunos);
            txtrsvNome = itemView.findViewById(R.id.txtrsvNome);
            chbFlag = itemView.findViewById(R.id.chbFlag);
            txtrsvInstrumento = itemView.findViewById(R.id.txtrsvInstrumento);
        }
    }
}
