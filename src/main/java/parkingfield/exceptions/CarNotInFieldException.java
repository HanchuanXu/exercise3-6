package parkingfield.exceptions;

public class CarNotInFieldException extends Exception{
	public CarNotInFieldException(String plateNo) {
        super("车" + plateNo + "在停车场中不存在！");
    }
}
