package club.peiyan.goaltrack.data;

import club.peiyan.goaltrack.utils.AppSp;

/**
 * Created by HPY.
 * Time: 2018/7/8.
 * Desc:
 */

public class Constants {
    public static final String LATEST_GOAL = "LATEST_GOAL";
    public static final String HOST = "http://120.79.79.63:8080/";
    public static final String USER_NAME = "username";

    public static final String REGISTER = "register";
    /*Setting Config */
    public static final String SCORE_SHOW_DAY = "SCORE_SHOW_DAY";

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018080460967172";
    public static final String RSA2_PRIVATE = "MIIEogIBAAKCAQEAuum84dEwKg/2gUjtUzfhEI/3p+lmYvvCDg7ztDIr3R8JToi8\n" +
            "W3Nv/cf582DtYtsEt96HzvM6Sw2QR/UYIY3GfC9sl4nEtIZRiA6bW6KeE3SG3HTH\n" +
            "JkSQGAMZw/TUCH2SUUPI/NAIYgqPbiT5wWjaCpbYvwIYmsvIAk/+Pm6W3Dfj5vXh\n" +
            "gKCAHo/2NkAXnzX7baOgu4sm6DSrKLNplVPXgZnIBgAP0iV83ycUg3/NOQ6xftcI\n" +
            "gpHvyHwmTsfzemBjNlASaNI3C65L7wIP6Fee3v+5MLV7+7XLX54HryGBkoYHWMwY\n" +
            "LxOa5KdqK7OpizTyMRRhB5v0X6ZynsllmpFfzwIDAQABAoIBAE8WK3eZHboOew0F\n" +
            "IHX/xNlPMUj49vuW7usn5HIwcGLliPiT3q8LTy2geeBC+O7FIV/zZp9oRxgOqyfn\n" +
            "SsiXxo4xOs972aFQ3Bqno5DIY/5zZcTOHJ3SkO24EiTZ6QWWrFx6RAbEt2dUdI/0\n" +
            "UUFG7ktgvGFFr8d/xidEVZ1xCaAvw590CyJr1gwbNZYyjqzP7jl4mGKgVFB1+C1u\n" +
            "AbCGPXlHbSqejLDVkLSMXMB328R5fYePqeG8hv3IpKlrf2VhEwLDK15U9eiLKl1z\n" +
            "AXM8rchcQRbg6xgo04gt/SVLx2D9tzZ9X7uzRceD/PuLch9hWkr0JKNKDp2gWyiN\n" +
            "StkRdMECgYEA9Do9KvQIBdVsPNqaraZxfGlKhTlDq1vvPkXcOB67csxVns0oKzHN\n" +
            "PqrTXpUnwdctUkh4hJUnfOrVN0pxklVeUqswbRnoLOZnvDWxQODjemuHtCPI6Mxt\n" +
            "LYUCGy0WpehqJNLG847hxRzvFvmL9iEdk4n/Q4NRmZ1uVG1sbbwYo1ECgYEAw+w9\n" +
            "bNZsWFAbdIMaFBDR+8zzARN0Ai2pCArnvrAJZEA8Ed1ukE7cnSmW9k516qlXijkl\n" +
            "bjg1brohlZPycjJtR7I6ar7uSLlO4VP5UbHkNT5UbTsjirMrx3GJSB0ADO/pCKY3\n" +
            "KMnlH3rucApmDlc+oG+rMUc5cU5LdfFToZDryR8CgYAmyETEWGIZww1gEHq1jpPA\n" +
            "5Ntvkmtbf8rbAa75vm+XyDjkccGqMktcYzYTc+4PSwMoaNeyfhj6LjRThl/IlAPZ\n" +
            "btPOz2leD+xzJ7eRDOUyoHa5NWGHDkdcDh4KgLty9xg75rivHoZdjb2t0UzXdeTD\n" +
            "dAt0BhtkIGrPv1yThGYJMQKBgH7ESCo3wvqROnJ0sVbyWyt1PHYhxBn0uT2+8Jtt\n" +
            "A2aeOT/F6lfs7smxsz/DHICs+TsyJoneTJRMUpaZl3FUN2kAZNPUnhDn9aXN06+M\n" +
            "ToA0yCO09BY2P8lYKN9EBFhakolKIr59g4aO3+AWmloFETx1hGrqDrzFUAYME3Si\n" +
            "dC05AoGAXkHPJOexsk3NfR2FXmNRg6M6AtpLhbSuflr4T3qpORWD8QVc2sBtRGTR\n" +
            "k/KErM+5qCTIK2gJjZrWksF/FbRxo5ROD2G5zy99/ozDDfPXRfJTnJTro4oHoMHl\n" +
            "lQX2KMscaP7HaH+CnQER4b6ZAdINeNe0NiXTshfUjQYD1Jyfdvo=";
    public static final String RSA_PRIVATE = "";

    /**
     * 分数显示的天数
     *
     * @return 默认31天
     */
    public static int getScoreShowDay() {
        return AppSp.getInt(Constants.SCORE_SHOW_DAY, 31);
    }

    public static void setScoreShowDay(int days) {
        AppSp.putInt(Constants.SCORE_SHOW_DAY, days);
    }
}
