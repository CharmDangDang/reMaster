package lab.bandm.puzzletalk;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GetYMDH {

    public static String callYMDH() {

        //    long now = System.currentTimeMillis();
        String form = "yyyy-MM-dd hh:mm:ss";
        Date mDate = new Date();

        SimpleDateFormat simpleDate = new SimpleDateFormat(form, Locale.KOREA);

        simpleDate.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        return simpleDate.format(mDate);
    }
}
