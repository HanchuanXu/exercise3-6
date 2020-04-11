package parkingfield;

import java.time.Duration;
import java.time.LocalDateTime;

//An mutable class 停车记录
public class Record implements Cloneable{ 
	
	//Abstraction function:
	//代表一个停车记录record
	//车辆record.getCar()在record.getTimeIn()时刻停到了record.getLot()车位上，
	//在record.getTimeOut()离开，花费record.getFee()元

	// Representation invariant:
	// timeOut为null，或者 timeOut>=timeIn
	//

	// Safety from rep exposure:
	//   所有的表示都是private的，都是不可变数据类型，通过set和get控制安全性

	
	private final Car car;
	private final Lot lot;
	private LocalDateTime timeIn= LocalDateTime.now();
	private LocalDateTime timeOut = null;
	private double fee=0.0;


	public Record(Car car, Lot lot) {
		this.car = car;
		this.lot = lot;
	}

	public LocalDateTime getTimeOut() {
		return timeOut;
	}
	public LocalDateTime getTimeIn() {
		return timeIn;
	}
	
	//考虑到Record会提供给Client使用，此处只返回Lot号，不返回整个Lot
	public int  getLotNo() {
		return lot.getNumber();
	}
	
	//考虑到Record会提供给Client使用，此处只返回车牌号，不返回整个Car
	public String getCarNo() {
		return car.getPlateNo();
	}
	
	//获得fee
	public double getFee() {
		return fee;
	}
	
	
	void setFee(double fee) {
		this.fee=fee;
	}

	/**
	 * 指定离场时间为特定时间，用于测试
	 * @param timeOut 指定的离场时间
	 */
	void setTimeOut(LocalDateTime timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * 设置离场时间为当前时间
	 */
	void setTimeOut() {
		this.timeOut = LocalDateTime.now();
	}

	/**
	 * 设置入场时间为特定时间，用于测试
	 * @param timeIn 指定的入场时间
	 */
	void setTimeIn(LocalDateTime timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * 根据停车时间自动计费（每半小时10元，不足半小时按半小时计算）
	 * @return 停车费用; 如果timeOut<timeIn,返回-1
	 *
	 */
	double calcFee(){
		if (timeOut==null)  //如果未指定离场时间，则按照当前时间为准
			setTimeOut();

		Duration duration = Duration.between(timeIn,timeOut);
		if (duration.getSeconds()<0)  //如果离场时间小于入场时间，则返回-1。也可以采用抛出异常方式处理
			return -1;

		long seconds= duration.getSeconds();//相差的秒数
	
		double fee = 0.0;
		if (seconds % (60*30) ==0 )
			fee= 10*(seconds/(60*30)); //半小时的整数倍
		else
			fee= 10*(seconds/(60*30)+1);
		
		this.fee=fee;
		return fee;
	}

	@Override
	public String toString() {
		return "Record [car=" + car + ", lot=" + lot + ", timeIn=" + timeIn + ", timeOut=" + timeOut + ", fee=" + fee
				+ "]";
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		//Record中所有的field都是不可变的，浅copy即可
		return super.clone();
	}
	
}
