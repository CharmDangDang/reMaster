package lab.bandm.puzzletalk.TradeFrag;

import java.io.Serializable;

public class ReplyData implements Serializable {
    private String r_id;
    private String r_YMDH;
    private String r_content;
    private String m_Token;

    public String getM_Token() {
        return m_Token;
    }

    public void setM_Token(String m_Token) {
        this.m_Token = m_Token;
    }

    public ReplyData(String r_id, String r_YMDH, String r_content,String m_Token) {
        this.r_id = r_id;
        this.r_YMDH = r_YMDH;
        this.r_content = r_content;
        this.m_Token = m_Token;
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
