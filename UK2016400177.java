import java.util.*;
import java.io.*;

public class UK2016400177 {
	public static void main(String[]args)throws FileNotFoundException {

		String[] pieces = {"BTSH","BTSS","BTRH","BTRS","BSSH","BSSS","BSRH","BSRS","WTSH","WTSS","WTRH","WTRS","WSSH","WSSS","WSRH","WSRS"}; //Pieces are the pieces which will be used in the game.

		Random rand = new Random(); //This enables program to put pieces randomly and give pieces to the user randomly.

		Scanner console = new Scanner(System.in); //This enables to take input from the user.

		String[][] table = new String[4][4]; //Table is the game table which specifies where the pieces are.

		System.out.println("Do you want to start a new game or continue an ongoing game?(To start a new game, write \"new game\";To continue an ongoing game, write \"ongoing game\")");

		int startFromWhere = 0; //startFromWhere enables program to start an ongoing game properly.

		String[] last = new String[1]; //last enables program to start an ongoing game properly and to enter a loop just once.

		String gameType = starting(console); //gameType specifies whether user enter "new game" or "ongoing game".

		if(gameType.equalsIgnoreCase("new game")) {
			newGame(table,pieces,last,startFromWhere,console,rand);
		}
		else {
			ongoingGame(table,pieces,last,startFromWhere,console,rand);
		}
	}

	public static String starting(Scanner console) { //This method asks whether user wants to start a new game or play the ongoing game.

		String gameType = console.nextLine(); //gameType specifies whether user enter "new game" or "ongoing game".

		while(!(gameType.equalsIgnoreCase("new game")||gameType.equalsIgnoreCase("ongoing game")||gameType.equalsIgnoreCase("ongoýng game"))) {

			System.out.println("Please enter a valid statement.");

			gameType = console.nextLine();

		}
		return gameType;
	}

	public static void newGame(String[][] table,String[] pieces,String[] last,int startFromWhere,Scanner console,Random rand) throws FileNotFoundException{ //This method enables user to play a new game.

		PrintStream stream = new PrintStream(new File("input.txt")); //stream enables program to write tokens to the file.

		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				table[i][j]="E";
			}
		}

		printTable(table,pieces);			

		for(int i=1;i<=8;i++) {
			if(!computerRound(table,console,rand,pieces)) {
				startFromWhere = 0;
				quit(table,stream,startFromWhere,last);
				stream.close();
				break;}

			if(winningConditionHorizontal(table)||winningConditionVertical(table)||winningConditionLeftDiagonal(table)||winningConditionRightDiagonal(table)) {
				System.out.print("Computer has won.");
				break;
			}
			if(controlDraw(table)) {
				System.out.print("Draw.");
				break;
			}
			if(!userRound(table,console,rand,pieces,last,startFromWhere)) {
				startFromWhere = 1;
				quit(table,stream,startFromWhere,last);
				stream.close();
				break;}

			if(winningConditionHorizontal(table)||winningConditionVertical(table)||winningConditionLeftDiagonal(table)||winningConditionRightDiagonal(table)) {
				System.out.print("Player has won.");
				break;
			}
			if(controlDraw(table)) {
				System.out.print("Draw.");
				break;
			}
		}
	}

	public static void ongoingGame(String[][] table,String[] pieces,String[] last,int startFromWhere,Scanner console,Random rand) throws FileNotFoundException{ //This method enables user to play an ongoing game.

		Scanner input = new Scanner(new File("input.txt")); //input enables program to take input from the file.

		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				table[i][j] = input.next();
			}
		}

		startFromWhere = input.nextInt();

		last[0] = input.next();

		input.close();

		PrintStream stream = new PrintStream(new File("input.txt")); //stream enables program to write tokens to the file.

		printTable(table,pieces);	

		int count = startFromWhere; //count enables ongoing game process to work properly by making the program to enter true loop firstly.

		for(int i=1;i<=8;i++) {

			if(count == 0) {

				if(!computerRound(table,console,rand,pieces)) {
					startFromWhere = 0;
					quit(table,stream,startFromWhere,last);
					break;}

				if(winningConditionHorizontal(table)||winningConditionVertical(table)||winningConditionLeftDiagonal(table)||winningConditionRightDiagonal(table)) {
					System.out.print("Computer has won.");
					break;
				}
				if(controlDraw(table)) {
					System.out.print("Draw.");
					break;
				}

				count = 1;

			}
			if(count == 1) {																													
				if(!userRound(table,console,rand,pieces,last,startFromWhere)) {
					startFromWhere = 1;
					quit(table,stream,startFromWhere,last);
					break;}

				if(winningConditionHorizontal(table)||winningConditionVertical(table)||winningConditionLeftDiagonal(table)||winningConditionRightDiagonal(table)) {
					System.out.print("Player has won.");
					break;
				}
				if(controlDraw(table)) {
					System.out.print("Draw.");
					break;
				}
				count = 0;

			}
		}
	}

	public static void printTable(String[][] table,String[] pieces) { //This method prints the table to the console.

		for(int i=0;i<table.length;i++) {
			for(int j=0;j<table[i].length;j++) {									
				System.out.print(table[i][j]+"    ");
			}
			System.out.println();			
		}
		System.out.print("Remainings: ");
		for(int i=0;i<16;i++) {
			int count = 0;
			for(int j=0;j<4;j++) {
				for(int k=0;k<4;k++) {
					if(!table[j][k].equals(pieces[i])) {
						count++;
					}
				}
			}
			if(count==16) {
				System.out.print(pieces[i]+" ");
			}
		}
		System.out.println();
	}

	public static boolean computerRound(String[][] table,Scanner console,Random rand,String[] pieces) { //This method enables computer to place a piece to the table.

		String piece = userGives(table,console); //piece is the piece user gives.

		if(piece.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt")) {
			return false;
		}

		while(!computerPlaces(table,rand,piece)) {

		}

		printTable(table,pieces);

		return true;
	}

	public static boolean userRound(String[][] table,Scanner console,Random rand,String[] pieces,String[] last,int startFromWhere) { //This method enables user to place a piece to the table.		

		String piece; //piece is the piece computer gives.
		if(startFromWhere == 1&&!(last[0]=="A")) {
			piece = last[0];
			last[0] = "A";
			System.out.print("The piece which computer has given to you is "+piece+"\nWhere will you put the piece?Please enter the coordinates(x,y)(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

		}	
		else {
			piece = computerGives(table,rand,pieces);
		}

		boolean valid = userPlaces(table,console,piece,last); //valid controls whether user enter "quit" or not.

		printTable(table,pieces);

		return valid;
	}

	public static String userGives(String[][] table,Scanner console) { //This method enables user to give a piece to computer.

		System.out.print("Please pick a piece.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

		String piece = console.next(); //piece is the piece user gives to the computer.

		if(!(piece.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt"))){

			while(!(piece.equals("BTSH")||piece.equals("BTSS")||piece.equals("BTRH")||piece.equals("BTRS")||piece.equals("BSSH")||piece.equals("BSSS")||piece.equals("BSRH")||piece.equals("BSRS")||piece.equals("WTSH")||piece.equals("WTSS")||piece.equals("WTRH")||piece.equals("WTRS")||piece.equals("WSSH")||piece.equals("WSSS")||piece.equals("WSRH")||piece.equals("WSRS"))) {

				System.out.print("Please enter a valid piece.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

				piece = console.next();

				if(piece.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt")) {
					return piece;
				}
			}

			for(int i=0;i<table.length;i++) {
				for(int j=0;j<table[i].length;j++) {

					while(table[i][j].equals(piece)) {

						i=0;

						j=0;

						System.out.print("Please enter an unused piece.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

						piece = console.next();

						if(piece.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt")) {
							return piece;
						}

						while(!(piece.equals("BTSH")||piece.equals("BTSS")||piece.equals("BTRH")||piece.equals("BTRS")||piece.equals("BSSH")||piece.equals("BSSS")||piece.equals("BSRH")||piece.equals("BSRS")||piece.equals("WTSH")||piece.equals("WTSS")||piece.equals("WTRH")||piece.equals("WTRS")||piece.equals("WSSH")||piece.equals("WSSS")||piece.equals("WSRH")||piece.equals("WSRS"))) {

							System.out.print("Please enter a valid piece.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

							piece = console.next();

							if(piece.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt")) {
								return piece;
							}
						}
					}
				}
			}
		}
		return piece;
	}

	public static boolean computerPlaces(String[][] table,Random rand,String piece) { //This method enables computer to place a piece which user gives to the table.

		int x = rand.nextInt(4); //x is the first element of the matrix in which piece will be put.

		int y = rand.nextInt(4); //x is the second element of the matrix in which piece will be put.

		if(table[x][y].equals("E")) {

			table[x][y] = piece;
			return true;
		}
		else {
			return false;
		}
	}

	public static String computerGives(String[][] table,Random rand,String[] pieces) { //This method enables computer to give a piece to user.

		int randomNumber = rand.nextInt(16); //randomNumber enables program to give a random piece to the user.

		String piece = pieces[randomNumber]; //piece is the piece which computer gives to player.		

		for(int i=0;i<table.length;i++) {
			for(int j=0;j<table[i].length;j++) {

				while(table[i][j].equals(piece)) {						

					i=0;

					j=0;

					randomNumber = rand.nextInt(16);

					piece = pieces[randomNumber];
				}
			}
		}

		System.out.print("The piece which computer has given to you is "+piece+"\nWhere will you put the piece?Please enter the coordinates(x,y)(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

		return piece;
	}

	public static boolean userPlaces(String[][] table,Scanner console,String piece,String[] last) { //This method enables user to place a piece which computer gives to the table.

		boolean valid = false; //valid enables program to take true input from the user.
		int count = 0;//count controls whether user enter true input and "quit" or not.
		int x=0; //x is the first element of the matrix in which piece will be put.
		int y=0; //x is the second element of the matrix in which piece will be put.
		while(!valid) {
			if(console.hasNextInt()) { 

				valid = true;

				x = console.nextInt();

				y = console.nextInt();

				count--;

				if(!(x<=3 && x>=0 && y>= 0 && y<=3 && table[x][y].equals("E"))) {

					System.out.print("Please enter a valid coordinate.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");

					valid = false;

					count++;
				}		
			}
			else {

				valid = true;

				String quit = console.next(); //quit controls whether user enter "quit" or not.

				count++;

				if(!(quit.equalsIgnoreCase("quit")||piece.equalsIgnoreCase("quýt"))) {
					System.out.print("Please enter a valid coordinate.(Enter \"quit\" to exit.If you close game without entering \"quit\",game will not be saved.)");
					count--;
					valid = false;
				}		
			}
		}

		if(count==-1) {			
			table[x][y]=piece;
		}
		if(count==1) {											
			last[0] = piece;
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean winningConditionHorizontal(String[][] table) { //This method controls whether player or computer win the game horizontally.

		int[][] win = new int[4][4]; //win is the numbers of same characters of each piece in the same horizontal line.

		for(int k=0;k<4;k++) {

			String statement = table[k][k]; //statement is a piece which will be compared with other pieces in the same horizontal line and enables program to check whether user or player win the game.

			for(int i=0;i<4;i++) {

				for(int j=0;j<4;j++) {

					if(!(table[k][j].equals("E")||statement.equals("E"))) {

						if(table[k][j].charAt(i) == statement.charAt(i)) {
							win[k][i]++;
						}
					}																				
				}
			}
		}

		for(int k=0;k<table.length;k++) {
			for(int i=0;i<table.length;i++) {
				if(win[k][i]==4) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean winningConditionVertical(String[][] table) { //This method controls whether player or computer win the game vertically.

		int[][] win = new int[4][4]; //win is the numbers of same characters of each piece in the same vertical line.

		for(int k=0;k<4;k++) {

			String statement = table[k][k]; //statement is a piece which will be compared with other pieces in the same vertical line and enables program to check whether user or player win the game.

			for(int i=0;i<4;i++) {

				for(int j=0;j<4;j++) {

					if(!(table[j][k].equals("E")||statement.equals("E"))) {

						if(table[j][k].charAt(i) == statement.charAt(i)) {
							win[k][i]++;
						}
					}																				
				}
			}
		}

		for(int k=0;k<table.length;k++) {
			for(int i=0;i<table.length;i++) {
				if(win[k][i]==4) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean winningConditionLeftDiagonal(String[][] table) { //This method controls whether player or computer win the game left diagonally.

		int[] win = new int[4]; //win is the numbers of same characters of each piece in the left diagonal.

		String statement = table[0][0]; //statement is a piece which will be compared with other pieces in the left diagonal and enables program to check whether user or player win the game.

		for(int k=0;k<4;k++) {

			for(int i=0;i<4;i++) {

				for(int j=0;j<4;j++) {

					if(i==j) {

						if(!(table[i][j].equals("E")||statement.equals("E"))) {

							if(table[i][j].charAt(k) == statement.charAt(k)) {
								win[k]++;
							}
						}
					}
				}
			}
		}

		for(int k=0;k<4;k++) {
			if(win[k]==4) {
				return true;
			}
		}
		return false;
	}

	public static boolean winningConditionRightDiagonal(String[][] table) { //This method controls whether player or computer win the game right diagonally.

		int[] win = new int[4]; //win is the numbers of same characters of each piece in the right diagonal.

		String statement = table[0][3]; //statement is a piece which will be compared with other pieces in the right diagonal and enables program to check whether user or player win the game.

		for(int k=0;k<4;k++) {

			for(int i=0;i<4;i++) {

				for(int j=0;j<4;j++) {

					if(i+j==3) {

						if(!(table[i][j].equals("E")||statement.equals("E"))) {

							if(table[i][j].charAt(k) == statement.charAt(k)) {
								win[k]++;
							}
						}
					}
				}
			}
		}

		for(int k=0;k<4;k++) {
			if(win[k]==4) {
				return true;
			}
		}
		return false;
	}

	public static void quit(String[][] table,PrintStream stream,int startFromWhere,String[] last) { //This method controls whether user enter "quit" or not.If user enter "quit",game is saved and terminated.
		for(int i=0;i<table.length;i++) {
			for(int j=0;j<table[i].length;j++) {									
				stream.print(table[i][j]+" ");
			}
			stream.println();			
		}
		stream.print(startFromWhere);
		stream.println();
		stream.print(last[0]);
	}

	public static boolean controlDraw(String[][] table) { //This method controls whether there is a draw situation or not.
		int count =0; //count is the number of pieces different from "E" in the table.
		for(int i=0;i<table.length;i++) {
			for(int j=0;j<table[i].length;j++) {
				if(!(table[i][j].equals("E"))) {
					count++;
				}
			}
		}
		if(count == 16) {
			return true;
		}
		else {
			return false;
		}
	}			
}