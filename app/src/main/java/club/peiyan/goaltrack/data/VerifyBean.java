package club.peiyan.goaltrack.data;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class VerifyBean {


    /**
     * code : 200
     * data : {"username":"Huang"}
     * msg : Success
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * username : Huang
         */

        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
