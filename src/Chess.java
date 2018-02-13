import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Chess {
	
	// HEY!!! Change these two numbers to make the screen fit 
	private static final int THE_SCREEN_WIDTH = 640;
	private static final int THE_SCREEN_HEIGHT = 640;
	int computerSearchDepth = 3;
	
	WhichPlayer whoseTurn;
	WhichPlayer whoWon;
	boolean whiteIfComputer;
	boolean blackIfComputer;
	
	//FIRST ARRAY IS THE COLUMN NUMBER, SECOND IS THE ROW NUMBER
	/*
	 * [[P, [P, [P, [P, [P, [P, [P, [P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P,  P,  P,  P,  P,  P,  P,  P
	 *   P], P], P], P], P], P], P], P]]
	 * 
	 * top-right is board[7][0];
	 * bottom-left is board[0][7]
	 * 
	 * exactly like the image coordinate system
	 * 
	 * */
	
	Piece[][] board;
	PaintedGame pg;
	
	// Constructor
	public Chess(boolean ifWhiteCom, boolean ifBlackCom) {
		
		whiteIfComputer = ifWhiteCom;
		blackIfComputer = ifBlackCom;
		
		board = new Piece[8][8];
		whoseTurn = WhichPlayer.WHITE;
		whoWon = WhichPlayer.NONE;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				WhichPlayer wp;
				
				if (j < 2)
					wp = WhichPlayer.BLACK;
				else if (j > 5)
					wp = WhichPlayer.WHITE;
				else
					wp = WhichPlayer.NONE;
				
				PieceNames pn;
				
				if (j == 1 || j == 6)
					pn = PieceNames.PAWN;
				else if (j == 0 || j == 7) {
					if (i == 0 || i == 7)
						pn = PieceNames.ROOK;
					else if (i == 1 || i == 6)
						pn = PieceNames.KNIGHT;
					else if (i == 2 || i == 5)
						pn = PieceNames.BISHOP;
					else if (i == 3)
						pn = PieceNames.QUEEN;
					else if (i == 4)
						pn = PieceNames.KING;
					else {
						//error
						System.out.print("THE FOR LOOP INITIALIZING THE BOARD IS REACHING OUT OF BOUNDS");
						pn = PieceNames.NONE;
					}
				}
				else
					pn = PieceNames.NONE;
				
				board[i][j] = new Piece(pn, wp, new Position(i, j), false, false);
			}
		}
		pg = new PaintedGame(board, THE_SCREEN_WIDTH, THE_SCREEN_HEIGHT);
	}
	
	public WhichPlayer play() {
		
		while (whoWon == WhichPlayer.NONE) {
			// draw the game with the board facing whose turn it is
			
			drawGame(whoseTurn);
			//do a turn
			if (handleTurn(whoseTurn))
				whoWon = whoseTurn;
			
			switchTurn();
		}
		
		// TODO
		// Winning stuff yanno
		drawGame(whoseTurn);
		
		return whoseTurn;
	}
	
	private void drawGame(WhichPlayer wp) {
		//Take in board and ifWhite? and draw the game
		if (ifComputer(wp))
			pg.drawGame(board,  otherPlayer(wp));
		else
			pg.drawGame(board, wp);
		
//		for (int j = 0; j < board[0].length; j++) {
//			for (int i = 0; i < board.length; i++) {
//				Piece piece = board[i][j];
//				char p = ' ';
//				switch(piece.getPieceName()) {
//				case PAWN:
//					p = 'p';
//					break;
//				case KNIGHT:
//					p = 'k';
//					break;
//				case BISHOP:
//					p = 'b';
//					break;
//				case ROOK:
//					p = 'R';
//					break;
//				case QUEEN:
//					p = 'Q';
//					break;
//				case KING:
//					p = 'K';
//					break;
//				case NONE:
//					p = '.';
//					break;
//				default:
//					p = ' ';
//					break;
//				}
//				
//				System.out.print(p + " ");
//			}
//			System.out.print("\n");
//		}
	}
	
	// Returns if the player won on his/her turn
	private boolean handleTurn(WhichPlayer whichPlayer) {
		boolean ifValidMove = false;
		Move move = null;
		while (!ifValidMove) {
			// Take in move (by input or computer?)
			move = handleMove(whichPlayer);
			// Check if it's a valid move
			// sees if it can place it on the board, then
			ifValidMove = checkMove(board, whichPlayer, move);
			// if it returns true, then it continues, else it loops and asks the player a-fucking-gain
			drawGame(whichPlayer);
		}
		// Change the board accordingly
		// Check if the board is a win and return the value
		
		return processMove(whichPlayer, move);
	}
	
	// Actually takes in the move that the player/computer suggests
	private Move handleMove(WhichPlayer wp) {
		Move potentialMove = null;
//		
//		Scanner s = new Scanner(System.in);
//		
//		int x = Integer.parseInt(s.nextLine());
//		int y = Integer.parseInt(s.nextLine());
//		
//		Piece piece = board[x][y];
//		
//		x = Integer.parseInt(s.nextLine());
//		y = Integer.parseInt(s.nextLine());
//		
//		potentialMove = new Move(piece, new Position(x, y));
		
		if (!ifComputer(wp)) {
			// Takes move from the JFrame/JPanel thing
			while(potentialMove == null) {
				potentialMove = pg.getMove();
			}
			
			pg.doneMove();
		}
		else {
			// Computer does a move
			potentialMove = createComputerMove(wp);
		}
		
		return potentialMove;
	}
	
	private boolean ifComputer(WhichPlayer wp) {
		if (wp == WhichPlayer.WHITE)
			return whiteIfComputer;
		else
			return blackIfComputer;
	}
	
	// Checks if the move can actually happen
	private boolean checkMove(Piece[][] b, WhichPlayer wp, Move potentialMove) {
		boolean ifLegalMove = false;
		Piece piece = potentialMove.getPiece();
		
		Move[] potentialMoves = piece.getValidMoves(b);
		
		//System.out.println(Integer.toString(potentialMove.pf.x) + Integer.toString(potentialMove.pf.y));
		//Check if this potentialMove is a part of the valid moves of the piece
		for (int i = 0; i < potentialMoves.length; i++) {
			if (potentialMove.getEnd().getX() == potentialMoves[i].getEnd().getX()
					&& potentialMove.getEnd().getY() == potentialMoves[i].getEnd().getY()) {
				ifLegalMove = true;
				potentialMove.setSpecialMove(potentialMoves[i].getSpecialMove());
			}
		}
		
		// Contains doesn't work on primitives
		// ifLegalMove = Arrays.asList(potentialMoves).contains(potentialMove);
		
		if (piece.getWhichPlayer() != wp)
			ifLegalMove = false;
		
		if (potentialMove.getSpecialMove() == SpecialMoves.ENPASSENT)
			System.out.println("enpassent");
		
		if (potentialMove.getSpecialMove() == SpecialMoves.CASTLE)
			System.out.println("castling");
		
		// This code doesn't work
		if (ifLegalMove) {
			Piece[][] potentialBoard = changeBoard(b, potentialMove);
		
			//Check all of black's moves and see if any of them kill the king
			Piece king = null;
			
			for (int i = 0; i < potentialBoard[0].length; i++) {
				for (int j = 0; j < potentialBoard.length; j++) {
					Piece p = potentialBoard[i][j];
					if (p.getPieceName() == PieceNames.KING
						&& (p.getWhichPlayer() == wp)) {
						king = p;
						i = potentialBoard[0].length;
						j = potentialBoard.length;
					}
				}
			}
			
			Position kPos = king.getPosition();
			
			ifLegalMove = !ifThreatened(wp, potentialBoard, kPos);
			
			//Check if castling is allowed here
			
			if (potentialMove.getSpecialMove() == SpecialMoves.CASTLE) {
				//Both inclusive
				int x0, xf;
				int y = potentialMove.getFirst().getY();
				
				if ((potentialMove.getFirst().getX() - potentialMove.getEnd().getX()) < 0) {
					//Short castling
					x0 = potentialMove.getFirst().getX();
					xf = 7;
				}
				else {
					//Long castling
					x0 = 0;
					xf = potentialMove.getFirst().getX();
				}
				
				for (int i = x0; i < (xf + 1); i++) {
					if (ifThreatened(wp, potentialBoard, new Position(i, y))) {
						ifLegalMove = false;
						i = 8;
					}
				}
			}
		}
		
		//System.out.print(ifLegalMove + "\n");
		return ifLegalMove;
	}
	
	// Returns true if the space is threatened by another piece
	private boolean ifThreatened(WhichPlayer wp, Piece[][] board, Position p) {
		boolean ifThreatenedVar = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				Piece piece = board[i][j];
				if (piece.getWhichPlayer() != wp && piece.getWhichPlayer() != WhichPlayer.NONE) {
					Move[] potKillMoves = piece.getValidMoves(board);
					for (int m = 0; m < potKillMoves.length; m++) {
						if (potKillMoves[m].getEnd().getX() == p.getX() && potKillMoves[m].getEnd().getY() == p.getY()) {
							ifThreatenedVar = true;
							m = potKillMoves.length;
							j = board.length;
							i = board.length;
						}
					}
				}
			}
		}
		
		return ifThreatenedVar;
	}
	
	// basically returns the value of checkBoard
	private boolean processMove(WhichPlayer wp, Move move) {
		//Change the bloody board
		board = changeBoard(board, move);
		//Check if this means that you won
		return checkBoard(wp);
	}
	
	// Returns if the board is a winning position for the player
	// Assumes that it is the other player's turn
	private boolean checkBoard(WhichPlayer wp) {
		boolean ifWon = true;
		//Check all the shits to see if there is a winner
		//Checks all the other player's
		
		WhichPlayer op;
		if (wp == WhichPlayer.WHITE) op = WhichPlayer.BLACK;
		else op = WhichPlayer.WHITE;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				Piece piece = board[i][j];
				if (piece.getWhichPlayer() == op) {
					Move[] potMoves = piece.getValidMoves(board);
					for (int m = 0; m < potMoves.length; m++) {
						if (checkMove(board, op, potMoves[m])) {
							ifWon = false;
							m = potMoves.length;
							j = board.length;
							i = board.length;
						}
					}
				}
			}
		}
		
		return ifWon;
	}
	
	// return the board from the move given
	// don't work with the class variables at all ya goon
	private Piece[][] changeBoard(Piece[][] b, Move move) {
		// Moves the piece no matter what and eats
		// anything in its way, because we already checked
		// that this is a legal move, trust me
		Piece[][] changedBoard = new Piece[b.length][b[0].length];
		
		Position p0 = move.getFirst();
		Position pf = move.getEnd();
		Piece movePiece = move.getPiece();
		
		boolean db = false;
		
		if (move.getSpecialMove() == SpecialMoves.DOUBLEJUMP)
			db = true;
		
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b.length; j++) {
				Piece piece = b[i][j];
				if (i == p0.getX() && j == p0.getY())
					changedBoard[i][j] = new Piece(PieceNames.NONE, WhichPlayer.NONE, new Position(i, j), false, false);
				else if (i == pf.getX() && j == pf.getY()) {
					changedBoard[i][j] = new Piece(movePiece.getPieceName(), movePiece.getWhichPlayer(), new Position(i, j), true, db);
				}
				else
					changedBoard[i][j] = new Piece(piece.getPieceName(), piece.getWhichPlayer(), new Position(i, j), piece.getIfMoved(), false);
			}
		}
		
		if (move.getSpecialMove() == SpecialMoves.ENPASSENT)
			changedBoard[pf.getX()][p0.getY()] = new Piece(PieceNames.NONE, WhichPlayer.NONE, new Position(pf.getX(), p0.getY()), false, false);
		
		if (move.getSpecialMove() == SpecialMoves.CASTLE) {
			if ((p0.getX() - pf.getX()) < 0) {
				//Short castling
				changedBoard[7][p0.getY()] = new Piece(PieceNames.NONE, WhichPlayer.NONE, new Position(pf.getX(), p0.getY()), false, false);
				changedBoard[pf.getX() - 1][p0.getY()] = new Piece(PieceNames.ROOK, movePiece.getWhichPlayer(), new Position(pf.getX() - 1, p0.getY()), true, false);
			}
			else {
				//Long castling
				changedBoard[0][p0.getY()] = new Piece(PieceNames.NONE, WhichPlayer.NONE, new Position(pf.getX(), p0.getY()), false, false);
				changedBoard[pf.getX() + 1][p0.getY()] = new Piece(PieceNames.ROOK, movePiece.getWhichPlayer(), new Position(pf.getX() + 1, p0.getY()), true, false);
			}
		}
		
		return changedBoard;
	}
	
	private void switchTurn() {
		if (whoseTurn == WhichPlayer.WHITE)
			whoseTurn = WhichPlayer.BLACK;
		else
			whoseTurn = WhichPlayer.WHITE;
	}
	
	// Do i need this?
	private class MinimaxNode {
		private Piece[][] board;
		private Move moveToHere;
		WhichPlayer wp;
		double value;
		
		MinimaxNode(Piece[][] b, Move m, WhichPlayer wp) {
			board = b;
			moveToHere = m;
			this.wp = wp;
			value = Integer.MIN_VALUE;
		}
		
		public MinimaxNode calcValue() {
			value = valueOfBoard(board, wp);
			return this;
		}
		
//		public MinimaxNode calcMin(int depth) {
//			
//			if (depth == 0) {
//				return calcValue();
//			}
//			
//			MinimaxNode[] children = createMinimaxNodes(this, otherPlayer(wp));
//			MinimaxNode value = null;
//			
//			for (int i = 0; i < children.length; i++) {
//				MinimaxNode max = children[i].calcMax(depth - 1);
//				if (value == null || (max != null && max.value < value.value))
//					value = max;
//					
//			}
//			
//			return value;
//		}
//		
//		public MinimaxNode calcMax(int depth) {
//			
//			if (depth == 0) {
//				return calcValue();
//			}
//			
//			MinimaxNode[] children = createMinimaxNodes(this, otherPlayer(wp));
//			MinimaxNode value = null;
//			
//			for (int i = 0; i < children.length; i++) {
//				MinimaxNode min = children[i].calcMin(depth - 1);
//				if (value == null || (min != null && min.value > value.value))
//					value = min;
//					
//			}
//			
//			return value;
//		}
		
		public MinimaxNode minimax(int depth, boolean ifMax) {
			if (depth == 0)
				return calcValue();
			
			MinimaxNode[] children = createMinimaxNodes(this, otherPlayer(wp));
			MinimaxNode value = null;
			
			for (int i = 0; i < children.length; i++) {
				MinimaxNode minormax = children[i].minimax(depth - 1, !ifMax);
				if (ifMax && (value == null || (minormax != null && minormax.value > value.value)))
						value = minormax;
				else if (!ifMax && (value == null || (minormax != null && minormax.value < value.value)))
					value = minormax;
			}
			
//			Vector<MinimaxNode> nodeList = new Vector<MinimaxNode>();
//			
//			for (int i = 0; i < children.length; i++) {
//				MinimaxNode minormax = children[i].minimax(depth - 1, !ifMax);
//				if (minormax.value == value.value)
//					nodeList.add(children[i]);
//			}
//			
//			int randomNum = ThreadLocalRandom.current().nextInt(0, nodeList.size());
//			value = nodeList.toArray(new MinimaxNode[nodeList.size()])[randomNum];
			
			return value;
		}
		
		public Move getMove() {
			return moveToHere;
		}
		
		public Piece[][] getBoard() {
			return board;
		}
	}
	
	private Move createComputerMove(WhichPlayer wp) {
		Move computedMove = null;
		
		//TODO use minimax algorithm
		
		// First maximize, then minimize, then max.... etc...
		MinimaxNode current = new MinimaxNode(board, null, otherPlayer(wp));
		computedMove = current.minimax(computerSearchDepth, true).getMove();
		
		return computedMove;
	}
	
	// Create a list of the next minimax nodes from potential boards
	private MinimaxNode[] createMinimaxNodes(MinimaxNode node, WhichPlayer wp) {
		Vector<MinimaxNode> nodeList = new Vector<MinimaxNode>();
		Piece[][] b = node.getBoard(); // PLZ don't alter
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j].getWhichPlayer() == wp) {
					Move[] potMoveList = board[i][j].getValidMoves(b);
					for (int m = 0; m < potMoveList.length; m++) {
						if (checkMove(b, wp, potMoveList[m]))
							nodeList.add(new MinimaxNode(changeBoard(b, potMoveList[m]), potMoveList[m], wp));
					}
				}
			}
		}
		
		return nodeList.toArray(new MinimaxNode[nodeList.size()]);
	}
//	
//	private MinimaxNode minimax(MinimaxNode[] nodes, boolean ifMax) {
//		for (int i = 0; i < nodes.length; i++) {
//			nodes[i].setValue(valueOfBoard(nodes[i].board, wp));
//		}
//		
//		// The most (max/min) list (all same values)
//		Vector<MinimaxNode> nodeList = new Vector<MinimaxNode>();
//		int value;
//		
//		if(ifMax) {
//			value = -999999;
//			// Return the best value in the list
//			for (int i = 0; i < nodes.length; i++) {
//				if (nodes[i].getValue() > value)
//					value = nodes[i].getValue();
//			}
//		}
//		
//		else {
//			value = 999999;
//			// Return the least value in this list
//			for (int i = 0; i < nodes.length; i++) {
//				if (nodes[i].getValue() < value)
//					value = nodes[i].getValue();
//			}
//		}
//		// random if multiple with the same value
//		// which there definitely will be, especially at the beginning
//		for (int i = 0; i < nodes.length; i++) {
//			if (nodes[i].getValue() == value)
//				nodeList.add(nodes[i]);
//		}
//		
//		int randomNum = ThreadLocalRandom.current().nextInt(0, nodeList.size());
//		resultNode = nodeList.toArray(new MinimaxNode[nodeList.size()])[randomNum];
//	}
//	
//	private MinimaxNode minimax(MinimaxNode[] nodes, int depth, WhichPlayer wp, boolean ifMax) {
//		MinimaxNode resultNode = null;
//		
//		// REMEMBER TO SWITCH THE WP, IFMAX, and DEPTH variables as you all minimax deeper
//		
//		if (nodes.length == 0) {
//			// THE MOST extreme SITUATION, because it means someone lost
//			//resultNode = new MinimaxNode()
//		}
//		
//		// at the end of the search depth
//		if (depth == 0) {
//			for (int i = 0; i < nodes.length; i++) {
//				nodes[i].setValue(valueOfBoard(nodes[i].board, wp));
//			}
//			
//			// The most (max/min) list (all same values)
//			Vector<MinimaxNode> nodeList = new Vector<MinimaxNode>();
//			int value;
//			
//			if(ifMax) {
//				value = -999999;
//				// Return the best value in the list
//				for (int i = 0; i < nodes.length; i++) {
//					if (nodes[i].getValue() > value)
//						value = nodes[i].getValue();
//				}
//			}
//			
//			else {
//				value = 999999;
//				// Return the least value in this list
//				for (int i = 0; i < nodes.length; i++) {
//					if (nodes[i].getValue() < value)
//						value = nodes[i].getValue();
//				}
//			}
//			// random if multiple with the same value
//			// which there definitely will be, especially at the beginning
//			for (int i = 0; i < nodes.length; i++) {
//				if (nodes[i].getValue() == value)
//					nodeList.add(nodes[i]);
//			}
//			
//			int randomNum = ThreadLocalRandom.current().nextInt(0, nodeList.size());
//			resultNode = nodeList.toArray(new MinimaxNode[nodeList.size()])[randomNum];
//		}
//		
//		else {
//			// Do the minimax on each node (spread out)
//			Vector<MinimaxNode> nextNodeList = new Vector<MinimaxNode>();
//			for (int i = 0; i < nodes.length; i++) {
//				nextNodeList.add(minimax(createMinimaxNodes(nodes[i], wp), depth - 2, 
//			}
//			MinimaxNode minimaxedNode = minimax(nextNodes, depth - 1, otherPlayer(wp), !ifMax);
//			resultNode = new MinimaxNode(minimaxedNode.board, minimaxedNode.value, minimaxedNode.moveToHere);
//		}
//		
//		return resultNode;
//	}
	
	private double valueOfBoard(Piece[][] board, WhichPlayer wp) {
		int value = 0;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j].getWhichPlayer() == wp)
					value += board[i][j].getValue();
				else
					value -= board[i][j].getValue();
			}
		}
		
		return value;
	}
	
	private WhichPlayer otherPlayer(WhichPlayer wp) {
		if (wp == WhichPlayer.WHITE)
			return WhichPlayer.BLACK;
		else if (wp == WhichPlayer.BLACK)
			return WhichPlayer.WHITE;
		else
			return WhichPlayer.NONE;
	}
	
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		String response;
		
		boolean ifComputer = false;
		boolean ifWhiteComputer = false;
		boolean ifBlackComputer = true;
		boolean ifCorrectlyResponded = false;
		
		//For testing purposes, just change i to 0 if you want to skip this and just do two humans
		for (int i = 0; i < 0; i++) {
			if (i == 0)
				System.out.println("White, human or computer?");
			else if (i == 1)
				System.out.println("Black, human or computer?");
			
			while(!ifCorrectlyResponded) {
				response = s.nextLine();
			
				if (response.equals("human") || response.equals("Human") || response.equals("player") || response.equals("Player")) {
					ifComputer = false;
					ifCorrectlyResponded = true;
				}
				else if (response.equals("computer") || response.equals("Computer") || response.equals("com") || response.equals("Com") || response.equals("AI")) {
					ifComputer = true;
					ifCorrectlyResponded = true;
				}
				else {
					System.out.println("Sorry, please type correctly: Human or Computer?");
				}
			}
			
			ifCorrectlyResponded = false;
			if (i == 0)
				ifWhiteComputer = ifComputer;
			else if (i == 1)
				ifBlackComputer = ifComputer;
		}
		
		s.close();
		Chess chess = new Chess(ifWhiteComputer, ifBlackComputer);
		
		WhichPlayer loser = chess.play();
		
		System.out.println(loser + " you lost");
	}

}
