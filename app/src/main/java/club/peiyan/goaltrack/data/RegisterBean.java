package club.peiyan.goaltrack.data;

/**
 * Created by HPY.
 * Time: 2018/7/12.
 * Desc:
 */

public class RegisterBean {

    /**
     * code : 200
     * data : {"id":"9dd531f9-99cd-4575-9236-87d72ae16346"}
     * msg : Login success.
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
         * id : 9dd531f9-99cd-4575-9236-87d72ae16346
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
