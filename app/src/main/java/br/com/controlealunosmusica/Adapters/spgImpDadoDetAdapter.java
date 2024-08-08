package br.com.controlealunosmusica.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.com.controlealunosmusica.Forms.ImpDados.frgImpDadosAulas;
import br.com.controlealunosmusica.Forms.ImpDados.frgImpDadosDet;
import br.com.controlealunosmusica.R;

public class spgImpDadoDetAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tabDadosBasicos, R.string.tabAulas};
    private final Context mContext;

    public spgImpDadoDetAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new frgImpDadosDet();
            case 1:
                return new frgImpDadosAulas();
            default:
                return null;
        }

        //return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}