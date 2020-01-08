package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import lab.bandm.puzzletalk.Frag.PublicChatRoomFrag;
import lab.bandm.puzzletalk.Frag.TabAdapter;
import lab.bandm.puzzletalk.TradeFrag.TradeFrag;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    FragmentManager fragmentManager;

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


        FINDID();
        fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(this,fragmentManager);
        tabAdapter.addFrag(chatRoomFrag);
        tabAdapter.addFrag(tradeFrag);



        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.dahyun);
        tabLayout.getTabAt(1).setIcon(R.drawable.ptalkiconmadebyhmk);

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


    }

    @SuppressLint("StaticFieldLeak")
    public static Activity context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.suggest:


        }
            return super.onOptionsItemSelected(item);
    }
}