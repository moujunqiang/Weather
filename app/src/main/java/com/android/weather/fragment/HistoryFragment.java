package com.android.weather.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.weather.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 历史页面
 */
public final class HistoryFragment extends Fragment {
    @BindView(R.id.tl_tabs)
    TabLayout tableLayout;
    @BindView(R.id.vp_content)
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }
    private View inflate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_weather, null, false);
        ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }


    protected void initView() {
        fragments.add(WeatherHistoryFragment.newInstance());
        fragments.add(WeatherCityFragment.newInstance());
        titles.add("天气历史");
        titles.add("城市历史");
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles.get(position);
            }
        });

        tableLayout.setupWithViewPager(viewPager);
    }


}