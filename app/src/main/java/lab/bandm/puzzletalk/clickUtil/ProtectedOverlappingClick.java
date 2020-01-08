package lab.bandm.puzzletalk.clickUtil;

import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

public abstract class ProtectedOverlappingClick implements View.OnClickListener {
    // 클릭 인터벌 1초
    private static final long CLICK_INTERVAL = 10000;
    private long mLastClickTime = 0;
    public abstract void onSingleClick(View v);
    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        // 중복클릭 아닌 경우
        if (elapsedTime > CLICK_INTERVAL) {
            onSingleClick(v);
        }



    }
}
