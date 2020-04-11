package parkingfield;

//An immutable class 一辆车
class Car {
	//Rep:
	private final String plateNo; // 车牌号
    private final int width; //车辆宽度，自然数 (度量单位为厘米)，>100
    
    //AF:
    //车辆由唯一的车牌号和宽度值表示 
    
    // Representation invariant:
    // 车牌号不为空，长度大于0
    // 车辆宽度>100
    
    // Safety from rep exposure:
    //   All fields are private and final,no exposure
    
    
    public Car(String plateNo, int width) {  // 构造器
        this.plateNo = plateNo;
        this.width = width;
        checkRep();
    }

    public String getPlateNo() {   //返回车牌号
        return plateNo;
    }

    public int getWidth() {  //返回宽度
        return width;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (plateNo == null) {
			if (other.plateNo != null)
				return false;
		} else if (!plateNo.equals(other.plateNo))
			return false;
		return true; //优化点：return plateNo.equals(other.plateNo)
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plateNo == null) ? 0 : plateNo.hashCode());
		return result;
	}

	@Override  //利用IDE工具自动生成
	public String toString() {
		return "Car [plateNo=" + plateNo + ", width=" + width + "]";
	}
	
	private void checkRep() {
		assert plateNo!=null;
		assert plateNo.length()>0;
		assert width>100;
	}
}
