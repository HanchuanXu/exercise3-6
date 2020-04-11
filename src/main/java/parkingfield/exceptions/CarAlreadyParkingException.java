package parkingfield.exceptions;

public class CarAlreadyParkingException extends Exception {
    public CarAlreadyParkingException(String plateNo) {
        super("车辆" + plateNo + "已停在该停车场且没有驶离，不能再次停车。");
    }
}
