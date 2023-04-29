package com.huawei.huaweicli;

public class StaticContext {

    private static String login;
    private static String password;
    private static boolean isInited = false;

    private static String session;

    public static void initCreds(String login, String password) {
        if (isInited) {
            throw new RuntimeException("Creds already inited");
        }
        StaticContext.login = login;
        StaticContext.password = password;
        isInited = true;
    }

    public static void changeSession(String newSession) {
        session = newSession;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static String getSession() {
        return session;
    }
}
