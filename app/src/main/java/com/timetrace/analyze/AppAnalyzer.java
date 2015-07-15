package com.timetrace.analyze;

import com.timetrace.api.API_APP;
import com.timetrace.monitor.AppDBHelper;
import com.timetrace.monitor.AppInfoProvider;
import com.timetrace.objects.ApplicationInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 负责统计app类型
 *
 */
public class AppAnalyzer {
    private static Map<String, ApplicationInfo> installedApps;

   //根据起始时间和结束时间统计相关数据
    public static long[] analyze(long start,long end) {
        Map<String, ApplicationInfo> map = new HashMap<String, ApplicationInfo>();
//        long current = System.currentTimeMillis();
        AppDBHelper.readProcessStatistic(map, start, end);
//        AppDBUtil.readProcessStatistic(map, TimeUtil.getDayStart(current), current);


        long[] typeStatistic = new long[6];
        if (map.isEmpty())
            System.out.println("map empty");

        for (String key : map.keySet()) {
            int type = getAppType(map.get(key));
            System.out.println(key);
            typeStatistic[type] += map.get(key).getActiveTime();
        }

        return typeStatistic;
    }

    //TODO：app分类结果用数据库保存，该函数从数据库中查询分类结果，如果不存在再手动分类。
    private static int getAppType(ApplicationInfo applicationInfo) {
        String packName = applicationInfo.getPacName().toLowerCase();
        if (packName.equals("com.miui.notes") || packName.equals("com.miui.notes"))
            return API_APP.LIFE_STYLE;
        if (packName.equals("com.tencent.mm") || packName.equals("com.android.phone") || packName.equals("com.miui.yellowpage"))
            return API_APP.SOCIAL;
        if (packName.equals("com.android.browser"))
            return API_APP.INFORMATION;
        if (packName.equals("com.miui.player") || packName.equals("com.xiaomi.gamecenter") || packName.equals("android.process.media"))
            return API_APP.ENTERTAINMENT;
        if (packName.equals("siir.es.adbWireless"))
            return API_APP.WORK;

        if (installedApps == null){
            installedApps = new HashMap<String, ApplicationInfo>();
            for (ApplicationInfo info: AppInfoProvider.getInstance().getInstalledApps()){
                installedApps.put(info.getPacName(), info);
            }
        }

        if (!installedApps.containsKey(packName))
            return API_APP.OTHER;

        ApplicationInfo applicationInfo1 = installedApps.get(packName);
        String appName = applicationInfo1.getAppName().toLowerCase();

        if (packName.contains("ali") || packName.contains("tmall") || packName.contains("meituan") || packName.contains("dazhong")
                || packName.contains("life") || packName.contains("jdong") || packName.contains("vip"))
            return API_APP.LIFE_STYLE;
        if (packName.contains("tencent") || packName.contains("qq") || packName.contains("sina") || packName.contains("wechat")
                || packName.contains("renren") || packName.contains("fackbook") || packName.contains("twiter") || packName.contains("momo")
                || packName.contains("chat") || packName.contains("fetion"))
            return API_APP.SOCIAL;
        if (packName.contains("news") || packName.contains("blog") || packName.contains("zhihu") || packName.contains("36kr")
                || packName.contains("csdn") || packName.contains("geek"))
            return API_APP.INFORMATION;
        if (packName.contains("music") || packName.contains("play") || packName.contains("video") || packName.contains("game")
                || packName.contains("disney"))
            return API_APP.ENTERTAINMENT;
        if (packName.contains("work") || packName.contains("note") || packName.contains("pad") || packName.contains("metting")
                || packName.contains("efficiency"))
            return API_APP.WORK;

        if (appName.contains("支付") || appName.contains("pay") || appName.contains("淘宝") || appName.contains("taobao")
                || appName.contains("天猫") || appName.contains("tmall") || appName.contains("京东") || appName.contains("jdong")
                || appName.contains("大众") || appName.contains("dazhong") || appName.contains("美团") || appName.contains("meituan")
                || appName.contains("唯品") || appName.contains("vip") || appName.contains("1号店"))
            return API_APP.LIFE_STYLE;
        if (appName.contains("qq") || appName.contains("微信") || appName.contains("wechat") || appName.contains("腾讯") || appName.contains("tencent")
                || appName.contains("微博") || appName.contains("sina") || appName.contains("人人") || appName.contains("renren")
                || appName.contains("facebook") || appName.contains("twiter") || appName.contains("momo") || appName.contains("陌陌")
                || appName.contains("聊") || appName.contains("信") || appName.contains("chat") || appName.contains("message"))
            return API_APP.SOCIAL;
        if (appName.contains("新闻") || appName.contains("news") || appName.contains("论坛") || appName.contains("社区")
                || appName.contains("blog") || appName.contains("博客") || appName.contains("知乎") || appName.contains("zhihu")
                || appName.contains("36氪") || appName.contains("36kr") || appName.contains("csdn")
                || appName.contains("极客") || appName.contains("geek"))
            return API_APP.INFORMATION;
        if (appName.contains("音") || appName.contains("乐") || appName.contains("music") || appName.contains("yin") || appName.contains("yue")
                || appName.contains("游") || appName.contains("game") || appName.contains("跑") || appName.contains("run")
                || appName.contains("逃") || appName.contains("战") || appName.contains("war") || appName.contains("play"))
            return API_APP.ENTERTAINMENT;
        if (appName.contains("效") || appName.contains("工作") || appName.contains("efficiency") || appName.contains("work")
                || appName.contains(""))
            return API_APP.WORK;


        return API_APP.OTHER;
    }

}
