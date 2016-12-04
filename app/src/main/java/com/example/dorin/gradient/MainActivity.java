package com.example.dorin.gradient;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final GradientDrawable.Orientation[] orientationList = { GradientDrawable.Orientation.BL_TR,
            GradientDrawable.Orientation.BOTTOM_TOP, GradientDrawable.Orientation.BR_TL,
            GradientDrawable.Orientation.LEFT_RIGHT, GradientDrawable.Orientation.TL_BR,
            GradientDrawable.Orientation.TOP_BOTTOM, GradientDrawable.Orientation.TR_BL,
            GradientDrawable.Orientation.RIGHT_LEFT };

    private ViewPager mViewPager;

    private TransitionDrawable tr;
    boolean flag = true;

    int time = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        tr = (TransitionDrawable) findViewById(R.id.main_content).getBackground();
        tr.setDrawableByLayerId(tr.getId(0), getRandomGradient());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(flag) {
                            tr.setDrawableByLayerId(tr.getId(1), getRandomGradient());
                            tr.startTransition(time);
                        }
                        else {
                            tr.setDrawableByLayerId(tr.getId(0), getRandomGradient());
                            tr.reverseTransition(time);
                        }

                        flag = !flag;
                    }
                });
            }
        }, 0, time);
    }

    private GradientDrawable getRandomGradient() {
        Random rnd = new Random();
        int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int color2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        GradientDrawable.Orientation orientation = orientationList[rnd.nextInt(8)];

        return new GradientDrawable(orientation, new int[] {color1,color2});
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return MenuFragment.newInstance();

            return ColorFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
