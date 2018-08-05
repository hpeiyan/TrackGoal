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
    public static final String ALARM_ON = "ALARM_ON";


    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018080460967172";
    public static final String RSA2_PRIVATE = "MIIEpAIBAAKCAQEAw5M6qYVpcRto96pOvU4PncDhXXscRaCHUnyOBDvH1ibWi9FT\n" +
            "1zbSaxVUukhgivFdnO0TA0eMz53jpKXfoRuTebnwOxcTOgI56pTdLtbhHonDhv7g\n" +
            "q4SoCxd8KMZR+Fgj/CeKCHU4IyS2kHr9kquweCQoyiWkbz6Yeqtfpo/xRKnFkPUn\n" +
            "bLANJLv6EPtyB6w+8znZ/hzzDVkAkJ5RiXtWJ1r9m2N4xsuUzBLQhS2knxRr5n1y\n" +
            "lsnJrpRBv4AW7KeqpxaSdco6+VwaluhtW5//WqPaXlJrkDBzuk97NTUzcYB3IW1j\n" +
            "uscKeV+T3EoaIR3LmfZLluPWypOPdlVDWQ0d2QIDAQABAoIBAQCHPyR9ILZ3MnZX\n" +
            "dx32iaoA9Oakp8M4XzfVki7/vHuIm9kYXYz2MoI27Uxnei/RRfLv136jcPJujQhk\n" +
            "zxSkLBbDQhNgOafhILL+hy2h+5U4chqWG2IxGo6/BOGmrOb4r7NK5+jrymwk2Xmq\n" +
            "1nLO4A4sNw1CJd/d0lLN2f9OhxJY4TzOVdV7EV539llu05hFrSr1T4Hd3EJ475Kb\n" +
            "I2EZ0wL+XTgLosRCbLq7OlWsnXW0uqHVwQHuB+hL81rAY9fvU6SgE6l3PRXFK/YC\n" +
            "3iCjxffQ6+GwJY2+SKFJFjvmtcldBDxy5ZQfq0j2GGnBdUr7sqzaquEOfo2eMrtW\n" +
            "sQNU2iXFAoGBAOuC7oN9sXlKRBAbMAqWv+lzK2TYJ+uUQLWCb0oBo4LMxo6Ol5Wg\n" +
            "mnIM0Jtp6ZwuublJYGMebSZIGT4cK2Lf3p3AD49NbR1PlqNkjN6tzWXQFrix2Htn\n" +
            "4666FtUwNvwd+kyQHjg8kNWIl8IPE89ra8P/Z+bcdR6lFHOYvw7hnyibAoGBANSW\n" +
            "4GIq4ylxep988VQDmQfS3LzYBGFUbIiE99Xsa9v/7J98zbIIl+ZV6uc/HduiFphV\n" +
            "ky+GlgDp4sIMR0ZXio9tFuvee45czKkwEEOr/qZbxuhXH+rFAy0pne2K1pixNVOc\n" +
            "DA2cTKxgwZWWm1kudt5sbPd1r4ZoBhFVjiZhMxibAoGALdUTOb+yVBph2IiR9t2T\n" +
            "h4kEPuHaEvyNA0XZBpv/CCt10LRrR5EzvVJFHpmj/uMQ/cAL4/49259YWj/9KYbL\n" +
            "ugNwg8DbpUKh3DZrO8KzIEc4xMTHjmyPYKeSXfa+HJ5w4YmCeAtBXGzyq69neYw8\n" +
            "Mq53t/PbTxVtP3nQZML/yFUCgYEAy0KataG14pkqmjU29JIgDhfxj+mmfnze7MTd\n" +
            "xb8v/YA7+N22OkP3IHcDiyiQ3r+ihPJFMUqoP6VgdxKQT1RCGhI+uTTWw+48jIN0\n" +
            "+Y8Ignz7uIgc7zaBa/v/kPCPcBz3FML9z0GjGanFTcZbMDC53L6kjRILyiHJs2cu\n" +
            "68lBtoMCgYBmTUOByMbLNwYrNzZup80OJv+2dIsmEPOOmZSqFl+tCdzpxQhWd9Mh\n" +
            "SbTRd6b6vo64leWEbhiPeBRR1GSAI8xmXNPebI1oK+kVS8xKS4Dixpx9aZZ94pR0\n" +
            "IH3f/MXd9HRkyqrpG8nQyshHYWjrLY9DnKLb+x1Ot/viEP3mR7Xjbw==";
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
