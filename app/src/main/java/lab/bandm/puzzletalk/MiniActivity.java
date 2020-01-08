package lab.bandm.puzzletalk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Objects;

public class MiniActivity extends AppCompatActivity {

    WindowManager wm;
    int LAYOUT_FLAG;
    View view;
    float xpos=0;
    float ypos=0;
    @SuppressLint({"ResourceType", "InflateParams"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini);

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(   //오버레이의 크기 및 여러가지 설정
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                0,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START | Gravity.TOP;  //기본 생성 위치
        view = Objects.requireNonNull(inflate).inflate(R.layout.activity_mini, null);
        wm.addView(view, params);
        MiniActivity.this.finish();
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Display display;
                display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int action = event.getAction();
                int pointerCount = event.getPointerCount();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (pointerCount == 1) {
                            xpos = event.getRawX();
                            ypos = event.getRawY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pointerCount == 1) {
                            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) v.getLayoutParams();
                            float dx = xpos - event.getRawX();
                            float dy = ypos - event.getRawY();
                            xpos = event.getRawX();
                            ypos = event.getRawY();
                            lp.x = (int) (lp.x - dx);
                            lp.y = (int) (lp.y - dy);
                            wm.updateViewLayout(v, lp);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

}
