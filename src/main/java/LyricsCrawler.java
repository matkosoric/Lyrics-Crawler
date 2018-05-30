/*
 * Created by © Matko Soric.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LyricsCrawler {

    public static void main(String[] args) {

        List<String> songLinks = scrapeSongLinks("https://tekstovi.net/2,182,0.html");

        songLinks.forEach(LyricsCrawler::extractLyrics);

    }



    // skupljanje linkova na pjesme po izvođaču
    public static List<String> scrapeSongLinks (String urlIzvođača) {

        List <String> songLinks = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(urlIzvođača).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements linkovi = doc.getElementsByClass("artLyrList");
        for (Element element : linkovi) {
            songLinks.add(element.select("a").attr("abs:href"));
        }
        return songLinks;
    }


    // ekstrakcija teksta
    public static void extractLyrics (String urlPjesme) {

        Document doc = null;
        try {
            doc = Jsoup.connect(urlPjesme).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements izvodjacNaslovJsoup = doc.getElementsByClass("lyricCapt");
        String izvodjac = izvodjacNaslovJsoup.first().text();
        String naslov = izvodjacNaslovJsoup.last().text();
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

        Path lokacija = Paths.get("src/main/resources/" + izvodjac);
        try {
            Files.createDirectories(lokacija);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.write(Paths.get(lokacija +"/" + izvodjac + " - " + naslov + ".txt"),
                    (Iterable<String>) lyrics.stream().map(String::valueOf)::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
