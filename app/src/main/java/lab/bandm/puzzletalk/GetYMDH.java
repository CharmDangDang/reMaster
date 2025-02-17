package lab.bandm.puzzletalk;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GetYMDH {
    private static class TIME_MAXIMUM {
        private static final int SEC = 60;
        private static final int MIN = 60;
        private static final int HOUR = 24;
        private static final int DAY = 30;
        private static final int MONTH = 12;


    }

    public static String callYMDH() {


        String form = "yyyy-MM-dd kk:mm:ss";

        String KST = "Asia/Seoul";

        Locale.setDefault(Locale.KOREA);
        DateFormat inputDate = new SimpleDateFormat(form);
        inputDate.setTimeZone(TimeZone.getTimeZone(KST));
        Date mDate = new Date();

        return inputDate.format(mDate);

    }

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }


}
