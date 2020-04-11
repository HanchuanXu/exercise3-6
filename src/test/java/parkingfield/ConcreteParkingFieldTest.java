package parkingfield;

import static org.junit.Assert.*;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import parkingfield.exceptions.CarAlreadyParkingException;
import parkingfield.exceptions.CarNotInFieldException;
import parkingfield.exceptions.LotOccupiedException;
import parkingfield.exceptions.LotTooNarrowException;
import parkingfield.exceptions.NoSuchLotException;
import parkingfield.exceptions.ParkingFieldFullException;


public class ConcreteParkingFieldTest {

	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	
	
	//测试name是否正常，测试停车位创建是否正常
	@Test
	public void testConstructor() throws NoSuchLotException {
		String pfName="A1";
		final int width[]= {200,180,200,170,190};
		final int lotnos[]={1,2,3,4,5};
		ParkingField pf = new ConcreteParkingField(pfName,lotnos,width);
		
		//1.测试name
		assertEquals("A1",pf.getPFName());
		
		//2.测试停车位生成是否正确
		assertEquals(5,pf.getLots().size());//数量是否正确 
		
		for (int lotno: lotnos) {  //每个给定的车位编号，都生成了车位
			assertTrue(pf.getLots().contains(lotno));
		}
		
		for(int i=0;i<lotnos.length;i++) {  //车位的宽度同车位编号对应一致
			assertEquals(width[i], pf.getLotWidth(lotnos[i]));
		}
	}
	
	/*
	//测试停车位数目异常
	@Test
	public void testCreateWithWrongNumbersOfLot() {
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("输入数组长度应大于等于5!");
		String pfName="A1";
		final int width[]= {200,180,200,170,190};
		final int lotnos[]={1,2,3,4};
		ParkingField pf = new ConcreteParkingField(pfName,lotnos,width);
	}
	
	// 测试车位宽度数目异常
	@Test
	public void testCreateWithWrongNumbersOfWidth() {
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("输入数组长度应大于等于5!");
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170};
		final int lotnos[] = { 1, 2, 3, 4,5};
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
	}
	
	// 测试车位宽度数目和车位编号个数不一致异常
	@Test
	public void testCreateWithUnequalNumbers() {
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("停车位编号数目应同宽度数目一致!");
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220, 230 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
	}
			
	// 测试车位编号重复
	@Test
	public void testCreateWithDuplicatedLotNo() {
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("停车位编号中存在重复!");
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 4 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
	}
	
	**/
	
	
	/*
	 * parking(String plateNo, int width, int lotNo) 测试策略： 
	 * 1.测试正常可停放情况； 
	 * 2.测试@throws LotOccupiedException 如果该停车位已被其他车辆占用
	 * 3.测试@throws NoSuchLotException 如果该停车场没有该编号的停车位 
	 * 4.测试@throws LotTooNarrowException 如果该停车位的宽度小于该车辆的宽度 
	 * 5.测试@throws CarAlreadyParkingException 如果该车已经停在该停车场
	 */

	// 1.测试正常可停放情况；
	@Test
	public void testParkingWithSpecificLot()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int lotNo1 = 1;
		int lotNo2 = 2;

		pf.parking(plateNo1, widthOfCar1, lotNo1);
		pf.parking(plateNo2, widthOfCar2, lotNo2);
		Map<Integer, String> status = pf.status();
		assertTrue(status.get(lotNo1).equals(plateNo1));
		assertTrue(status.get(lotNo2).equals(plateNo2));
	}

	// 2.测试@throws LotOccupiedException 如果该停车位已被其他车辆占用
	@Test
	public void testParkingWithSpecificLotOccupiedException()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int lotNo = 1;

		expectedEx.expect(LotOccupiedException.class);
		expectedEx.expectMessage("编号为" + lotNo + "的车位已被其他车辆占用");

		pf.parking(plateNo1, widthOfCar1, lotNo);
		pf.parking(plateNo2, widthOfCar2, lotNo);
	}

	// 3.测试@throws NoSuchLotException 如果该停车场没有该编号的停车位
	@Test
	public void testParkingWithSpecificLotNoSuchLotException()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		// String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		// int widthOfCar2=170;
		int lotNo = 6;

		expectedEx.expect(NoSuchLotException.class);
		expectedEx.expectMessage("该停车场不存在编号为" + lotNo + "的车位");

		pf.parking(plateNo1, widthOfCar1, lotNo);
	}

	// 4.测试@throws LotTooNarrowException 如果该停车位的宽度小于该车辆的宽度
	@Test
	public void testParkingWithSpecificLotTooNarrowException()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		// String plateNo2 = "CD002";
		int widthOfCar1 = 210;
		// int widthOfCar2=170;
		int lotNo = 1;

		expectedEx.expect(LotTooNarrowException.class);
		expectedEx.expectMessage("编号为" + lotNo + "的车位宽度小于" + plateNo1 + "的宽度，" + plateNo1 + "无法在此停车!");

		pf.parking(plateNo1, widthOfCar1, lotNo);
	}

	// 5.测试@throws CarAlreadyParkingException 如果该车已经停在该停车场
	@Test
	public void testParkingWithSpecificLotCarAlreadyParkingException()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		// String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		// int widthOfCar2=170;
		int lotNo = 1;

		expectedEx.expect(CarAlreadyParkingException.class);
		expectedEx.expectMessage("车辆" + plateNo1 + "已停在该停车场且没有驶离，不能再次停车。");

		pf.parking(plateNo1, widthOfCar1, lotNo);
		pf.parking(plateNo1, widthOfCar1, lotNo);
	}
	
	/*
	 * parking(String plateNo, int width) 测试策略： 
	 * 1.测试正常可停放情况； 
	 * 2.测试 @throws ParkingFieldFullException 若该停车场已满 (不存在空闲停车位)；
	 * 3.测试 @throws ParkingFieldFullException 有空闲停车位，但它们的宽度均小于该车辆的宽度 
	 * 4.测试 @throws CarAlreadyParkingException 如果该车当前已经停在该停车场
	 */
	
	// 1.测试正常可停放情况；
	@Test
	public void testParkingWithAutoAssign() throws ParkingFieldFullException, CarAlreadyParkingException {
		//生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		
		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		
		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		Map<Integer, String> status = pf.status();
		assertTrue(status.containsValue(plateNo1));
		assertTrue(status.containsValue(plateNo2));
	}
	
	// 2.测试 @throws ParkingFieldFullException 若该停车场已满 (不存在空闲停车位)；
	@Test
	public void testParkingWithAutoAssignParkingFieldFullException()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException, ParkingFieldFullException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		String plateNo3 = "AA101";
		String plateNo4 = "CD012";
		String plateNo5 = "DD012";
		String plateNo6 = "42012";

		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int widthOfCar3 = 170;
		int widthOfCar4 = 160;
		int widthOfCar5 = 170;
		int widthOfCar6 = 200;

		expectedEx.expect(ParkingFieldFullException.class);
		expectedEx.expectMessage("此停车场无空闲车位！");

		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		pf.parking(plateNo3, widthOfCar3);
		pf.parking(plateNo4, widthOfCar4);
		pf.parking(plateNo5, widthOfCar5);
		pf.parking(plateNo6, widthOfCar6);
		
	}
	
	// 3.测试 @throws ParkingFieldFullException 有空闲停车位，但它们的宽度均小于该车辆的宽度
	@Test
	public void testParkingWithAutoAssignParkingFieldFullException2() throws LotOccupiedException, NoSuchLotException,
			LotTooNarrowException, CarAlreadyParkingException, ParkingFieldFullException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		String plateNo3 = "AA101";

		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int widthOfCar3 = 250;

		expectedEx.expect(ParkingFieldFullException.class);
		expectedEx.expectMessage("此停车场无空闲车位！");

		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		pf.parking(plateNo3, widthOfCar3);

	}

	// 4.测试 @throws CarAlreadyParkingException 如果该车当前已经停在该停车场
	@Test
	public void testParkingWithAutoAssignCarAlreadyParkingException() throws LotOccupiedException, NoSuchLotException,
			LotTooNarrowException, CarAlreadyParkingException, ParkingFieldFullException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";

		int widthOfCar1 = 190;

		expectedEx.expect(CarAlreadyParkingException.class);
		expectedEx.expectMessage("车辆" + plateNo1 + "已停在该停车场且没有驶离，不能再次停车。");

		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo1, widthOfCar1);

	}
	
	
	
	/*
	 * status()测试策略： 
	 * 1.所有车位为空； 
	 * 2.>=1个车位有车停放.
	 */
	@Test
	public void testStatus()
			throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		Map<Integer, String> status = pf.status();

		// 1.所有车位为空
		assertEquals(0, status.size());

		// 2.>=1个车位有车停放
		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int lotNo1 = 1;
		int lotNo2 = 2;

		pf.parking(plateNo1, widthOfCar1, lotNo1);
		pf.parking(plateNo2, widthOfCar2, lotNo2);

		status = pf.status();
		assertTrue(status.get(lotNo1).equals(plateNo1));
		assertTrue(status.get(lotNo2).equals(plateNo2));
	}
	
	

	/*
	 *depart() 测试策略： 
	 * 1.正常驶离； 
	 * 2.车不在停车场内
	 */
	
	@Test
	public void testDepart() throws LotOccupiedException, NoSuchLotException, LotTooNarrowException, CarAlreadyParkingException, CarNotInFieldException, InterruptedException {
		// 生成1个停车场
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		
		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int lotNo1 = 1;
		int lotNo2 = 2;

		pf.parking(plateNo1, widthOfCar1, lotNo1);
		pf.parking(plateNo2, widthOfCar2, lotNo2);
		
		Map<Integer, String> status = pf.status();  //确保停入成功
		assertTrue(status.get(lotNo1).equals(plateNo1));
		assertTrue(status.get(lotNo2).equals(plateNo2));
		
		TimeUnit.SECONDS.sleep(3);  //根据需要执行若干时间 
		//TimeUnit.HOURS.sleep(1)
		Double fee=pf.depart(plateNo1).getFee();
		status = pf.status();		
		assertFalse(status.containsValue(plateNo1)); //不在停车场
		assertEquals(10.0,fee,0.0001);//价格正确
	}
	
	@Test
	public void testGetNumberOfLots() {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		assertEquals(lotnos.length, pf.getNumberOfLots());
	}
	
	@Test
	public void testIsLotInParkingField() {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		assertTrue(pf.isLotInParkingField(lotnos[2], width[2]));
		assertFalse(pf.isLotInParkingField(lotnos[1], width[4]));
	}
	
	@Test
	public void testIsFull() throws ParkingFieldFullException, CarAlreadyParkingException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		String plateNo3 = "AA101";
		String plateNo4 = "CD012";
		String plateNo5 = "DD012";

		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		int widthOfCar3 = 170;
		int widthOfCar4 = 160;
		int widthOfCar5 = 170;

		// 1.未停满
		assertFalse(pf.isFull());

		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		pf.parking(plateNo3, widthOfCar3);
		pf.parking(plateNo4, widthOfCar4);

		assertFalse(pf.isFull());

		// 2.停满
		pf.parking(plateNo5, widthOfCar5);
		assertTrue(pf.isFull());
	}

	@Test
	public void testGetOneFreeLot() throws ParkingFieldFullException, NoSuchLotException, CarAlreadyParkingException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;

		// 1.没有任何车辆停靠时
		Map<Integer, String> status = pf.status();
		int lotNo = pf.getOneFreeLot(180);
		assertTrue(pf.getLots().contains(lotNo)); // 是存在的Lot
		assertTrue(pf.getLotWidth(lotNo) >= 180); // 尺寸大于180
		assertFalse(status.containsKey(lotNo)); // 空的

		// 2.已经存在车辆
		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);

		status = pf.status();
		lotNo = pf.getOneFreeLot(180);
		assertTrue(pf.getLots().contains(lotNo)); // 是存在的Lot
		assertTrue(pf.getLotWidth(lotNo) >= 180); // 尺寸大于180
		assertFalse(status.containsKey(lotNo)); // 空的
	}

	// 测试 @throws ParkingFieldFullException 不存在满足要求的车位
	@Test
	public void testGetOneFreeLotException()
			throws ParkingFieldFullException, NoSuchLotException, CarAlreadyParkingException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);

		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;

		expectedEx.expect(ParkingFieldFullException.class);

		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		pf.getOneFreeLot(250);

	}
	
	@Test
	public void testGetPFName() {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		assertEquals(pfName, pf.getPFName());
		
	}
	
	@Test
	public void testGetLots() {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		assertEquals(lotnos.length, pf.getLots().size()); //the same size
		
		for(int lotNo:lotnos)
			assertTrue(pf.getLots().contains(lotNo));
		
	}
	
	@Test
	public void testLotWidth() throws NoSuchLotException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		
		for(int i=0;i<lotnos.length;i++)
			assertEquals(width[i], pf.getLotWidth(lotnos[i])); 		
	}
	
	
	@Test
	public void testLotWidthException() throws NoSuchLotException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		
		expectedEx.expect(NoSuchLotException.class);
		pf.getLotWidth(6);
	}
	
	@Test
	public void testToString() throws ParkingFieldFullException, CarAlreadyParkingException {
		String pfName = "A1";
		final int width[] = { 200, 180, 200, 170, 220 };
		final int lotnos[] = { 1, 2, 3, 4, 5 };
		ParkingField pf = new ConcreteParkingField(pfName, lotnos, width);
		
		// 生成一些car
		String plateNo1 = "AB001";
		String plateNo2 = "CD002";
		int widthOfCar1 = 190;
		int widthOfCar2 = 170;
		
		pf.parking(plateNo1, widthOfCar1);
		pf.parking(plateNo2, widthOfCar2);
		
		System.out.println(pf.toString());
		}
	
}
