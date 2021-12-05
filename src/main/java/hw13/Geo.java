package hw13;

public class Geo {
    private float lat;
    private float lng;

    @Override
    public String toString() {
        return "Geo{" +
                "lat=" + lat +
                ", lng=" + lng +
                "}\n";
    }

    public Geo(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }


}
