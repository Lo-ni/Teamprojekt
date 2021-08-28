package application.android.com.watchthewatchers.IOFormat;

import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import application.android.com.watchthewatchers.Core.SurveillanceCam;

public class XmlToSurveillanceCamParser {


    public static ArrayList<SurveillanceCam> parseDocument(File file){

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<SurveillanceCam> sub = new ArrayList<>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

            if(nodeList.item(i).getNodeName().equals("node"))
            {
                //Parsen der Nodes des XML zum SurveillanceCam Objekt
                NamedNodeMap usr = nodeList.item(i).getAttributes();
                String id = usr.getNamedItem("id").getNodeValue();
                String lat = usr.getNamedItem("lat").getNodeValue();
                String lon = usr.getNamedItem("lon").getNodeValue();

                SurveillanceCam scam = new SurveillanceCam(lat, lon);
                Log.d("","lat:   "+ lat );

                Log.d("","pos:   "+ scam.getGpsPosition().getLatitude() );

                scam.getGpsPosition().getLatitude();

                scam.setId(id);

                NodeList innerNodeList = nodeList.item(i).getChildNodes();
                for (int k = 0; k < innerNodeList.getLength(); k++) {

                    /*
                    Node node = innerNodeList.item(k);
                    if(node.getNodeName().equals("tag")){
                        String s=  node.getAttributes().getNamedItem("k").getNodeValue();
                        switch (s){
                            case "surveillance":
                                break;
                            case "f":
                                break;
                            case "g":
                                break;
                    }
                    */

                    //String key =   node.getAttributes().getNamedItem("k").getNodeValue();
                    //String value =   node.getAttributes().getNamedItem("v").getNodeValue();
                }
                sub.add(scam);
            }
        }
        return sub;
    }

}
