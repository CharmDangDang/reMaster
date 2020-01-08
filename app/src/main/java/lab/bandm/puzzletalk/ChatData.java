package lab.bandm.puzzletalk;

public class ChatData {
    public String enemyname;
    public String msg;

    public ChatData () {}
    public ChatData(String enemyname, String msg) {
        this.enemyname = enemyname;
        this.msg = msg;
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
