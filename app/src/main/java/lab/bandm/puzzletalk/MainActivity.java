package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import lab.bandm.puzzletalk.CoopFrag.CoopFrag;
import lab.bandm.puzzletalk.Frag.PublicChatRoomFrag;
import lab.bandm.puzzletalk.Frag.TabAdapter;
import lab.bandm.puzzletalk.TradeFrag.TradeFrag;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    FragmentManager fragmentManager;
    ImageButton setiingbtn;
    TabAdapter tabAdapter;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        PublicChatRoomFrag chatRoomFrag = new PublicChatRoomFrag();
        TradeFrag tradeFrag = new TradeFrag(context);
        tradeFrag.getId();

        CoopFrag coopFrag = new CoopFrag();

        FINDID();
        fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(this,fragmentManager);
        tabAdapter.addFrag(chatRoomFrag);
        tabAdapter.addFrag(tradeFrag);
        tabAdapter.addFrag(coopFrag);


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.dahyun);
        tabLayout.getTabAt(1).setIcon(R.drawable.miniicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.ptalkiconmadebyhmk);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setiingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
    }
   /* SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFrag(webFrag1);
        sectionsPagerAdapter.addFrag(webFrag2);
    ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);*/


    public void FINDID() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabTT);
        setiingbtn = findViewById(R.id.settingbtn);

    }

    @SuppressLint("StaticFieldLeak")
    public static Activity context;

}