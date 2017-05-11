package charview.com.mychartview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import charview.com.custom.MyChartView;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    MyChartView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(30);
        list.add(40);
        list.add(60);
        list.add(45);
        list.add(46);
        list.add(65);
        list.add(36);
        mChart.setItemData(list);
    }
}
