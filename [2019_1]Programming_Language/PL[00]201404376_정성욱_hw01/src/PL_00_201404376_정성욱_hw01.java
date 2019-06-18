import java.util.*;
import static java.lang.System.out;
import java.math.BigInteger;
import java.util.Scanner;

//out.print���� ���ϰ� ����ϱ� ���� ��ü import 
import static java.lang.System.out;

//ù���� ���� 1�� ���� ���� ���丮��
//201404376 ������
class HW_1_dd_factorial{
	public BigInteger Double_factorial(long n){
		if(n ==0 || n==1){
			BigInteger last = new BigInteger("1");
			return last;
		//�Ű������� 0�̳� 1�� ��� ������ 1�� �����Ͽ� �������ϴ�.
		}else {
			long n_n = n-2;
			return Double_factorial(n_n).multiply(BigInteger.valueOf(n));
		//���� ����, �Ű������� 1�̳� 0�� �ƴ� ��� �Ű�����-2�� �� �� �ٽ� recursion�� �����մϴ�. 
		//���� �б����� ���������� 1�� �ް� �ٽ� ���� ���������� �ǵ��� �� �� ����� ���� �տ� n�� �ٿ� �ݴϴ�.
		}
	}
}



//ù ���� ���� 2�� ���� �丮����
//201404376 ������
class HW_2_FS{

	
	//n�� ���° �ٺ�������,�и�� ������ ����Ʈ�� �޾� �丮������ ����ϴ� �Լ��Դϴ�. 
	void print_list(int n, ArrayList p, ArrayList c){
		out.print("f"+n+":");
		for (int i=0;i<p.size();i++) {
			if(i==0){
				//�����Ҷ� ��ȣ �ϳ� ����ϰ� �����մϴ�.
				out.print("[");
				
			}
			out.print(c.get(i));
			out.print("/");
			out.print(p.get(i));
			if(i==p.size()-1) {
				//���κ��̸� ��ȣ�� �������մϴ� ���� ���� ���̶��,
				out.println("]");
			}else {
				//�߰��߰��� ��ǥ �־��ݴϴ�.
				out.print(", ");				
			}
			
			
		}
	}
	
	//���� �丮���� ������ �ϱ� ���� ���� �ִ� �и�,���� ����Ʈ�� ũ�� ������ ���� �ϴ� �Լ��Դϴ�.
	//�μ��� �и�,����,���� arraylist�� �������� ��Ÿ���� index��,���� arraylist�� ������ ��Ÿ���� index���� �Ű������� �޽��ϴ�.
	void Compare(ArrayList p,ArrayList c,int begin,int end){
		//�񱳸� ���� ���� ��Ʈ���� �����ϴ�.
		for(int i= begin;i<end;i++) {
			//���� ����Ʈ�� i�ε����� �ش��ϴ� �и�� ���ڰ��� �޾Ƽ� a��� ������ �����մϴ�.
			double a =(double)((int)c.get(i))/((int)p.get(i));
			for(int j=i+1;j<end;j++) {
				//���� ����Ʈ�� j�ε����� �ش��ϴ� �и�� ���ڰ��� �޾Ƽ� a��� ������ �����մϴ�.
				double b = (double)((int)c.get(j))/((int)p.get(j));
				//Double�� ����� compare�Լ��� �̿��Ͽ� �ΰ��� double�� ������ �޾Ƽ� �����ʿ� �ִ� ���� �� Ŭ��� 
				//����(c) array list�� �и�(p) array list���� �ε����� ������ ���� ���� ��ġ�� �ٲߴϴ�.
				if(Double.compare(a,b)==1){
					Collections.swap(c, i, j);
					Collections.swap(p, i, j);
				}
			}
		}
	}
	
	//��ͷ� �丮������ �����ϴ� �Լ��Դϴ�.
	//�Ű������� �и�,���ڸ� ��Ÿ���� �ΰ��� arraylist �׸��� �丮 ������ ������ �κ� �����ϴ� ������(�տ��� ����� �Ͱ��� �ٸ� begin),�׸��� �丮 ������ ���� ����(�տ��� ����� �Ͱ��� �ٸ� end)�� �޽��ϴ�.
	//�պκа� ���̰� �ִٸ� ���⼭�� begin�� ���簡 ���° �丮�������� �˷��ִ� �����̰� end�� ����ڰ� �Է��Ѱ�  ��, �丮������ �������� ���³��� ���Դϴ�.
	void proceed_fs(ArrayList p,ArrayList c,int begin, int end){
		//�丮 ������ ��� ���� ���� ���ο� �и�,������ �ε����� ���� �����ϱ� ���� arraylist�Դϴ�.
		ArrayList index = new ArrayList();;
		//�丮 ���� ����� ���� ���� ���ο� ���� ���� �����ϴ� arraylist�Դϴ�.
		ArrayList c_value = new ArrayList();
		//�丮 ���� ����� ���� ���� ���ο� �и� ���� �����ϴ� arraylist�Դϴ�.
		ArrayList p_value  = new ArrayList();
		//�б� ���Դϴ�. ����ڰ� �Է��� ������ ���� ����ǰ��ִ� �丮������ Ƚ��(n����)�� ���ٸ� �����մϴ�.
		if(begin>end) {
			return ;
		}
		//p.size()-1�� ������ ������ �ڿ� j��� ������ �ξ i���� �����ִ� �ε����� �����ϱ� �����Դϴ�
		for(int i=0;i<p.size()-1;i++) {
			int j=i+1;
			//a�� i��°�� ����,d�� j��°�� �����Դϴ�.
			int a = (int)c.get(i);
			int d = (int)c.get(j);
			//�Ʒ��� b�� ���� �������ڸ� [0/1, 1/2] ���� 0+1 �� ��Ÿ���� �ٺ����� �ϴ� ���Դϴ�.
			int b = a+d;
			//a_�� i��°�� ����,d_�� j��°�� �и��Դϴ�.
			int a_ = (int)p.get(i);
			int d_ = (int)p.get(j);
			//�Ʒ��� b_�� ���� �������ڸ� [0/1, 1/2] ���� 1+2 �� ��Ÿ���� �ٺ����� �ϴ� ���Դϴ�.
			int b_ = a_ +d_;
			//�丮�������� �ɷ����� ������ �Ǵ� ���� �и� ���� n�� ���� ū ��� �̹Ƿ�, ���� n���� begin�� �ٺ����� �� �и��� ���� ���� �и𺸴� ũ�ٸ�
			//���� ��ŵ�ϴ�.
			if(b_>begin) {
				continue;
			}
			//�и� �˹��� ����Ѵٸ�, �ε�������Ʈ�� ���� �����ʿ� �ִ� ���� ���� �ε����� ����, �˹��� ����� �и�,���ڰ��� ������ ������ ���� ����Ʈ�� �����մϴ�.
			index.add(j);
			p_value.add(b_);
			c_value.add(b);
		}
		//idx ī�����ε�, ������ ���� ����Ʈ�� �ִ� ���� �������� ���� �丮���� ����Ʈ�� ���ο� ���� �߰��ȴٸ�
		//���� ����Ʈ�� �����ϰ� �ִ� �߰��ؾߵ�(�ε����� ��Ÿ����)��ġ�� ��Ÿ���� ������ ����  ���̻� ���𰡾����ϴ�. 
		//�߰��Ǵ� ��ŭ �ε����� ���� ���� ��������ϴµ� �� �ε��� ������ ���� ���� �Դϴ�.
		int idx_count =0;
		//�ε����� ��Ÿ���� ���� ����Ʈ�� �ε������� �ϳ��� ������� ���ο� ���ڿ� �и� ����� �߰��մϴ�.
		for(int i = 0;i<index.size();i++){
			int newc = (int)c_value.get(i);
			int newp = (int)p_value.get(i);
			int index_ = (int)index.get(i)+idx_count;
			c.add(index_,newc);
			p.add(index_,newp);
			//�������� ���� ����Ʈ�� �߰��� �̷�� �������� �ε��� ������ ���� ���� �ϳ��� ���� ��ŵ�ϴ�. 
			idx_count++;
		}
		//�ٺ����� ����� �߰��� �迭����Ʈ�� ���� �� ������ �ϳ��� �ҷ��ͼ�, ũ�⺰�� �����մϴ�.
		Compare(p,c,1,begin+1);
		//��� �Լ��� ���� ����Ʈ�� ����� ������ ����մϴ�.
		print_list(begin,p,c);
		//���� ����Ʈ���� ������ �ʱ�ȭ ���ݴϴ�.
		c_value.clear();
		p_value.clear();
		//���� ������ �Ѿ�ϴ�.
		proceed_fs(p,c,++begin,end);
		
	}
	void setting_initial(int n) {
		
		//�ٺ��� �����ϰ� ���� ������ ���� ���ŵ� ����Ʈ�� �߰��� ���ο� �и�� ���ڸ� �ӽ������� ������ ����Ʈ �Դϴ�.
		//p�� �и�
		ArrayList p_list = new ArrayList();
		//c�� ����
		ArrayList c_list = new ArrayList();
		//�ʱⰪ �����Դϴ�. 1�� �Է��� �⺻ ���̽��� �Ǿ�� ������ ���� �� �� �ֱ� ������ �������ݴϴ�
		// f1:[0/1, 1/1]
		p_list.add(1);
		p_list.add(1);
		
		c_list.add(0);
		c_list.add(1);
		//����� �ѹ��ϰ� ������ �����ϴ�.
		print_list(1,p_list,c_list);
		proceed_fs(p_list,c_list,2,n);
	}
	
}
	 class PL_00_201404376_������_hw01 {	
			public static void main(String []args){
				//���๮
				Scanner sc = new Scanner(System.in);
				System.out.print("1������ ���� ���丮�� input:");
				int dd_input = sc.nextInt();
				HW_1_dd_factorial dd = new HW_1_dd_factorial();
				out.println();
				out.println("output:"+dd.Double_factorial(dd_input));
				
				HW_2_FS ds = new HW_2_FS();
				System.out.print("2������ �丮���� input:");
				int fs_input = sc.nextInt();
				ds.setting_initial(fs_input);

				
			 }

}

	 //201404376 ������
	