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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final GradientDrawable.Orientation[] orientationList = { GradientDrawable.Orientation.BL_TR,
            GradientDrawable.Orientation.BOTTOM_TOP, GradientDrawable.Orientation.BR_TL,
            GradientDrawable.Orientation.LEFT_RIGHT, GradientDrawable.Orientation.TL_BR,
            GradientDrawable.Orientation.TOP_BOTTOM, GradientDrawable.Orientation.TR_BL,
            GradientDrawable.Orientation.RIGHT_LEFT };

    private Random rnd;
    private ViewPager mViewPager;

    private TransitionDrawable tr;

    int time = 10000;
    int alpha = 255;
    int red = 255;
    int green = 255;
    int blue = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        rnd = new Random();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        tr = (TransitionDrawable) findViewById(R.id.main_content).getBackground();
        tr.setDrawableByLayerId(tr.getId(0), getRandomGradient());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            boolean flag = true;

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
        int color1 = Color.argb(alpha, rnd.nextInt(red), rnd.nextInt(green), rnd.nextInt(blue));
        int color2 = Color.argb(alpha, rnd.nextInt(red), rnd.nextInt(green), rnd.nextInt(blue));

        GradientDrawable.Orientation orientation = orientationList[rnd.nextInt(8)];

        return new GradientDrawable(orientation, new int[] {color1,color2});
    }

    public static class MenuFragment extends Fragment {
        private SeekBar sbTime;
        private SeekBar sbWhite;
        private SeekBar sbRed;
        private SeekBar sbGreen;
        private SeekBar sbBlue;

        public static MenuFragment newInstance() {
            return new MenuFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.menu_fragment, container, false);
            sbTime = (SeekBar) view.findViewById(R.id.seekBar);
            sbTime.setProgress(10000);

            sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                    Log.e("123", progress + "");
                }
            });

            sbWhite = (SeekBar) view.findViewById(R.id.seekBarWhite);
            sbWhite.setProgress(255);

            sbRed = (SeekBar) view.findViewById(R.id.seekBarRed);
            sbRed.setProgress(255);

            sbGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
            sbGreen.setProgress(255);

            sbBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
            sbBlue.setProgress(255);

            return view;
        }
    }

    public static class ColorFragment extends Fragment {
        public static ColorFragment newInstance() {
            return new ColorFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.color_fragment, container, false);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 1)
                return MenuFragment.newInstance();

            return ColorFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
