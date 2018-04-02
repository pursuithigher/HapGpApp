package com.example.asus.gp1.activities;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.asus.gp1.R;
import com.example.asus.gp1.adapter.OnFragmentInteractionListener;
import com.example.asus.gp1.fragment.ClassFragment;
import com.example.asus.gp1.fragment.HomeFragment;
import com.example.asus.gp1.fragment.InfoFragment;
import com.example.asus.gp1.fragment.StdinfoFragment;
import com.example.asus.gp1.fragment.TeacherClassFragment;
import com.example.asus.gp1.helper.MetaData;
import com.example.asus.gp1.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private ViewPager mviewpager;
    private BottomNavigationView navigation;

    int[][] states = new int[][]{
            new int[]{-android.R.attr.state_checked},
            new int[]{android.R.attr.state_checked}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        int[] colors = new int[]{ContextCompat.getColor(this, R.color.home_bottom_checked),
                ContextCompat.getColor(this, R.color.colorAccent)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        navigation.setItemTextColor(csl);
        navigation.setItemIconTintList(csl);
        mviewpager = findViewById(R.id.viewpager);
        initData();
    }

    private void initData(){
        List<Fragment> datas = new ArrayList<>();
        datas.add(new HomeFragment());

        if (MetaData.Role.equals("Teacher")) {
            datas.add(new TeacherClassFragment());  //3
        }else{
            datas.add(new ClassFragment()); //1
        }
        if (MetaData.Role.equals("Teacher")) {
            datas.add(new InfoFragment());  //2
        }else {
            datas.add(new StdinfoFragment());   //4
        }
        mviewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), datas));
        mviewpager.addOnPageChangeListener(changeListener);
    }

    private ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            navigation.setSelectedItemId(navigation.getMenu().getItem(position).getItemId());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("校园助手");
                    mviewpager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    if (MetaData.Role.equals("Teacher")) {
                        setTitle("您开设的课程");
                    } else {
                        setTitle("您参加的课程");
                    }
                    mviewpager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    if (MetaData.Role.equals("Teacher")) {
                        setTitle("签到情况");
                    } else {
                        setTitle("签到系统");
                    }
                    mviewpager.setCurrentItem(2);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
