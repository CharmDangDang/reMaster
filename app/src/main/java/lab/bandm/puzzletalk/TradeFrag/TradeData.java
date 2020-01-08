package lab.bandm.puzzletalk.TradeFrag;

import java.io.Serializable;

public class TradeData implements Serializable {
    private String trade_title;
    private String trade_id;
    private String trade_content;
    private String trade_YMDH;
    private String nation;
    private String currentTime;
    private String isOK;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }


    public String getTrade_content() {
        return trade_content;
    }

    public void setTrade_content(String trade_content) {
        this.trade_content = trade_content;
    }


    public String getIsOK() {
        return isOK;
    }

    public void setIsOK(String isOK) {
        this.isOK = isOK;
    }


    public String getTrade_title() {
        return trade_title;
    }

    public void setTrade_title(String trade_title) {
        this.trade_title = trade_title;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getTrade_YMDH() {
        return trade_YMDH;
    }

    public void setTrade_YMDH(String trade_YMDH) {
        this.trade_YMDH = trade_YMDH;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
