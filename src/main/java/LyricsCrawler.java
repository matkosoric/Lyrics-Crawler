/*
 * Created by © Matko Soric.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LyricsCrawler {

    public static void main(String[] args) {

        Document doc = null;

        try {
            doc = Jsoup.connect("https://tekstovi.net/2,1156,36784.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements naslovJsoup = doc.getElementsByClass("lyricCapt");

        Elements lyricsJsoup = doc.getElementsByClass("lyric");

        List<String> lyrics = new ArrayList<String>();

        for (Element element : lyricsJsoup) {

            lyrics.addAll(Arrays.asList(element.toString().substring(element.toString().indexOf(">")+1).split("<br>")));

        }

        lyrics.replaceAll(String::trim);

        for (int i = 0; i < lyrics.size(); i++) {
            if (lyrics.get(i).contains("</p>")) {
                lyrics.set(i, lyrics.get(i).replace("</p>", ""));
            }
        }

        lyrics.forEach(System.out::println);

    }
}
