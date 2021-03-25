package com.test.tworldapplication.utils;

import android.app.Activity;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.test.tworldapplication.inter.SuccessNull;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamHelper {
    public static String object2XML(Object object) {
        XStream xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
        return xstream.toXML(object);
    }

    public static <T> T xml2Object(String xml, String root, Class<T> c) {
        XStream xstream = new XStream(new DomDriver());
        xstream.autodetectAnnotations(true);
        xstream.alias(root, c);
        return (T) xstream.fromXML(xml);
    }

    public static <T> T xml2Object(Context context, String root, String name, Class<T> c) {
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.autodetectAnnotations(true);
            xstream.alias(root, c);
            return (T) xstream.fromXML(context.getResources().getAssets().open(name));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parse(String xml, String keyWord) {
        try {
            return ((Element) DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)))
                    .getElementsByTagName(keyWord)
                    .item(0))
                    .getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}