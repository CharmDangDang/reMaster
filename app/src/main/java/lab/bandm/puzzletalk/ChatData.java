package lab.bandm.puzzletalk;

public class ChatData {
    public String enemyname;
    public String msg;
    public int nameColor;


    public ChatData () {}

    public ChatData(String enemyname, String msg, int nameColor) {
        this.enemyname = enemyname;
        this.msg = msg;
        this.nameColor = nameColor;

    }

    public int getNameColor() {
        return nameColor;
    }

    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }


    public String getEnemyname() {
        return enemyname;
    }

    public void setEnemyname(String nickname) {
        this.enemyname = nickname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
