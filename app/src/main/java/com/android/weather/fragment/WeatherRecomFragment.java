package com.android.weather.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.weather.R;
import com.android.weather.gson.WeatherBean;
import com.android.weather.helper.HttpUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *desc: 天气推荐
 */
public final class WeatherRecomFragment extends Fragment {
    @BindView(R.id.comfort_text)
    TextView comfortText;
    @BindView(R.id.car_wash_text)
    TextView carWashText;
    @BindView(R.id.sport_text)
    TextView sportText;
    public static WeatherRecomFragment newInstance() {
        return new WeatherRecomFragment();
    }
    private View inflate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_weather_recom, null, false);
        ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }
    protected void initView() {
        requestAqi("杭州");
    }
    /**
     * 设置生活建议数据
     *
     * @param weather
     */
    private void showSuggest(WeatherBean.HeWeather6Bean weather) {
        List<WeatherBean.HeWeather6Bean.LifestyleBean> lifeStyleBeans = weather.getLifeStyleBeans();
        String comfort = "";
        String carWash = "";
        String sport = "";
        for (int i = 0; i < lifeStyleBeans.size(); i++) {
            WeatherBean.HeWeather6Bean.LifestyleBean lifeStyleBean = lifeStyleBeans.get(i);
            switch (lifeStyleBean.getType()) {
                case "comf":
                    comfort = "舒适度： " + lifeStyleBean.getTxt();
                    break;
                case "cw":
                    carWash = "汽车指数： " + lifeStyleBean.getTxt();
                    break;
                case "sport":
                    sport = "运动建议： " + lifeStyleBean.getTxt();
                    break;
            }
        }

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);


    }
    /**
     * 获取当前城市空气质量
     */
    public void requestAqi(final String cityName) {
        String weatherUrl = "https://free-api.heweather.net/s6/weather/lifestyle?location=" + cityName + "&key=f23ef7e1cd4e42adaaf5f6ca99a6fbe2";
        Log.d("TAG", "开始发出请求查询天气，url=" + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "请求失败");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", "查询成功");
                final String responseText = response.body().string();
                Log.d("TAG", " 返回结果为 " + responseText);
                Gson gson = new Gson();
                final WeatherBean weatherBean = gson.fromJson(responseText, WeatherBean.class);
                final WeatherBean.HeWeather6Bean heWeather6Bean = weatherBean.getHeWeather6().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (heWeather6Bean != null && "ok".equals(heWeather6Bean.getStatus())) {
                            showSuggest(heWeather6Bean);
                        } else {
                            Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}