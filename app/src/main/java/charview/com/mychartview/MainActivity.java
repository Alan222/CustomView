package charview.com.mychartview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.chartView, R.id.waveView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chartView:
                startActivity(new Intent(this,ChartActivity.class));
                break;
            case R.id.waveView:
                startActivity(new Intent(this,WaveActivity.class));
                break;
        }
    }
}
