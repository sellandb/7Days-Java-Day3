package com.sellandb;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sellandb on 11/4/14.
 */
public class Pages implements Iterable<Page> {
    private final int numPages;
    private final String xmlFile;

    public Pages(int pages, String location) {
        numPages = pages;
        xmlFile = location;
    }

    @Override
    public Iterator<Page> iterator() {
        //Setup variables
        CopyOnWriteArrayList<Page> pageList = new CopyOnWriteArrayList<Page>();
        File f; InputStream i; Long start; Long end;

        start = System.nanoTime();  //Start Timing


        //Get the file stream
        try {
            f = new File(xmlFile);
            i = new FileInputStream(f);

            try {

                //Creat the XML Parser
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLStreamReader parser = factory.createXMLStreamReader(i);

                Boolean inText = false;     //We will set this to true if we are in a text node
                String data = "";           //Text from within the node will go here
                int count = 0;
                //Iterate over the nodes within the parser
                for (int event = parser.next();
                     event != XMLStreamConstants.END_DOCUMENT && count < numPages;
                     event = parser.next()) {


                    //Switch based on the type of event fired
                    //Start Element - check if this is a text node and mark inText as needed
                    //End Element - check if this ends a text node and mark inText as needed
                    //Characters or CDATA - If we are in a text node, this is the data we care about
                    switch (event) {
                        case XMLStreamConstants.START_ELEMENT:
                            if (isText(parser.getLocalName())) {
                                inText = true;
                            }
                            break;
                        case XMLStreamConstants.END_ELEMENT:
                            if (isText(parser.getLocalName())) {
                                inText = false;
                            }
                            break;
                        case XMLStreamConstants.CHARACTERS:
                            if (inText)  data += parser.getText();
                            break;
                        case XMLStreamConstants.CDATA:
                            if (inText)  data += parser.getText();
                            break;
                    } // end switch

                    //Check if we have accumulated text from a node
                    //If data has a positive length and we are not in text mode
                    //We have finished accumulating text for a document
                    if(!inText && data.length() > 0) {

                        //Create a page
                        Page p = new Page(data);
                        pageList.add(p);

                        //Make sure we clear out our data
                        data = "";
                        count++;
                    }
                }
                parser.close();

            } catch (XMLStreamException e) {
                System.out.println(e.getMessage());
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        //Conclude timing and report
        end = System.nanoTime();
        System.out.println("Page processing took: " + (end - start)/1000000 + "ms");
        return pageList.iterator();


    }

    private static boolean isText(String name) {
        if (name.equals("text")) return true;
        return false;
    }
}
