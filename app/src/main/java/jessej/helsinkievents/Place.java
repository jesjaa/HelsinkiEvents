package jessej.helsinkievents;

/**
 * Created by Jesse on 1.12.2017.
 */

public class Place {
    String id;
    String name;
    String postal_code;
    String street_address;
    String address_locality;
    String latitude;
    String longitude;
    String category;

    // Miksi longname ja longaddress?
    // niitä hyödynnämme filteröinialgoritmissä
    String longname;
    String longaddress;
    boolean selected;


    public Place(String id, String name, String postal_code, String street_address, String address_locality, String latitude, String longitude, String category) {
        this.id = id;
        this.name = name;
        this.postal_code = postal_code;
        this.street_address = street_address;
        this.address_locality = address_locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.selected = false;
        this.longname = name + " (" + street_address + ", " + postal_code + " " + address_locality + ")";
        this.longaddress = street_address + ", " + postal_code + " " + address_locality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getAddress_locality() {
        return address_locality;
    }

    public void setAddress_locality(String address_locality) {
        this.address_locality = address_locality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongname() {
        return this.longname;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getLongaddress() {
        return longaddress;
    }

    public void setLongaddress(String longaddress) {
        this.longaddress = longaddress;
    }

}
