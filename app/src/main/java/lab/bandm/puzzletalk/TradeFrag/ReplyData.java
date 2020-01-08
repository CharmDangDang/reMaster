package lab.bandm.puzzletalk.TradeFrag;

import java.io.Serializable;

public class ReplyData implements Serializable {
    public String r_id;
    public String r_YMDH;
    public String r_content;


    public ReplyData() {
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_YMDH() {
        return r_YMDH;
    }

    public void setR_YMDH(String r_YMDH) {
        this.r_YMDH = r_YMDH;
    }

    public String getR_content() {
        return r_content;
    }

    public void setR_content(String r_content) {
        this.r_content = r_content;
    }
}
