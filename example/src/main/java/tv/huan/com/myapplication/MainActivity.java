package tv.huan.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huan.icanvas.chart.CanDrawView;
import com.huan.icanvas.chart.drawable.Friend;

public class MainActivity extends AppCompatActivity {
    private CanDrawView canDrawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canDrawView = (CanDrawView) findViewById(R.id.drawingView);
        canDrawView.addFriend("bg", Bg.class);
        canDrawView.addFriend("dotted", DottedLine.class);
        canDrawView.setMargin(150, 100, 150, 100);
        canDrawView.setUiCallback(new CanDrawView.UiCallback() {
            @Override
            public void onCallByUIInited(CanDrawView chart) {
                Friend f = chart.getFriend("bg");
//                f.setOutScreenListener(new Friend.OutScreenListener() {
//                    @Override
//                    public int getX_append() {
//                        return 0;
//                    }
//
//                    @Override
//                    public int getY_append() {
//                        return 0;
//                    }
//                });
//                f.setOnSetChangeListener(new Friend.OnSetChangeListener() {
//                    @Override
//                    public void onSetChange(Friend who) {
//                         // 调用icanvas.invalidate() 时执行
//                    }
//                });

                Friend dotted = chart.getFriend("dotted");
                dotted.setxMax(500);
                dotted.setyMax(200);
            }
        });
    }
}
