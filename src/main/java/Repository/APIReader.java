package Repository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class APIReader {
    private static APIReader instance;

    private APIReader() {}

    public static APIReader getInstance(){
        if(instance == null)
            instance = new APIReader();
        return instance;
    }

    public String getRestaurantsFromAPI() throws IOException {
        URL url = new URL("http://138.197.181.131:8080/restaurants");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        String restaurantsInfo = "";
        if(responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        else {
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                restaurantsInfo += sc.nextLine();
            }
            sc.close();
            return restaurantsInfo;
        }
    }

    public String getDeliveriesFromAPI() throws IOException {
        URL url = new URL("http://138.197.181.131:8080/deliveries");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        String deliveriesInfo = "";
        if(responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        else {
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext()) {
                deliveriesInfo += sc.nextLine();
            }
            sc.close();
            return deliveriesInfo;
        }
    }

}