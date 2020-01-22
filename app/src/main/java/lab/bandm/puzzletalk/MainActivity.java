package lab.bandm.puzzletalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lab.bandm.puzzletalk.Frag.PublicChatRoomFrag;
import lab.bandm.puzzletalk.Frag.TabAdapter;
import lab.bandm.puzzletalk.TradeFrag.TradeFrag;
import lab.bandm.puzzletalk.ui.CustomActionBar;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    MenuItem searchItem;
    FragmentManager fragmentManager;
    SearchView searchView;
    TabAdapter tabAdapter;
    TradeFrag tradeFrag;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        SetActionbar();
        final PublicChatRoomFrag chatRoomFrag = new PublicChatRoomFrag();
        tradeFrag = new TradeFrag(context);

        FINDID();
        fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(this, fragmentManager);
        tabAdapter.addFrag(chatRoomFrag);
        tabAdapter.addFrag(tradeFrag);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void FINDID() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabTT);

    }

    @SuppressLint("StaticFieldLeak")
    public static Activity context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tradeFrag.TextSearch(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.suggest:
                final EditText et = new EditText(context);
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
                alt_bld.setTitle("건의/신고")
                        .setMessage("익명으로 전달됩니다.")
                        .setIcon(R.drawable.puztalkminiicon)
                        .setCancelable(true)
                        .setView(et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String value = et.getText().toString();
                                DatabaseReference suggest = FirebaseDatabase.getInstance().getReference().child("suggest");
                                suggest.push().setValue(value);
                                Toast.makeText(context, "메세지가 전달되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                final AlertDialog alert = alt_bld.create();
                alert.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    }
                });
                alert.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (viewPager.getCurrentItem() == 0) {
            searchItem.setVisible(false);
        } else {
            searchItem.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void SetActionbar() {
        CustomActionBar customActionBar = new CustomActionBar(this,getSupportActionBar());
        customActionBar.setActionBar();
    }
}



