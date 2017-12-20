package jessej.helsinkievents;

import java.util.ArrayList;

/**
 * Created by Jesse on 1.12.2017.
 */

public class Event {
    String id;

    // Sijainti karttaa varten haetaan tästä idstä
    String location_id;

    // Lisätietoa ja kuvia..
    String name;
    ArrayList<String> image_urls;
    String info_url;
    String description;
    String short_description;
    String start_time;
    String end_time;

    String time_range;

    double latitude;
    double longitude;


    public Event(String id, String location_id, String name, ArrayList<String> image_urls, String info_url, String short_description, String description, String start_time, String end_time, double latitude, double longitude) {
        this.id = id;
        this.location_id = location_id;
        this.name = name;
        this.image_urls = image_urls;
        this.info_url = info_url;
        this.description = description;
        this.short_description = short_description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.time_range = "";
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo_url() {
        return info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public ArrayList<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ArrayList<String> image_urls) {
        this.image_urls = image_urls;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTime_range() {
        if (getStart_time().length() != 0 && getEnd_time().length() != 0) {

            String start_day = start_time.substring(8, 10);
            String start_month = start_time.substring(5, 7);
            String start_year = start_time.substring(0, 4);

            String end_day = end_time.substring(8, 10);
            String end_month = end_time.substring(5, 7);
            String end_year = end_time.substring(0, 4);

            return start_day + "." + start_month + "." + start_year + " - " + end_day + "." + end_month + "." + end_year;

        } else {

            return "Aikaa ei asetettu";

        }
    }

    public String getFirstImgUrl() {
        if (image_urls.size() != 0) {
            return image_urls.get(0);
        } else {
            return null;
        }
    }

    public void setTime_range(String time_range) {
        this.time_range = time_range;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
