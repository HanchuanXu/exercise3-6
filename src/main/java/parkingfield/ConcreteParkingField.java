package parkingfield;

import parkingfield.exceptions.*;

import java.util.*;


//A mutable class 一个停车场
public class ConcreteParkingField implements ParkingField {
	
	/*
	 * RI: 
	 * Name 不为空，长度大于0 
	 * lots中不存在重复  //此项已经通过Set保证
	 * lots.size() >= 5;
	 * lots.size() >= status.size();
	 * status中lot的width大于等于>=car的width
	 * status中car不为null 
	 * status中每个key均在lots中出现
	 * status中的values中不存在重复
	 * records中的每个value(record)，如果record.getTimeOut()为null，
		则<record.getLot(),record.getCar()>必定出现在status中
	 */

	// Abstraction function:
	// 代表着一个停车场，该停车场有lots.size()个车位
	// lots中每个元素lot代表着一个车位
	// lot上停的车是status.get(lot)
	// 该车场的所有停车记录是records，其中的元素record表示一个停车记录，表明：
	// 车辆record.getCar()在record.getTimeIn()时刻停到了record.getLot()车位上，在record.getTimeOut()离开，花费record.getFee()元
	
	//Rep
	private final String name; 		//停车场名称
	
	//包含的停车位对象列表，列表中元素的类型是Lot,不同停车位对象的编号不能相同
	private final Set<Lot> lots = new HashSet<>();

	//停车场当前状态，Key为一个Lot对象，Value为当前停在该Lot对象所代表的车位上的Car对象
	//且Key和Value的值均不能为null，意即:如果一个停车位上没有停车，则它不应包含在该map中
	private final Map<Lot, Car> status = new HashMap<>();
	
	//停车记录
	//为提升检索性能，后续可以考虑分当前在停车场车辆记录和历史记录两个Set
	//private final Map<Lot,Record> records = new HashMap<>(); 
	private final Set<Record> records = new HashSet<>();
	

	
	/**
	 * 创建一个停车场
	 * @param name  停车场名称，不能为空，长度大于0
	 * @param lotnos 各停车位编号，不重复
	 * @param width 车位宽度数组，不为空，数组长度大于等于5
	 * @throws IllegalArgumentException 停车位数量小于5或停车位宽度数量小于5或二者数量不一致，
	 *  存在重复的停车位编号
	 */
	public ConcreteParkingField(String name, int[] lotnos, int[] width) throws IllegalArgumentException{
		
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
		
		this.name = name;

		for (int i = 0; i < lotnos.length; i++) {
			Lot lot = new Lot(lotnos[i], width[i]);
			lots.add(lot);
		}
		checkRep();
	}
	
	@Override
	public Set<Integer> getLots(){
		Set<Integer> reSet=new HashSet<>();
		for (Lot lot:lots)
			reSet.add(lot.getNumber());
		return reSet;
	}

	@Override
	public int getLotWidth(int lotNo) throws NoSuchLotException {
		Lot lot = getLotByNo(lotNo);
		if (lot==null)
			throw new NoSuchLotException(lotNo);
		else
			return lot.getWidth();
	}


	@Override
	public String getPFName() {
		return this.name;
	}
	
	@Override
	public void parking(String plateNo, int width, int lotNo) throws LotOccupiedException, NoSuchLotException,
			LotTooNarrowException, CarAlreadyParkingException{

		Car car= getCarByNo(plateNo);
		if (status.containsValue(car))
			throw new CarAlreadyParkingException(plateNo);


		boolean findFlag = false;
		for (Lot lot : lots) {
			if (lot.getNumber() == lotNo) {
				if (lot.getWidth() < width)
					throw new LotTooNarrowException(plateNo, lotNo);
				if (status.containsKey(lot))
					throw new LotOccupiedException(lotNo);
				Car newCar=new Car(plateNo,width); //更新状态
				status.put(lot, newCar);
				Record record=new Record(newCar,lot); //更新记录
				records.add(record);
				findFlag = true;

				break;
			}
		}
		if (!findFlag)
			throw new NoSuchLotException(lotNo);
		checkRep();
	}

	@Override
	public void parking(String plateNo, int width)  throws ParkingFieldFullException, CarAlreadyParkingException {
		Car car= getCarByNo(plateNo);
		if (status.containsValue(car))
			throw new CarAlreadyParkingException(plateNo);
		
		boolean findFlag = false;
		for (Lot lot : lots) {
			if (status.containsKey(lot))  //occupied
				continue;
			else
				{
				if (lot.getWidth() < width)
					continue;
				else{
					Car newCar=new Car(plateNo,width);
					status.put(lot, newCar);
					findFlag = true;

					Record record=new Record(newCar,lot); //更新记录
					records.add(record);

					break;
				}
			}
		}
		
		if (!findFlag)
			throw new ParkingFieldFullException();		
		checkRep();
	}

	@Override
	public Record depart(String plateNo) throws CarNotInFieldException{
		Car car= getCarByNo(plateNo);
		if (!status.containsValue(car) || car==null)
			throw new CarNotInFieldException(plateNo);

		Lot toBeemptiedLot=null; //找到需要从status中删除的lot
		for(Lot lot:status.keySet()) { 
			if (status.get(lot).equals(car)) {
				toBeemptiedLot=lot;				
				break;
			}
		}
		
		//System.out.println("begin  "+status.keySet().toString());
		//找到需要返回的Record
		Record returnRecord=null;
		for(Record record:records) {
			if(record.getCarNo().equals(car.getPlateNo())
					&&record.getLotNo()==toBeemptiedLot.getNumber()
					&&record.getTimeOut()==null) {
				record.calcFee();
				try {
					returnRecord=(Record) record.clone(); 
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		status.remove(toBeemptiedLot);
		//System.out.println("end  "+status.keySet().toString());
		checkRep();
		return returnRecord;
	}

	@Override
	public Map<Integer, String> status () {
		Map<Integer, String> reMap=new HashMap<>();
		
		for(Lot lot: status.keySet()) {
			reMap.put(lot.getNumber(), status.get(lot)==null?"":status.get(lot).getPlateNo());
		}
		checkRep();
		return reMap;
	}

	@Override
	public int getNumberOfLots() {
		return lots.size();
	}

	@Override
	public boolean isLotInParkingField(int lotNo,int width) {
		for (Lot lot:lots)
			if((lot.getNumber() == lotNo) && (lot.getWidth() == width))
				return true;
		return false;
	}

	
	@Override
	public boolean isFull() {
		if(status.size()==lots.size())
			return true;	
		checkRep();
		return false;
	}
	
	@Override
	public int getOneFreeLot(int width) throws ParkingFieldFullException {

		//boolean findFlag=false;
		for (Lot lot:lots) {
			if (status.containsKey(lot))
				continue;
			else if (lot.getWidth()>=width) {
				return lot.getNumber();
			}			
		}

		throw new ParkingFieldFullException();
	}
	
	@Override
	public Set<Record> records(){
		return this.records;  //表示泄露
		/*  
		//解决表示泄露
		Set<Record> reSet=new HashSet<>();
		for(Record re:records) {
			try {
				reSet.add((Record)re.clone());
			} catch (CloneNotSupportedException e) {
				//e.printStackTrace();
			}
		}
		return reSet;  
		*/
	}

	/**
	 * helper类：根据车牌号获得Car对象
	 * @param plateNo 车牌号
	 * @return 返回车牌PlateNo对应的Car对象，如果不存在返回null
	 */
	private Car getCarByNo(String plateNo){
		for (Car car:status.values()){
			if (car.getPlateNo().equals(plateNo))
				return car;
		}
		return null;
	
	}

	/**
	 * helper类：根据车位编号获得Lot对象
	 * @param lotNo 车位号
	 * @return 返回LotNo对应的Lot对象，如果不存在返回null
	 */
	private Lot getLotByNo(int lotNo){
		for (Lot lot:lots){
			if (lot.getNumber()==lotNo)
				return lot;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Parking field " + this.name + " has total number of lots:" + lots.size() +"\n");
		sb.append("Now " + ((double)status.size()/lots.size())*100 + "% lots are occupied\n");
		//按照车位号排序,此处可用Lambda简化
		//SortedSet<Lot> sortedLots=new TreeSet<Lot>((Lot o1, Lot o2)->o1.getNumber()-o2.getNumber()); 
		SortedSet<Lot> sortedLots=new TreeSet<Lot>(new lotComparator()); 
		sortedLots.addAll(lots);
		for(Lot lot:sortedLots) {
			sb.append("Lot " + lot.getNumber() + " (" + lot.getWidth()+"): ");
			if (status.containsKey(lot)) 
				sb.append("Car " + status.get(lot).getPlateNo() +"\n");
			else 
				sb.append("Free\n");
		}
		checkRep();
		return sb.toString();
	}
	
	private void checkRep() {
		//Name 不为空，长度大于0 
		assert name != null;
		assert name.length() > 0;
		
		//lots中不存在重复  //此项已经通过Set保证
		
		//lots.size() >= 5;
		assert lots.size() >= 5;	
		
		//lots.size() >= status.size();
		assert lots.size() >= status.size();
		
		Set<Car> cars=new HashSet<>();
		for (Lot lot : status.keySet()) {
			assert lots.contains(lot);
			//status中car不为null 
			assert status.get(lot) != null;
			//status中lot的width大于等于>=car的width
			assert lot.getWidth() >= status.get(lot).getWidth();
			
			//status中的values中不存在重复
			assert !cars.contains(status.get(lot));
			cars.add(status.get(lot));
		}
	
		
		
		// records中的每个value(record)，如果record.getTimeOut()为null，
		//则<record.getLot(),record.getCar()>必定出现在status中
		
		for (Record record : records()) {
			if (record.getTimeOut() == null) {//没有离开
				assert status.containsKey(this.getLotByNo(record.getLotNo()));
				assert status.get(this.getLotByNo(record.getLotNo())).equals(this.getCarByNo(record.getCarNo()));
			}
		}
	}	
}

//车位比较器,用于toString方法中按照车位号升序排序用
class lotComparator implements Comparator<Lot>{ 
	
	public int compare(Lot o1,Lot o2){
		/*
		 升序写法
		< return -1
		= return 0
		> return 1
			
		*/
		
		//强制类型转换来调用函数里number变量，再来比较
		int number1=((Lot)o1).getNumber();
		int number2=((Lot)o2).getNumber();

		if(number1==number2)
			return 0;

		if(number1<number2){
			return -1;
		}
		else{
			return 1;
		}

	}
}

