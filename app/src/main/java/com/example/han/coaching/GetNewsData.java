package com.example.han.coaching;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by han on 2015-11-24.
 */
public class GetNewsData extends AsyncTask<Void,Void,Void> {


    NewsItem item;
    // �쎒�궗�씠�듃�뿉 �젒�냽�븷 二쇱냼
    String uri = "http://rss.hankyung.com/new/news_main.xml";
    // �쎒�궗�씠�듃�뿉 �젒�냽�쓣 �룄��二쇰뒗 �겢�옒�뒪
    URL url;
    // XML臾몄꽌�쓽 �궡�슜�쓣 �엫�떆濡� ���옣�븷 蹂��닔
    String tagname = "", title="", desc="", link ="", image = "";
    // �뜲�씠�꽣�쓽 �궡�슜�쓣 紐⑤몢 �씫�뼱�뱶�졇�뒗吏��뿉 ���븳 �젙蹂대�� ���옣
    Boolean flag = null;
    @Override
    protected void onPreExecute() {
        item = new NewsItem();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Boolean flag = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            url = new URL(uri);
            InputStream in = url.openStream();
            xpp.setInput(in, "utf-8");

            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT ) {
                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("item")){
                        flag = true;
                    }
                    tagname = xpp.getName();
                } else if(eventType == XmlPullParser.TEXT) {
                    if(flag){
                        if(tagname.equals("title")) title += xpp.getText();
                        else if (tagname.equals("link")) link += xpp.getText();
                        else if (tagname.equals("description")) desc += xpp.getText();
                        else if (tagname.equals("image")) image += xpp.getText();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    tagname = xpp.getName();
                    if(tagname.equals("item")) {

                        item.setTitle(title);
                        item.setDesc(desc);
                        item.setImageUrl(image);
                        item.setLink(link);

                        MainActivity.NewsNews.add(item);

                        title="";
                        desc="";
                        link="";
                        image="";
                    }
                }

                eventType = xpp.next();
            }

            flag = true;

        } catch(Exception e) {
            e.printStackTrace();
        }
        SaveData svData = new SaveData(MainActivity.mContext);
        svData.save("SharedNews");

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity.mHandler.sendEmptyMessage(0);

        super.onPostExecute(aVoid);
    }

}

