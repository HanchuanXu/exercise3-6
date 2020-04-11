package parkingfield.exceptions;

public class LotTooNarrowException extends Exception {
    public LotTooNarrowException(String plateNo, int lotNo) {
        super("编号为" + lotNo + "的车位宽度小于" + plateNo + "的宽度，" + plateNo + "无法在此停车!");
    }
}