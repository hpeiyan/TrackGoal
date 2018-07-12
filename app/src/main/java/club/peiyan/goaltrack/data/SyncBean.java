package club.peiyan.goaltrack.data;

import java.util.List;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class SyncBean {

    /**
     * code : 200
     * data : [{"id":1,"level":3,"over":"over","parent":"parent","start":"start","status":1,"timestamp":10,"title":"title3"},{"id":2,"level":1,"over":"2018/7/20","parent":"rootParent","start":"2018/7/3","status":1,"timestamp":-1993054218,"title":"thanks"},{"id":3,"level":1,"over":"2018/7/31","parent":"rootParent","start":"2018/7/2","status":1,"timestamp":-1993040336,"title":"ok I'll let you know"},{"id":4,"level":1,"over":"2018/7/27","parent":"rootParent","start":"2018/7/10","status":1,"timestamp":-1948537815,"title":"this message"},{"id":5,"level":3,"over":"over","parent":"parent","start":"start","status":1,"timestamp":10,"title":"title5"}]
     * msg : success
     */

    private int code;
    private String msg;
    private List<GoalBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GoalBean> getData() {
        return data;
    }

    public void setData(List<GoalBean> data) {
        this.data = data;
    }


}
