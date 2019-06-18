import java.util.*;
import static java.lang.System.out;
import java.math.BigInteger;
import java.util.Scanner;

//out.print문을 편하게 출력하기 위한 객체 import 
import static java.lang.System.out;

//첫주차 숙제 1번 문제 더블 팩토리얼
//201404376 정성욱
class HW_1_dd_factorial{
	public BigInteger Double_factorial(long n){
		if(n ==0 || n==1){
			BigInteger last = new BigInteger("1");
			return last;
		//매개변수가 0이나 1일 경우 무조건 1로 가정하여 내보냅니다.
		}else {
			long n_n = n-2;
			return Double_factorial(n_n).multiply(BigInteger.valueOf(n));
		//위의 조건, 매개변수가 1이나 0이 아닐 경우 매개변수-2를 한 후 다시 recursion을 수행합니다. 
		//최종 분기점에 도달했을때 1을 받고 다시 최초 시작점으로 되돌아 올 때 계산을 위해 앞에 n을 붙여 줍니다.
		}
	}
}



//첫 주차 숙제 2번 문제 페리수열
//201404376 정성욱
class HW_2_FS{

	
	//n은 몇번째 바보셈인지,분모와 분자의 리스트를 받아 페리수열을 출력하는 함수입니다. 
	void print_list(int n, ArrayList p, ArrayList c){
		out.print("f"+n+":");
		for (int i=0;i<p.size();i++) {
			if(i==0){
				//시작할때 괄호 하나 출력하고 시작합니다.
				out.print("[");
				
			}
			out.print(c.get(i));
			out.print("/");
			out.print(p.get(i));
			if(i==p.size()-1) {
				//끝부분이면 괄호로 마무리합니다 포문 도는 중이라면,
				out.println("]");
			}else {
				//중간중간에 쉼표 넣어줍니다.
				out.print(", ");				
			}
			
			
		}
	}
	
	//다음 페리수열 연산을 하기 위해 현재 있는 분모,분자 리스트를 크기 순으로 정렬 하는 함수입니다.
	//인수로 분모,분자,비교할 arraylist의 시작점을 나타내는 index값,비교할 arraylist의 끝점을 나타내는 index값을 매개변수로 받습니다.
	void Compare(ArrayList p,ArrayList c,int begin,int end){
		//비교를 위한 버블 소트문을 돌립니다.
		for(int i= begin;i<end;i++) {
			//비교할 리스트의 i인덱스에 해당하는 분모와 분자값을 받아서 a라는 변수에 저장합니다.
			double a =(double)((int)c.get(i))/((int)p.get(i));
			for(int j=i+1;j<end;j++) {
				//비교할 리스트의 j인덱스에 해당하는 분모와 분자값을 받아서 a라는 변수에 저장합니다.
				double b = (double)((int)c.get(j))/((int)p.get(j));
				//Double에 내장된 compare함수를 이용하여 두개의 double형 변수를 받아서 오른쪽에 있는 값이 더 클경우 
				//분자(c) array list와 분모(p) array list에서 인덱스가 지정된 값들 끼리 위치를 바꿉니다.
				if(Double.compare(a,b)==1){
					Collections.swap(c, i, j);
					Collections.swap(p, i, j);
				}
			}
		}
	}
	
	//재귀로 페리수열을 진행하는 함수입니다.
	//매개변수로 분모,분자를 나타내는 두개의 arraylist 그리고 페리 소팅을 시작할 부분 지정하는 시작점(앞에서 언급한 것과는 다른 begin),그리고 페리 소팅을 끝낼 끝점(앞에서 언급한 것과는 다른 end)을 받습니다.
	//앞부분과 차이가 있다면 여기서는 begin은 현재가 몇번째 페리수열인지 알려주는 역할이고 end가 상용자가 입력한값  즉, 페리수열의 마지막을 나태내는 것입니다.
	void proceed_fs(ArrayList p,ArrayList c,int begin, int end){
		//페리 수열을 계산 돕기 위해 새로운 분모,분자의 인덱스의 값을 저장하기 위한 arraylist입니다.
		ArrayList index = new ArrayList();;
		//페리 수열 계산을 돕기 위해 새로운 분자 값을 저장하는 arraylist입니다.
		ArrayList c_value = new ArrayList();
		//페리 수열 계산을 돕기 위해 새로운 분모 값을 저장하는 arraylist입니다.
		ArrayList p_value  = new ArrayList();
		//분기 문입니다. 사용자가 입력한 값보다 현재 진행되고있는 페리수열의 횟수(n번쨰)가 높다면 종료합니다.
		if(begin>end) {
			return ;
		}
		//p.size()-1로 설정한 이유는 뒤에 j라는 변수를 두어서 i보다 옆에있는 인덱스를 관리하기 위함입니다
		for(int i=0;i<p.size()-1;i++) {
			int j=i+1;
			//a는 i번째의 분자,d는 j번째의 분자입니다.
			int a = (int)c.get(i);
			int d = (int)c.get(j);
			//아래의 b를 예로 설명하자면 [0/1, 1/2] 에서 0+1 을 나타내는 바보셈을 하는 것입니다.
			int b = a+d;
			//a_는 i번째의 분자,d_는 j번째의 분모입니다.
			int a_ = (int)p.get(i);
			int d_ = (int)p.get(j);
			//아래의 b_를 예로 설명하자면 [0/1, 1/2] 에서 1+2 을 나타내는 바보셈을 하는 것입니다.
			int b_ = a_ +d_;
			//페리수열에서 걸러야할 기준이 되는 것은 분모가 현재 n값 보다 큰 경우 이므로, 현재 n값인 begin과 바보셈을 한 분모의 값을 비교후 분모보다 크다면
			//컨디뉴 시킵니다.
			if(b_>begin) {
				continue;
			}
			//분모가 검문을 통과한다면, 인덱스리스트에 현재 오른쪽에 있는 값에 대한 인덱스를 저장, 검문을 통과한 분모,분자값을 각각의 보조를 위한 리스트에 저장합니다.
			index.add(j);
			p_value.add(b_);
			c_value.add(b);
		}
		//idx 카운터인데, 보조로 사용된 리스트에 있던 값이 빠져나가 원래 페리수열 리스트에 새로운 값이 추가된다면
		//보조 리스트에 저장하고 있는 추가해야될(인덱스를 나타내는)위치를 나타내는 현재의 값은  더이상 쓸모가없습니다. 
		//추가되는 만큼 인덱스의 값을 증가 시켜줘야하는데 그 인덱스 갱신을 위한 변수 입니다.
		int idx_count =0;
		//인덱스를 나타내는 보조 리스트의 인덱스값을 하나씩 끄집어내어 새로운 분자와 분모를 만들어 추가합니다.
		for(int i = 0;i<index.size();i++){
			int newc = (int)c_value.get(i);
			int newp = (int)p_value.get(i);
			int index_ = (int)index.get(i)+idx_count;
			c.add(index_,newc);
			p.add(index_,newp);
			//보조에서 원래 리스트에 추가가 이루어 질때마다 인덱스 갱신을 위한 값을 하나씩 증가 시킵니다. 
			idx_count++;
		}
		//바보셈의 결과가 추가된 배열리스트에 대해 각 값들을 하나씩 불러와서, 크기별로 정리합니다.
		Compare(p,c,1,begin+1);
		//출력 함수로 현재 리스트에 저장된 값들을 출력합니다.
		print_list(begin,p,c);
		//보조 리스트들의 값들을 초기화 해줍니다.
		c_value.clear();
		p_value.clear();
		//다음 루프로 넘어갑니다.
		proceed_fs(p,c,++begin,end);
		
	}
	void setting_initial(int n) {
		
		//바보셈 수행하고 다음 루프때 새로 갱신될 리스트에 추가할 새로운 분모와 분자를 임시적으로 저장할 리스트 입니다.
		//p는 분모
		ArrayList p_list = new ArrayList();
		//c는 분자
		ArrayList c_list = new ArrayList();
		//초기값 설정입니다. 1의 입력이 기본 베이스가 되어야 루프를 진행 할 수 있기 때문에 설정해줍니다
		// f1:[0/1, 1/1]
		p_list.add(1);
		p_list.add(1);
		
		c_list.add(0);
		c_list.add(1);
		//출력을 한번하고 루프를 돌립니다.
		print_list(1,p_list,c_list);
		proceed_fs(p_list,c_list,2,n);
	}
	
}
	 class PL_00_201404376_정성욱_hw01 {	
			public static void main(String []args){
				//실행문
				Scanner sc = new Scanner(System.in);
				System.out.print("1번문제 더블 팩토리얼 input:");
				int dd_input = sc.nextInt();
				HW_1_dd_factorial dd = new HW_1_dd_factorial();
				out.println();
				out.println("output:"+dd.Double_factorial(dd_input));
				
				HW_2_FS ds = new HW_2_FS();
				System.out.print("2번문제 페리수열 input:");
				int fs_input = sc.nextInt();
				ds.setting_initial(fs_input);

				
			 }

}

	 //201404376 정성욱
	