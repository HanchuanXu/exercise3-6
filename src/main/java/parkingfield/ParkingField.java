package parkingfield;

import parkingfield.exceptions.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A mutable ADT  停车场
 */
public interface ParkingField {

	/**
	 * 创建一个停车场
	 *  @param name 停车场名字,不能为空，长度大于0
	 *  @param lotnos 各停车位编号，不重复
	 *  @param width 各停车位的宽度，数量大于等于5
	 *  @throws IllegalArgumentException 停车位数量小于5或停车位宽度数量小于5或二者数量不一致，
	 *  存在重复的停车位编号
	 *  @return 返回停车场对象
	 */
	static ParkingField create(String name, int[] lotnos, int[] width) throws IllegalArgumentException{
		if ( width.length < 5 || lotnos.length<5)
			throw new IllegalArgumentException("输入数组长度应大于等于5!");

		if (width.length!=lotnos.length) {
			throw new IllegalArgumentException("停车位编号数目应同宽度数目一致!");
		}
		
		Set<Integer> testDuplicationLotNo = new HashSet<>();
		for(int lotno:lotnos)
			testDuplicationLotNo.add(lotno);
		if (testDuplicationLotNo.size()!=lotnos.length)
			throw new IllegalArgumentException("停车位编号中存在重复!");
		
		return new ConcreteParkingField(name, lotnos, width);
	}


	/**
	 * 在某个指定停车位上停车
	 * 车牌号为plateNo的车辆，之前没停在车场，执行后停在了车位号为lotNo的车位上，该车位宽度大于车宽度
	 * 其他车位的状态不变
	 *
	 * @param plateNo 要停进来的车辆车牌号，not null
	 * @param width  车的宽度，自然数
	 * @param lotNo  指定的停车位编号，自然数
	 * @throws LotOccupiedException 如果该停车位已被其他车辆占用
	 * @throws NoSuchLotException 如果该停车场没有该编号的停车位
	 * @throws LotTooNarrowException 如果该停车位的宽度小于该车辆的宽度
	 * @throws CarAlreadyParkingException 如果该车已经停在该停车场
	 */
	void parking(String plateNo, int width, int lotNo) throws LotOccupiedException, NoSuchLotException,
			LotTooNarrowException, CarAlreadyParkingException;


	/**
	 * 在停车场停车，自动分配空闲停车位
	 * 车牌号为plate的车辆，之前没停在车场，执行后停在了一个之前空闲的车位上，该车位宽度大于车宽度
	 * 其他车位的状态不变
	 *
	 * @param plateNo 要停进来的车辆车牌号
	 * @param width  车的宽度，自然数
	 * @throws ParkingFieldFullException 若该停车场已满 (不存在空闲停车位)；或有空闲停车位，但
	 * 它们的宽度均小于该车辆的宽度
	 * @throws CarAlreadyParkingException 如果该车当前已经停在该停车场
	 */
	void parking(String plateNo, int width)  throws ParkingFieldFullException, CarAlreadyParkingException;


	/**
	 * 将汽车驶离停车场，plateNo车原来占用的车位空出来了，计算出本次停车的费用（半小时10元，不足半小时按半小时计算）
	 * @param plateNo 待驶离车辆的车牌，not null
	 * @return 本次停车的费用（精确计算得到）
	 * @throws CarNotInFieldException 如果该车辆当前并未停在该停车场内
	 */
	Record depart(String plateNo) throws CarNotInFieldException;
	

	/**
	 * 返回当前停车场各车位的状态
	 * @return Key 为停车位的编号，Value为该车位上的车辆车牌号。如果停车位上无车辆，则对应的Value为“”
	 */
	Map<Integer, String> status();

	/**
	 * 获得停车场中总的车位数目
	 * @return 车场中车位数目
	 */
	int getNumberOfLots();

	/**
	 * 查看停车场中是否存在指定车位号和宽度的车位
	 * @param lotNo  指定的停车位编号，自然数
	 * @param width  指定的停车位宽度，自然数
	 * @return true如果存在；false如果不存在
	 */
	boolean isLotInParkingField(int lotNo,int width);

	
	/**
	 * 当前停车场是否已满
	 * @return true：已满；false：尚有空闲停车位
	 */
	boolean isFull ();

	/**
	 * 获得宽度大于指定宽度的停车位
	 * @param width 停车位的宽度大于此值，等于零的整数
	 * @return 如果存在宽度大于等于width的空闲停车位，返回其编号
	 * @throws  ParkingFieldFullException 不存在满足要求的车位
	 */
	int getOneFreeLot(int width) throws ParkingFieldFullException;


	/**
	 * 返回停车场的名字
	 * @return 停车场名字
	 */
	String getPFName();
	
	/**
	 * 返回停车场停车位的编号列表
	 * @return 停车场车位编号清单
	 */
	Set<Integer> getLots();

	/**
	 * 返回某车位宽度
	 * @param lotNo 车位号
	 * @return 该车位宽度
	 * @throws NoSuchLotException 如果该停车场没有该编号的停车位
	 */
	int getLotWidth(int lotNo) throws NoSuchLotException;

	/**
	 * 获取停车记录
	 * @return 返回停车记录
	 */
	Set<Record> records();
}
