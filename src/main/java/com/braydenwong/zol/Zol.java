package com.braydenwong.zol;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Braydenwong
 * @Date: 2018-07-27 11:07
 * @Description:java爬虫-中关村网站
 * @Version: 1.0
 */
public class Zol {
    public static void main(String[] args) {
        try {
            int num = 1;
            for (int a = 1; a <= 163; a++) {
                Document document = Jsoup.connect(String.format("http://detail.zol.com.cn/cell_phone_advSearch/subcate57_1_s1398-s7548-s7549-s7074-s6500-s6502_1_1__%s.html#showc", a)).timeout(300000).get();
                for (int i = 1; i <= 30; i++) {
                    Elements elements = document.select(formatString("#result_box > div.list_box > ul > li:nth-child(%s)>div.check_pic>a>img", i, 0));
                    System.out.println("第" + num + "条数据");
                    System.out.println(elements.attr("src"));
                    String title = document.select(String.format("#result_box > div.list_box > ul > li:nth-child(%s) > dl > dt", i)).select("a").text();
                    System.out.println(title);
                    String price = document.select(formatString("#result_box > div.list_box > ul > li:nth-child(%s) > div.date_price > span.price > b.price-type", i, 0)).text();
                    System.out.println(price);
                    for (int j = 1; j <= 3; j++) {
                        for (int k = 1; k <= 3; k++) {
                            Elements parms = document.select(String.format("#result_box > div.list_box > ul > li:nth-child(%s) > dl > dd:nth-child(2) > div > ul:nth-child(%s) > li:nth-child(%s)", i, j, k));
                            boolean flag = parms.hasAttr("title");
                            String parm;
                            if (!flag) {
                                parm = parms.select("p").text();
                            } else if (k == 1 && j == 1) {
                                parm = parms.text();
                            } else {
                                parm = parms.attr("title");
                            }
                            System.out.println(parm);
                        }
                    }
                    String string = document.select(String.format("#result_box > div.list_box > ul > li:nth-child(%s) > dl > dd:nth-child(4)", i)).text();
                    String comment = regexToString(string);
                    System.out.println(comment + "星");
                    System.out.println();
                    num++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String regexToString(String content) {
        String regex = "\\d+(\\.\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static String formatString(String string, int i, int flag) {
        if (flag == 0) {
            return String.format(string, i);
        } else if (flag == 2) {
            return String.format(string, i, i + 1, i);
        }
        return null;
    }
}
