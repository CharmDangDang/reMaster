package lab.bandm.puzzletalk.Frag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import lab.bandm.puzzletalk.R;

public class TabAdapter extends FragmentStatePagerAdapter {

    @Nullable
    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    private List<Fragment> listF = new ArrayList<>();

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1,R.string.tab_text_2};
    private final Context mContext;

    @SuppressLint("WrongConstant")
    public TabAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return listF.get(position);
    }
    public void addFrag(Fragment fragment) {
        listF.add(fragment);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return listF.size();
    }

}