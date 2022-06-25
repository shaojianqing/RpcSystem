package org.smile.service.consumer.test.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.smile.framework.route.annotation.RpcReference;
import org.smile.service.domain.accountbill.BillTypeEnum;
import org.smile.service.domain.userinfo.UserInfo;
import org.smile.service.facade.IAccountBillService;
import org.smile.service.facade.IAccountInfoService;
import org.smile.service.facade.IUserInfoService;
import org.smile.service.request.AccountBillOperRequest;

public class TestDataAction {
	
	@RpcReference(referenceId="accountBillService")
	private IAccountBillService accountBillService;
	
	@RpcReference(referenceId="accountInfoService")
	private IAccountInfoService accountInfoService;
	
	@RpcReference(referenceId="userInfoService")
	private IUserInfoService userInfoService;
	
	public void executeGenerateTestData() throws Exception {
		
		String nameList[] = new String[]{"阳顶天", "张无忌", "托克托", "李嘉图", "科斐波尔", "刘天成", "王阳明", "费米", "奥本海默", "洛克菲勒",
										 "诺依曼", "卡夫卡", "富兰克林", "弗兰克", "克丽莎", "托马斯", "平程阳", "徐达", "刘伯温", "吴岩极",
										 "马克思", "李世名", "武承思", "武之安", "张居正", "恩格斯", "欧天成", "马尔克斯", "蒋百里", "李隆基",
										 "刘询", "朱祁镇", "如来", "孙悟空", "左宗棠", "戴安澜", "张自忠", "宇文邕", "高阳", "亚当斯密",
										 "宇文华", "凯恩斯", "爱因斯坦", "钟会", "诸葛亮", "孙伯符", "周瑜", "张维迎", "诸葛瑾", "伯利恒",
										 "邵庸", "赵冰之", "何文明", "明晓溪", "明靖安", "张华", "高安", "付晓英", "应佳成", "长孙无忌",
										 "杨士奇", "解缙", "文天祥", "于东阳", "赵匡胤", "辛弃疾", "李太白", "李商隐", "高适", "陈颖琪",
										 "舒庆春", "朱文安", "白崇禧", "李宗仁", "宋子文", "宋仲基", "宋嘉树", "陈立夫", "左丘明", "商鞅",
										 "赵子龙", "司徒静", "上官洪", "司马懿", "云向天", "向华成", "刘天奇", "盛学军", "张海安", "柯杰其",
										 "周大伟", "章太炎", "钟琪华", "刘安静", "金全福", "成益华", "刘捷信", "安琥", "李默然", "邵坤华"};
		
		String userList[] = new String[]{"smith", "yeclo", "steven", "jarry", "lily2016", "kate", "kimiy", "yalle", "babob", "annie",
										 "soccet", "frank", "rosvert", "candy", "wendy", "vivian", "vicent", "ziddle", "ellay", "geogre",
										 "hebbay", "senline", "joy", "jatey", "john", "dave", "michelle", "mary", "alice", "harley",
										 "oliver", "sunny", "james", "sarah", "robert", "william", "peter", "paul", "racel", "thomas",
										 "lisa", "daniel", "elizabeth", "kevin", "angela", "emily", "richard", "charles", "eric", "chris",
										 "andy", "linda", "jackson", "tina", "alex", "edward", "leo", "emma", "jim", "tony",
										 "joseph", "cindy", "henry", "grace", "alan", "susan", "maggie", "christian", "tom", "rebecca",
										 "claire", "carlos", "vanessa", "judy", "stephen", "catherine", "jean", "helen", "andrew", "karen",
										 "marco", "jonathan", "elaine", "gary", "nana", "antonio", "nicole", "alexander", "margaret", "matthew",
										 "julia", "louis", "natalie", "jose", "patrick", "olivia", "samy", "betty", "angel", "vicky"};
		
		int profitNum[] = new int[]{1889, 2898, 1982, 8981, 2891, 1883, 6787, 9812, 2381, 5568,
									2891, 1829, 1903, 9233, 2182, 3721, 8923, 3278, 8829, 2233,
									2901, 8928, 1389, 3378, 2212, 3238, 2381, 9818, 7452, 4312,
									3882, 2781, 8492, 2323, 2832, 3843, 2341, 8129, 9823, 9712,
									4533, 7681, 5621, 9823, 1342, 1923, 8732, 3723, 3778, 8711,
									3892, 3910, 9821, 2319, 9912, 2833, 3132, 4783, 8643, 2231,
									2912, 2129, 1848, 2813, 8893, 3882, 3421, 4332, 3882, 3253,
									1982, 8912, 1672, 8421, 8234, 2812, 9881, 8632, 2435, 8723,
									1235, 1783, 5723, 9833, 1721, 3843, 9911, 2333, 5532, 7813,
									1902, 2893, 9153, 3721, 3813, 3839, 2218, 8792, 6723, 8932};
		
		int expanseNum[] = new int[]{1389, 2598, 1182, 5989, 1992, 1383, 6587, 2312, 2181, 5325,
									 2821, 1439, 839, 782, 323, 123, 122, 939, 832, 783,
									 2311, 1245, 238, 332, 843, 372, 812, 933, 813, 673,
									 3662, 1279, 894, 939, 189, 829, 982, 783, 132, 891, 
									 4123, 5892, 235, 882, 188, 128, 891, 283, 931, 712,
									 2672, 2891, 897, 281, 173, 673, 343, 129, 883, 323,
									 1212, 1920, 732, 921, 713, 766, 323, 382, 712, 713,
									 1282, 1829, 234, 736, 892, 323, 332, 881, 214, 888,
									 843, 689, 523, 137, 235, 903, 774, 813, 478, 787,
									 892, 367, 782, 453, 353, 383, 821, 913, 981, 848};
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = formatter.parse("1988-08-08");
		
		String baseIdNumber = "3301831989020821";
		
		for (int i=0;i<100;++i) {
			String username = userList[i];
			String realname = nameList[i];
			
			String idNumber = String.format("%s%d", baseIdNumber, i);
			
			UserInfo userInfo = userInfoService.registeUserInfo(username, "12345678", realname, birthday, idNumber);
			
			AccountBillOperRequest request = new AccountBillOperRequest();
			request.setAmount(profitNum[i]+0.0);
			request.setBillType(BillTypeEnum.PROFIT.getType());
			request.setUserInfoId(userInfo.getId());
			
			accountBillService.operateAccountBill(request);
			
			request = new AccountBillOperRequest();
			request.setAmount(expanseNum[i]+0.0);
			request.setBillType(BillTypeEnum.EXPANSE.getType());
			request.setUserInfoId(userInfo.getId());
			
			accountBillService.operateAccountBill(request);
		}
	}

	public void setAccountBillService(IAccountBillService accountBillService) {
		this.accountBillService = accountBillService;
	}

	public void setAccountInfoService(IAccountInfoService accountInfoService) {
		this.accountInfoService = accountInfoService;
	}

	public void setUserInfoService(IUserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
}
