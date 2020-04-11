package parkingfield;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecordTest {
//	//Car car
//	@Before
//	public void ini() {
//		Car car = new Car("HA12345", 150);
//		Lot lot = new Lot(12, 200);
//		Record record = new Record(car, lot);
//	}
    @Test
    public void testGetTimeIn() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        //System.out.println(record.toString());
        assertEquals(LocalDateTime.now(),record.getTimeIn());
    }

    @Test
    public void testSetTimeIn() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        record.setTimeIn(LocalDateTime.of(2020,3,20,23,48));
        //System.out.println(record.toString());
        assertEquals(LocalDateTime.of(2020,3,20,23,48),record.getTimeIn());
    }

    @Test
    public void testGetTimeOut() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        assertNull(record.getTimeOut()); //未设置的情况
        record.setTimeOut(LocalDateTime.of(2020,3,20,23,52));
        //System.out.println("testSetTimeOut  " + record.toString());
        assertEquals(LocalDateTime.of(2020,3,20,23,52),record.getTimeOut());
    }

    @Test
    public void testSetTimeOut() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        record.setTimeOut(LocalDateTime.of(2020,3,20,23,52));
        //System.out.println("testSetTimeOut  " + record.toString());
        assertEquals(LocalDateTime.of(2020,3,20,23,52),record.getTimeOut());
    }

    //测试时长不是半小时整数倍
    @Test
    public void testCalcFee1() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        record.setTimeIn(LocalDateTime.of(2020,3,20,20,51,1));
        //大于半小时
        record.setTimeOut(LocalDateTime.of(2020,3,20,23,52,1));
        System.out.println("testCalcFee  " + record.toString());
        double fee=record.calcFee();
        assertEquals(70.0,fee,0.0001);
        
        //小于半小时
        record.setTimeOut(LocalDateTime.of(2020,3,20,20,52,1));
        System.out.println("testCalcFee  " + record.toString());
        fee=record.calcFee();
        assertEquals(10.0,fee,0.0001);
    }

    //测试时长是半小时整数倍
    @Test
    public void testCalcFee2() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        record.setTimeIn(LocalDateTime.of(2020,3,20,22,52,1));
        record.setTimeOut(LocalDateTime.of(2020,3,20,23,52,1));
        //System.out.println("testCalcFee  " + record.toString());
        double fee=record.calcFee();
        assertEquals(20.0,fee,0.0001);

        //离入场时间相等
        record.setTimeOut(LocalDateTime.of(2020,3,20,22,52,1));
        fee=record.calcFee();
        assertEquals(0.0,fee,0.0001);
    }

    //测试异常情况：离场时间小于入场时间
    @Test
    public void testCalcFeeException() {
    	Car car=new Car("HA12345", 150);
    	Lot lot=new Lot(12, 200);
        Record record = new Record(car,lot);
        record.setTimeIn(LocalDateTime.of(2020,3,20,20,52));
        record.setTimeOut(LocalDateTime.of(2020,3,20,20,51));

        double fee=record.calcFee();
        assertEquals(-1,fee,0.0001);

    }
}
