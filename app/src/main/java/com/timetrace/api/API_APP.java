package com.timetrace.api;

/**
 * Created by Atomu on 2014/12/1.
 * ApplicationUtil getAppInstalled -> List<ApplicationInfo>
 * 直接根据 packName 还有 appName 判断, 系统进程里面保留了包含 player, browser, music, news, video
 */
public interface API_APP {

    /**
     * 消费类的, 大众点评, 淘宝
     */
    public final int LIFE_STYLE = 1;
    /**
     * QQ, renren
     */
    public final int SOCIAL = 2;
    /**
     * 知乎, XX新闻
     */
    public final int INFORMATION = 3;
    /**
     * 音乐, 游戏之类
     */
    public final int ENTERTAINMENT = 4;
    /**
     * 工作
     */
    public final int WORK = 5;
    /**
     * 不属于以上几类
     */
    public final int OTHER = 0;
}
