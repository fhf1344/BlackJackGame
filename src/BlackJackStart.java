import javax.swing.JFrame;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;

import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.Arrays;



public class BlackJackStart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		Card ca = new Card();
		Betting be = new Betting();
		Money_mb mm = new Money_mb();
		Dealer dealer = new Dealer();
		String[][][] card = new String[4][4][13];
		String[] deal_card;
		int p = 0;
		boolean allZeros = false;
		card = ca.card_value();
		System.out.println("시드금액은 1000입니다.");
		System.out.print("참가할 인원을 선택하여 주세요 : ");
		p = sc.nextInt();
		String[] mb_ans = new String[p];
		int[] mb_mm = new int[p];
		int[] mb = new int[p];
		String[][] mb_card = new String[p][12];
		mb_mm = mm.fmb_money(p);
		while(!allZeros) {
			int hap = 0;
			int[] mb_count = new int[p];
			int[] mb_sum = new int[p];
			
			if(allZeros == true) {
				break;
			}
			mb = be.bet_member(p, mb_mm);
			mb_mm = mm.after_money(mb_mm, mb, p);
			for(int i = 0; i < p; i++) {
				if(mb_mm[i] >= 0) {
					if(mb[i] > 0) {
						System.out.println((i+1) + "번째 참가자 베팅금액 : " + mb[i] + ", 남은 금액 : " + mb_mm[i]);
					}
					else {
						System.out.println((i+1) + "번째 참가자 돈은 있지만 베팅하지 않음 " + ", 남은 금액 : " + mb_mm[i]);
					}
				}
				else {
					System.out.println((i+1) + "번째 참가자 돈 부족으로 게임 참여x");
				}
			}
			Result mb_result = dealer.distribute_card(card, p, mb_mm, mb);
			Result deal_result = dealer.dealer_card(card);
			mb_card = mb_result.getmb_card();
			deal_card = deal_result.getdear_card();
			card = mb_result.getcard();
//			for(int i = 0; i < 4; i++) {
//				for(int j = 0; j < 4; j++) {
//					for(int k = 0; k < 13;k++) {
//						System.out.print(card[i][j][k]); 
//					}
//					System.out.println();
//				}
//			}
			
			for(int i = 0; i < p; i++) {
				System.out.print((i+1)+"번째 참가자 카드 : ");
				for(int j = 0; j < 2; j++) {
					if(mb_card[i][j].equals("-1")) {
						System.out.print("X ");
					}
					else {
						System.out.print(mb_card[i][j] + " ");
						mb_count[i]++;
					}
				}
				System.out.println();
			}
			for(int i = 0; i < 1; i++) {
				System.out.println("딜러 카드 : " + deal_card[i]);
			}
			System.out.println();
			for(int i = 0; i < p; i++) {
				int count = 0;
				int card_count = 2;
				int sum = 0;
				Result fcardsum = ca.card_sum(mb_card, card_count, i);
				sum = fcardsum.getSum();
				if(!mb_card[i][0].equals("-1")) {
					while(true) {
						System.out.print((i+1) + "번째 참가자 현재 카드 근황 : ");
						for(int j = 0; j < card_count; j++) {
							System.out.print(mb_card[i][j]);
						}
						System.out.println(", " + sum);
						System.out.print((i+1) + "번째 참가자 stay, hit : ");
						mb_ans[i] = sc.next();
						
						if(mb_ans[i].equals("hit")) {
							count++;
							for(int j = 2 + count - 1; j < 2 + count; j++) {
								Result stay_result = ca.stay_card(i, j, card, mb_card);
								mb_card = stay_result.getmb_card();
								card = mb_result.getcard();
								card_count++;
								mb_count[i]++;
							}
							Result cardsum = ca.card_sum(mb_card, card_count, i);
							sum = cardsum.getSum();
//							for(int j = 0; j < 2 + count; j++) {카드, 합 한번더 보여주는거
//								System.out.print(mb_card[i][j] + " ");
//							}
//							System.out.println();
//							System.out.println(sum);
						}
						if(sum > 21 ) {
							for(int j = 0; j < 2 + count; j++) {
								System.out.print(mb_card[i][j] + " ");
							}
							System.out.println();
							System.out.println(sum);
							mb_sum[i] = sum;
							System.out.println("you are Bust..");
							break;
						}
						else if(mb_ans[i].equals("stay")) {
							Result cardsum = ca.card_sum(mb_card, card_count, i);
							sum = cardsum.getSum();
//							for(int j = 0; j < 2 + count; j++) {카드, 합 한번더 보여주는거
//								System.out.print(mb_card[i][j] + " ");
//							}							
//							System.out.println();
//							System.out.println(sum);
							mb_sum[i] = sum;
							break;
						}
						
					}
				}
			}
			int d_count = 0;
			int d_car_c = 2;
			int d_sum = 0;
			Result fcardsum = ca.d_card_sum(deal_card, 2);
			d_sum = fcardsum.getSum();
			while(true) {
				
				if(d_sum <= 16) {
					d_count++;
					for(int i = 2 + d_count - 1; i < 2 + d_count; i++) {
						Result d_stay_result = ca.d_stay_card(i, card, deal_card);
						deal_card = d_stay_result.getdear_card();
						card = mb_result.getcard();
						d_car_c++;
						
					}
					Result cardsum = ca.d_card_sum(deal_card, d_car_c);
					d_sum = cardsum.getSum();
					
				}
				if(d_sum > 21 ) {
					System.out.println("dealer is Bust..(Winner Double the bet)");
					break;
				}
				else if(d_sum > 16 && d_sum <= 21) {
					Result cardsum = ca.d_card_sum(deal_card, d_car_c);
					d_sum = cardsum.getSum();
					break;
				}
			}	
			System.out.println("\n\n===================결과===================\n\n");
			
			System.out.print("딜러 카드 : ");
			for(int i = 0; i < 2 + d_count; i++) {
				System.out.print(deal_card[i] + " ");;
			}
			System.out.println();
			System.out.println("딜러 숫자 합 : " + d_sum);
			for(int i = 0; i < p; i++) {
				System.out.println();
				if(!mb_card[i][0].equals("-1")) {
					if((d_sum < mb_sum[i] && mb_sum[i] <= 21) || (mb_sum[i] <= 21 && d_sum > 21)) {
						System.out.print((i+1) + "번째 참가자 카드 : ");
						for(int j = 0; j < mb_count[i]; j++) {
							System.out.print(mb_card[i][j] + " ");
						}
						System.out.println();
						System.out.print("카드 숫자 합 : " + mb_sum[i]);
						System.out.print("-Winner Double the bet");
						mb_mm[i] = mb_mm[i] + (mb[i] * 2);
					}
					else if(mb_sum[i] <= 21 && mb_sum[i] == d_sum){
						System.out.print((i+1) + "번째 참가자 카드 : ");
						for(int j = 0; j < mb_count[i]; j++) {
							System.out.print(mb_card[i][j] + " ");
						}
						System.out.println();
						System.out.print("카드 숫자 합 : " + mb_sum[i]);
						System.out.print("-Draw");
						mb_mm[i] = mb_mm[i] + mb[i]; 
					}
					else if((mb_sum[i] <= 21 && d_sum > mb_sum[i]) || mb_sum[i] > 21){
						System.out.print((i+1) + "번째 참가자 카드 : ");
						for(int j = 0; j < mb_count[i]; j++) {
							System.out.print(mb_card[i][j] + " ");
						}
						System.out.println();
						System.out.print("카드 숫자 합 : " + mb_sum[i]);
						System.out.print("-Lower");
					}
				}
				
				System.out.println();
			}
			System.out.println("\n\n\n");
			for(int i = 0; i < p; i++) {
				if(mb_mm[i] == 0) {
					hap++;
					if(hap == 1 * p) {
						allZeros = true;
					}
				}
			}
		}
		System.out.println("\n\n게임 종료\n\n");
	}
}
class Card{
	private int x, y, z;
	public Card() {
		
	}
	public static String[][][] card_value() {
		//카드생성
		String[][][] card_val = new String[4][4][13];
		String[] Pattern_val = {"◆", "♠", "♣", "♥"};
		String[] num = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 13;k++) {
					card_val[i][j][k] = Pattern_val[j] + num[k]; 
				}
			}
		}
		return card_val;
	}
	public Result stay_card(int mb, int num, String[][][] card, String[][] mb_card) {
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		x = (int)(Math.random() * 4);
		y = (int)(Math.random() * 4);
		z = (int)(Math.random() * 13);
		mb_card[mb][num] = card[x][y][z];
		if(mb_card[mb][num] == null) {
			while(mb_card[mb][num] == null) {
				x = (int)(Math.random() * 3);
				y = (int)(Math.random() * 3);
				z = (int)(Math.random() * 13);
				mb_card[mb][num] = card[x][y][z];
			}
		}
		card[x][y][z] = null;
		return new Result(mb_card, card);
	}
	public Result d_stay_card(int num, String[][][] card, String[] deal_card) {
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		x = (int)(Math.random() * 4);
		y = (int)(Math.random() * 4);
		z = (int)(Math.random() * 13);
		deal_card[num] = card[x][y][z];
		if(deal_card[num] == null) {
			while(deal_card[num] == null) {
				x = (int)(Math.random() * 3);
				y = (int)(Math.random() * 3);
				z = (int)(Math.random() * 13);
				deal_card[num] = card[x][y][z];
			}
		}
		card[x][y][z] = null;
		return new Result(deal_card, card);
	}
	public Result card_sum(String[][] mb_card, int card_count, int mb) {
		int sum = 0;
		int Acount = 0;
		for(int i = 0; i < card_count; i++) {
			if(mb_card[mb][i].substring(1).equals("A")) {
				Acount++;
			}
			else if(mb_card[mb][i].substring(1).equals("J") || mb_card[mb][i].substring(1).equals("Q") || mb_card[mb][i].substring(1).equals("K")) {
				sum += 10;
			}
			else{
				sum += Integer.parseInt(mb_card[mb][i].substring(1));
			}
		}
		if(Acount > 0) {
			if((sum + 11 + (Acount - 1)) <= 21) {
				sum += 11 + (Acount - 1);
			}
			else {
				sum += Acount;
			}
		}
		return new Result(sum);
	}
	public Result d_card_sum(String[] deal_card, int d_car_c) {
		int sum = 0;
		int Acount = 0;
		for(int i = 0; i < d_car_c; i++) {
			if(deal_card[i].substring(1).equals("A")) {
				Acount++;
			}
			else if(deal_card[i].substring(1).equals("J") || deal_card[i].substring(1).equals("Q") || deal_card[i].substring(1).equals("K")) {
				sum += 10;
			}
			else{
				sum += Integer.parseInt(deal_card[i].substring(1));
			}
		}
		if(Acount > 0) {
			if((sum + 11 + (Acount - 1)) <= 21) {
				sum += 11 + (Acount - 1);
			}
			else {
				sum += Acount;
			}
		}
		return new Result(sum);
	}
}
class Betting{
	public Betting() {
		
	}
	public int[] bet_member(int p, int[]mb_mm) {
		//참가자수 만큼 베팅
		Scanner sc = new Scanner(System.in);
		int[] mb_bet = new int[p];
		for(int i = 0;i < p; i++) {
			if(mb_mm[i] > 0)
			{
				System.out.println((i+1) + "번째 참가자 베팅해주세요 : ");
				mb_bet[i] = sc.nextInt();
				if(mb_bet[i] > mb_mm[i] ) {
					System.out.println("가지고 계신 돈보다 더 많은 베팅금액을 입력하셨습니다. 다음 게임에 참여해주세요.");
					mb_bet[i] = 0;
				}
			}
			else{
				System.out.println((i+1) + "번째 참가자는 베팅금액이 없어서 참여 못합니다.");
				mb_bet[i] = -1;
			}
		}
		return mb_bet;
	}
}
class Money_mb{
	public Money_mb(){
		
	}
	public int[] fmb_money(int p) {
		//초기 베팅시드
		int[] f_m = new int[p];
		for(int i = 0; i < p; i++) {
			f_m[i] = 1000;
		}
		return f_m;
	}
	public int[] after_money(int[] now_money, int[] bet_money, int p ) {
		//베팅하고 현재 남은 돈
		for(int i = 0; i < p; i++) {
			if(now_money[i] >= 0 && bet_money[i] != -1) {
				now_money[i] -= bet_money[i];
			}
			else {
				now_money[i] =-1;
			}
		}
		return now_money;
	}
}
class Dealer{
	private int x, y, z;
	public Result distribute_card(String[][][] card, int p, int[] mb_mm, int[] mb) {
		//딜러가 참가자들에게 나눠주는 카드
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		String [][] mb_card = new String[p][12];
		for(int i = 0; i < p; i++ ) {
			if(mb_mm[i] >= 0 && mb[i] > 0) {
				for(int j = 0; j < 2; j++) {
					x = (int)(Math.random() * 4);
					y = (int)(Math.random() * 4);
					z = (int)(Math.random() * 13);
					mb_card[i][j] = card[x][y][z];
					if(mb_card[i][j] == null) {
						while(mb_card[i][j] == null) {
							x = (int)(Math.random() * 3);
							y = (int)(Math.random() * 3);
							z = (int)(Math.random() * 13);
							mb_card[i][j] = card[x][y][z];
						}
					}
					card[x][y][z] = null;
				}
			}
			else {
				for(int j = 0; j < 2; j++) {
					mb_card[i][j] = "-1";
				}
			}
		}
		return new Result(mb_card, card);
	}
	public Result dealer_card(String[][][]card) {
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		String[] dealer_card = new String[12];
		for(int i = 0; i < 1; i++ ) {
			for(int j = 0; j < 2; j++) {
				x = (int)(Math.random() * 4);
				y = (int)(Math.random() * 4);
				z = (int)(Math.random() * 13);
				dealer_card[j] = card[x][y][z];
				if(dealer_card[j] == null) {
					while(dealer_card[j] == null) {
						x = (int)(Math.random() * 3);
						y = (int)(Math.random() * 3);
						z = (int)(Math.random() * 13);
						dealer_card[j] = card[x][y][z];
					}
				}
				card[x][y][z] = null;
			}
		}
		return new Result(dealer_card, card);
	}
}
class Result{
	//2개의 값 반환
	private String[][] mb_card;
	private String[][][] card;
	private String[] dear_card;
	private int sum;
	public Result(int sum) {
		this.sum = sum;
	}
	public Result(String[][] mb_card, String[][][] card) {
		this.mb_card = mb_card;
		this.card = card;
	}
	public Result(String[] dear_card, String[][][]card) {
		this.dear_card = dear_card;
		this.card = card;
	}
	public String[] getdear_card() {
		return dear_card;
	}
	public String[][] getmb_card() {
		return mb_card;
	}
	public String[][][] getcard(){
		return card;
	}
	public int getSum() {
		return sum;
	}
}