package parkingfield;

import java.util.Scanner;

import parkingfield.exceptions.CarAlreadyParkingException;
import parkingfield.exceptions.CarNotInFieldException;
import parkingfield.exceptions.LotOccupiedException;
import parkingfield.exceptions.LotTooNarrowException;
import parkingfield.exceptions.NoSuchLotException;
import parkingfield.exceptions.ParkingFieldFullException;

/*
 * 停车场客户端
 * 功能包括：构造停车场；查询车位和停车情况；驶离一辆车；停入一辆车；查询一个车位；
 * 
 */
public class Client {

	private final static String 
	pfInstruction = "\n 操作提示" 
			+ "\n 创建停车场：create 停车场名称 各停车位编号(>=5,逗号分割) 各停车位宽度(>150,逗号分隔),数量同停车位数目一致"
			+ "\n 查询停车场情况：query" 
			+ "\n 获得一个满足宽度要求的空停车位编号：get 宽度(数字)" 
			+ "\n 停入车辆，自动分配停车位：park1 车牌号(不空) 车的宽度(>100)"
			+ "\n 停入车辆，指定停车位：park2 车牌号(不空) 车的宽度(>100) 停车位号"
			+ "\n 将一辆车驶出停车场：depart 车牌号(不空)"
			+ "\n 显示帮助：help"
			+ "\n 退出程序：quit" 
			+ "\n 请输入命令：";
	private final static String errorInfo = "\n 请输入正确命令和参数!";
	ParkingField pf;
	
	/**
	 * 检查各操作输入命令的格式是否正确
	 */
	private static boolean isValidCommand(String[] commands) {
		boolean reFlag=true;
		String errMessage="";
		
		switch (commands[0].toLowerCase()) {
		case "create":
			if (commands.length!=4) {
				errMessage="参数个数不对!";
				reFlag=false;
				break;		
			}
			if (commands[1].length()==0) {
				errMessage="停车场名称不能为空!";
				reFlag=false;
				break;	
			}
			String[] lotnos=commands[2].split(",");
			if (Integer.valueOf(lotnos.length)<5) {  //此处不处理不是数字的异常问题，留到后面章节处理
				errMessage="停车位个数不能小于5!";
				reFlag=false;
				break;	
			}
			
			String[] width=commands[3].split(",");
			if (width.length!=lotnos.length) {  //此处不处理不是数字的异常问题，留到后面章节处理
				errMessage="停车位宽度数量应同停车位数目一致!";
				reFlag=false;
				break;	
			}
			
			for(int i=0;i<width.length;i++) {
				if (Integer.valueOf(width[i])<=150) { //此处不处理不是数字的异常问题，留到后面章节处理
					errMessage="停车位长度应大于150!";
					reFlag=false;
					break;	
				}
			}
			break;
			
		case "get":
			if (commands.length!=2) {
				errMessage="参数个数不对!";
				reFlag=false;
				break;		
			}
			
			try {
				Integer.valueOf(commands[1]);				
			} catch (Exception e) {
				errMessage="宽度应该是数字!";
				reFlag=false;				
			}
			break;
		
			
		case "park1":
			if (commands.length!=3) {
				errMessage="参数个数不对!";
				reFlag=false;
				break;		
			}
			
			if (commands[1].length()==0) {
				errMessage="车牌号不能为空!";
				reFlag=false;
				break;	
			}
			
			try {
				if (Integer.valueOf(commands[2])<101) {
					errMessage="车辆宽度应大于100!";
					reFlag=false;
				}
			} catch (Exception e) {
				errMessage="宽度应该是数字!";
				reFlag=false;
			}
			break;
			
		case "park2":
			if (commands.length!=4) {
				errMessage="参数个数不对!";
				reFlag=false;
				break;		
			}
			
			if (commands[1].length()==0) {
				errMessage="车牌号不能为空!";
				reFlag=false;
				break;	
			}
			
			try {
				if (Integer.valueOf(commands[2])<101) {
					errMessage="车辆宽度应大于100!";
					reFlag=false;
				}
			} catch (Exception e) {
				errMessage="宽度应该是数字!";
				reFlag=false;
			}
			
			try {
				if (Integer.valueOf(commands[3])<1) {
					errMessage="车位号应大于0!";
					reFlag=false;
				}
			} catch (Exception e) {
				errMessage="车位号应该是数字!";
				reFlag=false;
			}
			break;
			
		case "depart":
			if (commands.length!=2) {
				errMessage="参数个数不对!";
				reFlag=false;
				break;		
			}
			
			if (commands[1].length()==0) {
				errMessage="车牌号不能为空!";
				reFlag=false;
				break;	
			}		
		}		
		
		if (!reFlag)
			System.out.println(errMessage);
		return reFlag;
	}
	
	public static void main(String[] args) {
		
		ParkingField pf=null;
		Scanner sc = new Scanner(System.in);
		System.out.println(pfInstruction);

		while (sc.hasNext()) {
			System.out.println();
			String command = sc.nextLine().trim();
			if (command.isEmpty()) //空行，不处理，继续输入
				continue;

			// 处理退出操作
			if (command.toLowerCase().equals("quit")) {
				System.out.println("操作结束，谢谢使用！");
				sc.close();
				break;
			}

			// 解析输入命令
			String[] commands = command.split(" ");
			//调试用
			//for(int i=0;i<commands.length;i++)
			//	System.out.println(commands[i]);
			
			// 处理每个命令
			switch (commands[0].toLowerCase()) {			
			case "create": //创建停车场
				if (!isValidCommand(commands)) {  //确保参数正确
					System.out.println(errorInfo);
					System.out.println(pfInstruction);
					continue;
				}
				
				//生成车位号数组
				String[] strLotnos=commands[2].split(",");
				int[] intLotnos=new int[strLotnos.length];
				for(int i=0;i<strLotnos.length;i++)
					intLotnos[i]=Integer.valueOf(strLotnos[i]);
				
				//生成宽度数组
				String[] strWidth=commands[3].split(",");
				int[] intWidth=new int[strWidth.length];
				for(int i=0;i<strWidth.length;i++)
					intWidth[i]=Integer.valueOf(strWidth[i]);
				
				
				pf = ParkingField.create(commands[1], intLotnos, intWidth);
				System.out.println(pf.toString());
				System.out.println(pfInstruction);
				break;
				
			case "query": //处理停车场查询
				if(pf==null) {
					System.out.println("请先创建停车场!");
					System.out.println(pfInstruction);
					break;
				}else {
					System.out.println(pf.toString());
					System.out.println(pfInstruction);
					break;
				}					
			
			case "get": //获取满足宽度的停车位
				if (!isValidCommand(commands)) {  //确保参数正确
					System.out.println(errorInfo);
					System.out.println(pfInstruction);
					continue;
				}
				
				if(pf==null) {
					System.out.println("请先创建停车场!");
					System.out.println(pfInstruction);
					break;
				}
				
				Integer.valueOf(commands[1]);
				try {
					System.out.println("停车位"+pf.getOneFreeLot(Integer.valueOf(commands[1]))+"满足要求");
				} catch (ParkingFieldFullException e) {
					System.out.println("没有满足要求的空停车位!");
				}
				System.out.println(pfInstruction);
				break;
			
			case "park1":
				if (!isValidCommand(commands)) {  //确保参数正确
					System.out.println(errorInfo);
					System.out.println(pfInstruction);
					continue;
				}
				
				if(pf==null) {
					System.out.println("请先创建停车场!");
					System.out.println(pfInstruction);
					break;
				}
				
				try {
					pf.parking(commands[1], Integer.valueOf(commands[2]));
					System.out.println(pf.toString());
				} catch (ParkingFieldFullException e) {
					System.out.println("没有满足要求的空停车位!");
				}
				catch (CarAlreadyParkingException e) {
					System.out.println("车已经停在停车场!");
				}
				
				System.out.println(pfInstruction);
				break;

			case "park2":
				if (!isValidCommand(commands)) {  //确保参数正确
					System.out.println(errorInfo);
					System.out.println(pfInstruction);
					continue;
				}
				
				if(pf==null) {
					System.out.println("请先创建停车场!");
					System.out.println(pfInstruction);
					break;
				}
				
				//Car c2=new Car(commands[1], Integer.valueOf(commands[2]));				
				try {
					pf.parking(commands[1], Integer.valueOf(commands[2]),Integer.valueOf(commands[3]));
					System.out.println(pf.toString());
				} catch (LotOccupiedException e) {
					System.out.println("指定停车位不空!");
				} catch (NoSuchLotException e) {
					System.out.println("指定停车位不存在!");
				} catch (LotTooNarrowException e) {
					System.out.println("指定停车位小于车的宽度!");
				} catch (CarAlreadyParkingException e) {
					System.out.println("车已经停在停车场!");
				}				
				System.out.println(pfInstruction);
				break;
			
			case "depart":
				if (!isValidCommand(commands)) {  //确保参数正确
					System.out.println(errorInfo);
					System.out.println(pfInstruction);
					continue;
				}
				
				if(pf==null) {
					System.out.println("请先创建停车场!");
					System.out.println(pfInstruction);
					break;
				}
				
				//Car c3=new Car(commands[1],101);
				try {
					double fee;
					fee=pf.depart(commands[1]).getFee();
					System.out.println(commands[1]+"停车费用为:"+fee);//此处可以完善Record显示信息
					System.out.println(pf.toString());
				} catch (CarNotInFieldException e) {
					System.out.println("停车场中没有此车!");
				}
				System.out.println(pfInstruction);
				break;
				
			case "help":
				System.out.println(pfInstruction);
				break;
				
			default:
				System.out.println(errorInfo);
				System.out.println(pfInstruction);

			}
		}

	}
}
