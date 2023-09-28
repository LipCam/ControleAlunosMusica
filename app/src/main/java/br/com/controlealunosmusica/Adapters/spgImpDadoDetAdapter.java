package br.com.controlealunosmusica.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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