import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Board{
	public static final int ME = 1;
	public static final int OPPONENT = -1;
	public static final int BORDER = -2;
	public static final int EMPTY = 0;
	private static final int ENDGAMEFACTOR = 1000;
	private String color;
	private int player;
	private Move move;
	private static final String[] printBoard = new String[100];
	private static final int[] board = new int[100];
	private Board oldBoard;
	private ArrayList<Move> moves = new ArrayList<>();
	private int[] directions = {-1, -11, -10, -9, 1, 11, 10, 9};
	private String space;
	private char[] letters = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', ' '};
	private String columns = new String(letters);
	private Scanner input = new Scanner(System.in);
	private String col;
	private String row;
	private String[] colors;
	private int index;

	public Board(String color){
		this.color = color;

		for (int i = 0; i<board.length; i++){
			board[i] = EMPTY;
		}
		
		for (int i = 0; i<board.length/10; i++){
			board[i] = BORDER;
		}
		
		for (int i = 0; i<board.length; i+=10){
			board[i] = BORDER;
		}

		for (int i = board.length/10-1; i<board.length; i+=10){
			board[i] = BORDER;
		}

		for (int i = board.length-10; i<board.length; i++){
			board[i] = BORDER;
		}

		if (color == "B"){
			board[45] = ME;
			board[54] = ME;
			board[44] = OPPONENT;
			board[55] = OPPONENT;
		}

		if (color == "W"){
			board[45] = OPPONENT;
			board[54] = OPPONENT;
			board[44] = ME;
			board[55] = ME;
		}

	}

	public Board(Board oldBoard){
		// makes copy of currentBoard
		this(oldBoard.getColor());
	}

	public String getColor(){
		return color;
	}

	public ArrayList<Move> generateMoves(int player){
		moves.clear();
		for (int i = 0; i<board.length; i++){
			if (board[i] == EMPTY){
				for (int j = 0; j<directions.length; j++){
					if (board[i + directions[j]] == player*-1){
						int n = 0;
						while (board[i + (directions[j] + directions[j]*n)] != EMPTY && 
							board[i + (directions[j] + directions[j]*n)] != BORDER){
							if (board[i + (directions[j] + directions[j]*n)]
								== player && !moves.contains(new Move(i))){
								moves.add(new Move(i));
							}
						n++;
						}
					}
				}
			}
		}
		return moves;
	}

	public boolean gameOver(){
		if (generateMoves(ME).isEmpty() && generateMoves(OPPONENT).isEmpty())
			return true;
		return false;
	}

	public String getCurrentColor(String[] colors, int index){
		return colors[index];
	}

	public Move getOpponent(){
		ArrayList<Move> opponentMoves = generateMoves(OPPONENT);
		int r = 0;
		int c = 0;
		int i = 0;
		System.out.println("C Enter a move: ");
		String opponentMove = input.nextLine();
		if (opponentMove.equals("E")){
			System.exit(0);
		}

		if (opponentMove.length() == 5){
			col = opponentMove.substring(2,3);
			row = opponentMove.substring(4,5);
			c = columns.indexOf(col);
			r = Integer.parseInt(row);
			i = r*10 + c;
			Move m = new Move(i);
			if (!opponentMoves.contains(m)){
				System.out.println("C Not a valid move");
				return getOpponent();
			}
			move = new Move(i);
		}
		if (opponentMove.length() == 1){
			move = new Move(0);
		}

		return move;
	}

	/*
	public double evaluate(){
		ArrayList<Move> myMoves = generateMoves(ME);
		ArrayList<Move> opponentMoves = generateMoves(OPPONENT);
		double currentMobility = (myMoves.length - opponentMoves.length)/(myMoves.length + opponentMoves.length);
		double potentialMobility1 = ()
	}
	*/


	public double evaluate(){
		ArrayList<Move> myMoves = generateMoves(ME);
		ArrayList<Move> opponentMoves = generateMoves(OPPONENT);
		int netPieces = numOfPieces(ME) - numOfPieces(OPPONENT);
		if (gameOver() == true){
			if (netPieces == 0)
				return ENDGAMEFACTOR;
			return netPieces*ENDGAMEFACTOR;
		}
		else
			return netPieces;
	}

	/*
	* 
	*/
	public Move alphaBeta(Board currentBoard, int ply, int player, double alpha, double beta, int maxDepth){
		System.out.println("C enter alpha beta");
		if (ply >= maxDepth){
			Move returnMove = new Move();
			returnMove.setValue(currentBoard.evaluate());
			System.out.println("C level reached: " + ply);
			return returnMove;
		}
		else{
			/*
			* 1. generate moves for player
			* 2. if moveList is empty, add passMove to moveList
			* 3. bestMove = moveList.get(0)
			* 4. for each move in moveList
			*    a.) newBoard = currentBoard.applyMove(player, move)
			*    b.) tempMove = -alphaBeta(newBoard, -1*player, ply+1, -1*beta, -1*alpha, maxdepth)
			*    c.) move.value = -1*tempMove.value
			*    d.) if moveValue > alpha
			*       c0) bestMove = move
			*       c1) alpha = moveValue
			*       c2) if alpha > beta
			*              return bestMove
			* 5. return bestMove
			*/
			ArrayList<Move> moves = currentBoard.generateMoves(player);
			//printMoves();
			if (moves.isEmpty()) moves.add(new Move()); //add pass move if empty
			Move bestMove = moves.get(0);
			for (Move move : moves){
				Board newBoard = new Board(currentBoard);
				newBoard.applyMove(player, move);
				printMoves(moves);
				Move tempMove = alphaBeta(newBoard, ply+1, -1*player, -1*beta, -1*alpha, maxDepth);
				move.setValue(-1*tempMove.getValue());
				if (move.getValue() > alpha){
					bestMove = move;
					alpha = move.getValue();
					if (alpha > beta)
						return bestMove;
				}

			}
			return bestMove;
		}
	}

	/*
	* return number of pieces by empty space
	*/

	/*
	public int piecesByEmptySpace(int player){
		int piecesByEmptySpace = 0;
		for (int i = 0; i<board.length; i++){
			if (board[i] == player){
				int n = 0;
				while (board[i + directions[n]] != EMPTY){
					n++;
				}
				if (board[i + directions[n]] == EMPTY){
					piecesByEmptySpace++;
				}
			}
			
		}
		return piecesByEmptySpace;
	}
	*/

	public Move getMyMove(){
		/*
		* double alpha = Double.MIN_VALUE;
		* double beta = Double.MAX_VALUE;
		* int maxDepth = 2;
		* Move bestMove = new Move();
		* while (!OthelloGame.timeUp){
		* 		Move tempMove = alphaBeta(this, 0, 1, alpha, beta, maxDepth);
		*		bestMove = tempMove;
		*		maxDepth +=2;
		}
		* return bestMove;
		* check that this works without loop first (just check where it does maxDepth =2 every time)
		* 
		*/
		double alpha = Double.MIN_VALUE;
		double beta = Double.MAX_VALUE;
		int maxDepth = 4;
		//Move bestMove = new Move();
		Move bestMove = alphaBeta(this, 0, 1, alpha, beta, maxDepth);

		/*
		while(!OthelloGame.timeUp){
			Move tempMove = alphaBeta(this, 0, 1, alpha, beta, maxDepth);
			bestMove = tempMove;
			maxDepth += 2;
		}
		*/
		
		return bestMove;

		/*
		ArrayList<Move> myMoves = generateMoves(ME);
		if (myMoves.size() == 0){
			move = new Move(0);
		}
		else{
			Random gen = new Random();
			int index = gen.nextInt(myMoves.size());
			move = myMoves.get(index);
		}
		return move;
		*/
	}

	public void applyMove(int player, Move move){
		if (move.getMove() != 0){
			board[move.getMove()] = player;
			for (int j = 0; j<directions.length; j++){
				if (board[move.getMove() + directions[j]] == player*-1){
					int n = 0;
					while (board[move.getMove() + (directions[j] + directions[j]*n)] != BORDER &&
						board[move.getMove() + (directions[j] + directions[j]*n)] != EMPTY){
						if (board[move.getMove() + (directions[j] + directions[j]*n)] == player){
							int m = 0;
							while (board[move.getMove() + (directions[j] + directions[j]*m)] != player &&
								board[move.getMove() + (directions[j] + directions[j]*m)] != EMPTY){
								board[move.getMove() + (directions[j] + directions[j]*m)] = player;
							m++;
							}
						}
					n++;
					}
				}
			}
		}
	}

	public void printBoard(){

		System.out.print("C   ");
		for (int i = 0; i<letters.length; i++){
			System.out.print(letters[i] + " ");
		}
		System.out.println();
		for (int i = 0; i<board.length; i++){
			if (i%10 == 0){
				System.out.print("C ");
				if (i/10 == 0 || i/10 == 9)
					System.out.print("  ");
				else{
					System.out.print(i/10 + " ");
				}
			}
			if (board[i] == BORDER){
					space = "X";
				}
				if (board[i] == EMPTY){
					space = "*";
				}
				if (color == "B"){
					if (board[i] == ME){
						space = "B";
					}
					if (board[i] == OPPONENT){
						space = "W";					}
					
				}
				if (color == "W"){
					if (board[i] == ME){
						space = "W";
					}
					if (board[i] == OPPONENT){
						space = "B";
					}
					
				}
				printBoard[i] = space;
				System.out.print(printBoard[i] + " ");
			if (i%10 == 9){
				System.out.println();
			}
		}
		System.out.println("C ");
	}

	public int numOfPieces(int player){
		int n = 0;
		for (int i = 0; i<board.length; i++){
			if (board[i] == player)
				n++;
		}
		return n;
	}

	public void printMoves(){
		System.out.print("C ");
		for (int i = 0; i<moves.size(); i++){
			System.out.print(moves.get(i) + " ");
		}
		System.out.println();
	}

	public void printMoves(ArrayList<Move> moves){
		System.out.print("C ");
		for (int i = 0; i<moves.size(); i++){
			System.out.print(moves.get(i) + " ");
		}
		System.out.println();
	}
	
	public void printMove(String[] colors, int index, Move move){
		int r = move.getMove()/10;
		int c = move.getMove()%10;
		char letter = letters[c];
		String color = colors[index];
		if (move.getMove() == 0){
			System.out.printf("%s\n", color);
		}
		System.out.printf("%s %c %d\n", color, letter, r);
	}

	public static void main (String [] args){
		
	}
}