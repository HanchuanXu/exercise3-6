package parkingfield.exceptions;

public class ParkingFieldFullException extends Exception {
    public ParkingFieldFullException() {
        super("此停车场无空闲车位！");
    }
}