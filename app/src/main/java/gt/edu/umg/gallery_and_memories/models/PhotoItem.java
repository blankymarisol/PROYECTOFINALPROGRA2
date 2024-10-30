package gt.edu.umg.gallery_and_memories.models;

public class PhotoItem {
    //VARIABLES DE INSTANCIA
    private long id;
    private String uri;
    private String description;
    private String date;
    private double latitude;
    private double longitude;

    //CONSTRUCTOR
    public PhotoItem(long id, String uri, String description, String date, double latitude, double longitude) {
        this.id = id;
        this.uri = uri;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters (OBTENER)
    public long getId() { return id; }
    public String getUri() { return uri; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setters (MODIFICAR)
    public void setId(long id) { this.id = id; }
    public void setUri(String uri) { this.uri = uri; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}