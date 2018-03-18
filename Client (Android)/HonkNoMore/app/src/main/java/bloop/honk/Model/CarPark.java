package bloop.honk.Model;


public class CarPark {
    private String Agency;
    private String Area;
    private int AvailableLots;
    private String CarParkID;
    private String Development;
    private String Location;
    private String LotType;

    public CarPark() {}

    public CarPark(String agency, String area, int availableLots, String carParkID, String development, String location, String lotType) {
        this.Agency = agency;
        this.Area = area;
        this.AvailableLots = availableLots;
        this.CarParkID = carParkID;
        this.Development = development;
        this.Location = location;
        this.LotType = lotType;
    }

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public void setArea(String area) {
        Area = area;
    }

    public void setAvailableLots(int availableLots) {
        AvailableLots = availableLots;
    }

    public void setCarParkID(String carParkID) {
        CarParkID = carParkID;
    }

    public void setDevelopment(String development) {
        Development = development;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setLotType(String lotType) {
        LotType = lotType;
    }

    public String getArea() {
        return Area;
    }

    public int getAvailableLots() {
        return AvailableLots;
    }

    public String getCarParkID() {
        return CarParkID;
    }

    public String getDevelopment() {
        return Development;
    }

    public String getLocation() {
        return Location;
    }

    public String getLotType() {
        return LotType;
    }
}
