package com.braydenwong.douban;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Author: Braydenwong
 * @Date: 2018-07-31 20:28
 */
public class DouBanMovie {
    static int num = 1;

    public static void main(String[] args) throws IOException {

        a:
        for (int i = 0; i < 100000000; i++) {
            Document document = Jsoup.connect(String.format("https://movie.douban.com/subject/27605698/comments?start=%d&limit=20&sort=time&status=P", i)).get();
            System.out.println(String.format("https://movie.douban.com/subject/27605698/comments?start=%d&limit=20&sort=time&status=P", i));
            Element comments = document.getElementById("comments");
            if (comments == null) {
                System.out.println(document);
            }
            Elements elementsByClass = document.getElementsByClass("comment-item");
            for (Element element : elementsByClass) {
                System.out.println("第" + num + "条数据");
                String username = element.select("a").attr("title");
                String userPhoto = element.select("a>img").attr("src");
                System.out.println(username + " " + userPhoto);
                System.out.println(element.getElementsByClass("comment-info").select("span").attr("title"));
                System.out.println(element.getElementsByClass("comment-time").attr("title"));
                System.out.println(element.getElementsByClass("short").text());
                System.out.println();
                num++;
                if (comments.getElementsByClass("comment-item").size() < 20) {
                    break a;
                }
            }
        }
    }
}
