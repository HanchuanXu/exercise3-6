package parkingfield.exceptions;

public class NoSuchLotException extends Exception {

    public NoSuchLotException(int num ) {
        super("该停车场不存在编号为"+ num + "的车位");
    }
}