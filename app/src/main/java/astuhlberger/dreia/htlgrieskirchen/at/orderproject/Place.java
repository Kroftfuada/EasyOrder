package astuhlberger.dreia.htlgrieskirchen.at.orderproject;

/**
 * Created by fabia_000 on 19.06.2016.
 */
public class Place {
    String name;
    double lat;
    double lng;
    String address;

    public Place() {
    }

    public Place(String name, double lat, double lng, String address) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Place: " + "name= " + name + ", lat= " + lat + ", lng= " + lng + ", address= " + address;
    }
}
