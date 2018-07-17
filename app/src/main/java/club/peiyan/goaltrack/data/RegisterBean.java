package club.peiyan.goaltrack.data;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class RegisterBean {

    /**
     * code : 200
     * data : {"Location":"http://172.16.206.106:8080/api/users/2","username":"Hudang"}
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
         * Location : http://172.16.206.106:8080/api/users/2
         * username : Hudang
         */

        private String Location;
        private String username;

        public String getLocation() {
            return Location;
        }

        public void setLocation(String Location) {
            this.Location = Location;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
