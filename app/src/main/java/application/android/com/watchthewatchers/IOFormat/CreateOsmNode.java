package application.android.com.watchthewatchers.IOFormat;

import android.content.Context;
import android.location.Location;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import application.android.com.watchthewatchers.Core.SurveillanceCam;
import cz.msebera.android.httpclient.client.HttpResponseException;

public class CreateOsmNode {

     public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

     public static Location createLocation(String s){

        Location location = null;
        try{
            String[] latlong =  s.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return location;
    }

    public static void writeFile(Context context, Node src, String name)
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        File file = new File(context.getFilesDir(), name);
        DOMSource source = new DOMSource(src);
        StreamResult result = new StreamResult(file);

        // Output to console for testing
        try {
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Erstellen des XML Files aus der Eingabe des Nutzers um dieses an den OSM Server hochzuladen
    public static void createOsmFile(String changesetID, SurveillanceCam scam, Context context) throws HttpResponseException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("osm");
        doc.appendChild(rootElement);


        // staff elements
        Element node = doc.createElement("node");
        rootElement.appendChild(node);


        // set attribute to node element
        Attr id = doc.createAttribute("id");
        id.setValue("-1");
        node.setAttributeNode(id);

        if(changesetID != null){
            // set attribute to node element
            Attr changeset = doc.createAttribute("changeset");
            changeset.setValue(changesetID);
            node.setAttributeNode(changeset);
        }else
            throw new HttpResponseException(0,"changeset not available");


        Attr version = doc.createAttribute("version");
        version.setValue("1");
        node.setAttributeNode(version);

        Attr lat = doc.createAttribute("lat");
        lat.setValue(scam.getGpsPosition().getLatitude()+"");
        node.setAttributeNode(lat);

        Attr lon = doc.createAttribute("lon");
        lon.setValue(scam.getGpsPosition().getLongitude()+"");
        node.setAttributeNode(lon);

        //-------------------------------------------------------
        Element tag = doc.createElement("tag");
        node.appendChild(tag);

        Attr k = doc.createAttribute("k");
        k.setValue("man_made");
        tag.setAttributeNode(k);

        Attr v = doc.createAttribute("v");
        v.setValue("surveillance");
        tag.setAttributeNode(v);

        //--------------------------------------------------------

        Element watcher = doc.createElement("tag");
        node.appendChild(watcher);

        Attr surveillance = doc.createAttribute("k");
        surveillance.setValue("surveillance");
        watcher.setAttributeNode(surveillance);

        Attr watcher_type = doc.createAttribute("v");
        watcher_type.setValue(scam.getWatcher_type());
        watcher.setAttributeNode(watcher_type);

        //--------------------------------------------------------
/*

        Element camera = doc.createElement("tag");
        node.appendChild(camera);

        Attr key = doc.createAttribute("k");
        key.setArmor("camera:type");
        camera.setAttributeNode(key);

        Attr val = doc.createAttribute("v");
        val.setArmor(scam.getCamera_type());
        camera.setAttributeNode(val);

        */

        //--------------------------------------------------------

        /*
        String name = nameField.getText().toString();
        scam.setId(name);

        Element nameElement = doc.createElement("tag");
        node.appendChild(nameElement);

        Attr namekey = doc.createAttribute("k");
        namekey.setArmor("name");
        nameElement.setAttributeNode(namekey);

        Attr nameval = doc.createAttribute("v");
        nameval.setArmor(scam.getName());
        nameElement.setAttributeNode(nameval);

        */


        //--------------------------------------------------------

/*
        String operator = operatorField.getText().toString();
        scam.setOperator(operator);

        Element opsElement = doc.createElement("tag");
        node.appendChild(opsElement);

        Attr opskey = doc.createAttribute("k");
        opskey.setArmor("operator");
        opsElement.setAttributeNode(opskey);

        Attr opsval = doc.createAttribute("v");
        opsval.setArmor(scam.getOperator());
        opsElement.setAttributeNode(opsval);

*/

        // write the content into xml file
        writeFile(context, doc, "node.xml");

    }



    //Erstellen des XML Files aus der Eingabe des Nutzers um dieses an den OSM Server hochzuladen
    public static void createChangesetFile(Context context) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("osm");
        doc.appendChild(rootElement);

        Element changeset = doc.createElement("changeset");
        rootElement.appendChild(changeset);

        Element tag = doc.createElement("tag");
        changeset.appendChild(tag);


        // set attribute to node element
        Attr cs1_0 = doc.createAttribute("k");
        cs1_0.setValue("created_by");
        tag.setAttributeNode(cs1_0);

        // set attribute to node element
        Attr cs1_1 = doc.createAttribute("v");
        cs1_1.setValue("Buddabread");
        tag.setAttributeNode(cs1_1);

        // write the content into xml file
        writeFile(context, doc, "changeset.xml");
    }

}

