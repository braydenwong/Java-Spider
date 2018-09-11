
package com.braydenwong.youtube;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.braydenwong.Abstract.AbstractSpider;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

import java.util.HashMap;
import java.util.Map;


/**
 * @author: Braydenwong
 * @date: 2018/09/03
 */

public class YouTuBeSpider extends AbstractSpider {
    public static void main(String[] args) {
        Session session = Requests.session();
        String headers = "accept: */*\n" +
                "accept-encoding: gzip, deflate, br\n" +
                "accept-language: zh-CN,zh;q=0.9\n" +
                "cookie: VISITOR_INFO1_LIVE=AOoW5EFAREI; _ga=GA1.2.1432266453.1534835110; PREF=f1=50000000&cvdm=grid; YSC=kLToQv_R-q8; ST-f2a0zt=itct=CDYQ8JMBGAIiEwib-ZCt2p3dAhWRLWAKHTgmCaMomxw%3D&csn=ZpGMW5uuJpHbgAO4zKSYCg; ST-1xeebhs=itct=CDcQ8JMBGAEiEwib-ZCt2p3dAhWRLWAKHTgmCaMomxw%3D&csn=ZpGMW5uuJpHbgAO4zKSYCg\n" +
                "referer: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/playlists\n" +
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\n" +
                "x-client-data: CJK2yQEIpbbJAQjBtskBCKmdygEI153KAQjancoBCKijygE=\n" +
                "x-spf-previous: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/playlists\n" +
                "x-spf-referer: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/playlists\n" +
                "x-youtube-client-name: 1\n" +
                "x-youtube-client-version: 2.20180830\n" +
                "x-youtube-page-cl: 210947591\n" +
                "x-youtube-page-label: youtube.ytfe.desktop_20180829_6_RC1\n" +
                "x-youtube-sts: 17773\n" +
                "x-youtube-utc-offset: 480\n" +
                "x-youtube-variants-checksum: d9c008758e968d12ab791a74d2abe05c";
        Map<String, String> params = new HashMap<>(1);
        params.put("pbj", "1");
        String s = session.get("https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/videos")
                .params(params)
                .headers(setHeaders(headers))
                .send()
                .readToText();
        System.out.println(s);
        //itct
        String clickTrackingParams = getValueByKeyFromJson(s, "clickTrackingParams");
        //ctoken
        String continuation = getValueByKeyFromJson(s, "continuation");
        Map<String, String> param2 = new HashMap<>(3);
        param2.put("ctoken", continuation);
        param2.put("continuation", continuation);
        param2.put("itct", clickTrackingParams);
        Map<String, Object> headers2 = setHeaders("accept: */*\n" +
                "accept-encoding: gzip, deflate, br\n" +
                "accept-language: zh-CN,zh;q=0.9\n" +
                "cookie: VISITOR_INFO1_LIVE=AOoW5EFAREI; _ga=GA1.2.1432266453.1534835110; PREF=f1=50000000&cvdm=grid; YSC=kLToQv_R-q8; GPS=1\n" +
                "referer: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/videos\n" +
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\n" +
                "x-client-data: CJK2yQEIpbbJAQjBtskBCKmdygEI153KAQjancoBCKijygE=\n" +
                "x-spf-previous: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/videos\n" +
                "x-spf-referer: https://www.youtube.com/channel/UC3qWcF49rv8VMZO7Vg6kj5w/videos\n" +
                "x-youtube-client-name: 1\n" +
                "x-youtube-client-version: 2.20180830\n" +
                "x-youtube-page-cl: 210947591\n" +
                "x-youtube-page-label: youtube.ytfe.desktop_20180829_6_RC1\n" +
                "x-youtube-utc-offset: 480\n" +
                "x-youtube-variants-checksum: d9c008758e968d12ab791a74d2abe05c");
        String jsonString = session.get("https://www.youtube.com/browse_ajax")
                .params(param2)
                .headers(headers2)
                .send()
                .readToText();
        System.out.println(jsonString);
        boolean end;
        int sum = 0;
        do {
            String continuation1 = getValueByKeyFromJson(jsonString, "continuation");
            String itct = getValueByKeyFromJson(jsonString, "clickTrackingParams");
            Map<String, String> param3 = new HashMap<>(3);
            param3.put("ctoken", continuation1);
            param3.put("continuation", continuation1);
            param3.put("itct", itct);
            boolean continueGet = true;
            if (continueGet) {
                jsonString = session.get("https://www.youtube.com/browse_ajax")
                        .headers(headers2)
                        .params(param3)
                        .send()
                        .readToText();
                sum = sum + 1;
                System.out.println(jsonString);

            } else {
                break;
            }
            end = isEnd(jsonString);

        } while (!end);

    }


    private static boolean isEnd(String json) {
        String gridContinuation = getValueByKeyFromJson(json, "gridContinuation");
        JSONArray continuations = JSON.parseObject(gridContinuation).getJSONArray("continuations");
        if (continuations == null) {
            return true;
        }
        return false;
    }
}

