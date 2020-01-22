package lab.bandm.puzzletalk.NotiUtil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import lab.bandm.puzzletalk.MainActivity;
import lab.bandm.puzzletalk.R;

public class NotiUtil extends Notification {
    private Context context;

    public static final int CHANNEL_ID = 1;

    private static final int NO_EFFECT = 0;
    private static final int SOUND = 1;
    private static final int VIBRATE = 2;
    private static final int SOUND_AND_VIBRATE = 3;

    public NotiUtil(Context context, String body) {//사용시 new NotiUtil(this, "body");
        this.context = context;
        createNotification(context, null, body, null);
    }

    NotiUtil(Context context, String head, String body, Integer val) {
        this.context = context;

        createNotification(context, head, body, val);
    }

    private void createNotification(Context context, String head, String body, Integer val) {
        Intent showGoIntent = new Intent(context, MainActivity.class);
        showGoIntent.putExtra("particularFragment", "notiIntent");

        showGoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent showGoPendingIntent = PendingIntent.getActivity(context, 0, showGoIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.puztalkminiicon)
                .setContentText(body)
                .setContentTitle(head)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(showGoPendingIntent)
                .setAutoCancel(true);

        if (val == null)
            val = NO_EFFECT;

        switch (val) {
            case NO_EFFECT:
                break;

            case SOUND:
                Uri alarmSound = Settings.System.DEFAULT_RINGTONE_URI;
                builder.setSound(alarmSound);
                break;

            case VIBRATE:
                long[] vibratePattern = {0, 100, 200, 300};
                builder.setVibrate(vibratePattern);
                break;

            case SOUND_AND_VIBRATE:
                builder.setSound(Settings.System.DEFAULT_RINGTONE_URI);
                long[] num = {0, 100, 200, 300};
                builder.setVibrate(num);
                break;
        }

        // 알림 표시 및 오레오 버전 이상일때는 채널 코드 추가
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)       // 정의해야하는 각 알림의 고유한 int값
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new
                        NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }


    }
}
