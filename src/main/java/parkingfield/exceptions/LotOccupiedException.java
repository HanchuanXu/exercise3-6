package parkingfield.exceptions;

/**
 *
 */
public class LotOccupiedException extends Exception {

    public LotOccupiedException(int num) {
        super("编号为"+ num +"的车位已被其他车辆占用");
    }
}