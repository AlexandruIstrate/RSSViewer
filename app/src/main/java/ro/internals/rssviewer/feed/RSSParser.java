package ro.internals.rssviewer.feed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RSSParser {

    private static final String TAG = "RSSParser";

    private URL url;

    public RSSParser(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "RSSParser: URL is malformed: ", e);
        }
    }

    public RSSFeed readFeed() throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(url.openStream());
        document.getDocumentElement().normalize();

        Node channelNode = document.getElementsByTagName("channel").item(0);
        NodeList nodes = channelNode.getChildNodes(); // Nodes inside <channel>

        ArrayList<FeedEntry> feedEntries = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) { // For each <item> tag in <channel>
            Node node = nodes.item(i);

            if (node.getNodeName().equals("item") && node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                feedEntries.add(new FeedEntry(getNodeValue("title", element), getNodeValue("description", element), getNodeValue("link", element), downloadResizedImage(element.getElementsByTagName("media:thumbnail").item(0).getAttributes().getNamedItem("url").getTextContent(), 200)));
            }
        }

        Element channelElement = (Element) channelNode;
        return new RSSFeed(getNodeValue("title", channelElement), getNodeValue("link", channelElement), getNodeValue("description", channelElement), getNodeValue("language", channelElement), getNodeValue("copyright", channelElement), feedEntries, getFeedType());
    }

    private int getFeedType() {
        for (Map.Entry<Integer, String> entry : Feeds.getRssFeeds().entrySet()) {
            if (url.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return Feeds.UNDEFINED;
    }

    private String getNodeValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag);
        return nodes.item(0).getTextContent();
    }

    private Bitmap downloadIcon(String imageUrl) {
        Bitmap bitmap = null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();

            InputStream inputStream = con.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (MalformedURLException e) {
            Log.e(TAG, "downloadIcon: The image URL is malformed", e);
        } catch (IOException e) {
            Log.e(TAG, "downloadIcon: An IO Exception has occurred", e);
        }

        return bitmap;
    }

    private Bitmap getDefaultImage() {
        return BitmapFactory.decodeFile("Path to default RSS image");
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return getDefaultImage();
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    private Bitmap downloadResizedImage(String url, int width) {
        Bitmap original = downloadIcon(url);

        if (original != null)
            return resizeBitmap(original, width, (int) ((float) width / (float) original.getWidth() * original.getHeight())); // Casting to float helps the scale factor stay above 0
        else
            return null;
//            return getDefaultImage();
    }
}
