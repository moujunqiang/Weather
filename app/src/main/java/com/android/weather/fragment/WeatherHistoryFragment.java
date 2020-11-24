package com.android.weather.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.android.weather.R;
import com.android.weather.db.WeatherDao;
import com.android.weather.helper.SPUtils;
import com.android.weather.weatherview.WeatherItemView;
import com.android.weather.weatherview.WeatherModel;
import com.android.weather.weatherview.WeatherView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 历史
 */
public final class WeatherHistoryFragment extends Fragment {
    @BindView(R.id.weather_view)
    WeatherView weatherView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    public static WeatherHistoryFragment newInstance() {
        return new WeatherHistoryFragment();
    }
    private View inflate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history, null, false);
        ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }

    protected void initView() {
        //填充天气数据
        weatherView.setList(generateData());

        //画折线
        //weatherView.setLineType(ZzWeatherView.LINE_TYPE_DISCOUNT);
        //画曲线(已修复不圆滑问题)
        weatherView.setLineType(WeatherView.LINE_TYPE_CURVE);

        //设置线宽
        weatherView.setLineWidth(2f);

        //设置一屏幕显示几列(最少3列)
        try {
            weatherView.setColumnNumber(6);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置白天和晚上线条的颜色
        weatherView.setDayAndNightLineColor(Color.parseColor("#E4AE47"), Color.parseColor("#58ABFF"));

        //点击某一列
        weatherView.setOnWeatherItemClickListener(new WeatherView.OnWeatherItemClickListener() {
            @Override
            public void onItemClick(WeatherItemView itemView, int position, WeatherModel weatherModel) {
                //Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherView.setList(generateData());

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    private List<WeatherModel> generateData() {
        List<WeatherModel> list = new ArrayList<>();
        String mWeatherId;
        mWeatherId = (String) SPUtils.get(getContext(), "weatherId", (String) SPUtils.get(getContext(), "local", ""));
        List<WeatherModel> weatherModels = new WeatherDao(getContext()).queryAlllxr(mWeatherId);
        //获取设置里面显示的天数
        String count = (String) SPUtils.get(getContext(), "count", "7");
        int i = Integer.parseInt(count);
        if (i == 0) {
        } else {
            if (weatherModels == null || weatherModels.size() == 0) {
                return list;
            }
            for (int j = 0; j < weatherModels.size(); j++) {
                if (j == i) {
                    break;
                }
                WeatherModel weatherModel = weatherModels.get(j);
                list.add(weatherModel);
            }
        }
        swipeRefreshLayout.setRefreshing(false);

        return list;
    }
}