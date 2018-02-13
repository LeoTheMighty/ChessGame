public class Move {
	// initial position and final position;
	// ADD SOMETHING THAT ALLOWS FOR TWO SPECIAL MOVES
	
	private Piece piece;
	private Position p0, pf;
	private SpecialMoves specialMove;
	
	Move(Piece piece, Position pf, SpecialMoves specialMove) {
		this.piece = piece;
		p0 = piece.getPosition();
		this.pf = pf;
		this.specialMove = specialMove;
	}
	
	public void setSpecialMove(SpecialMoves sm) {
		specialMove = sm;
	}
	
	public SpecialMoves getSpecialMove() {
		return specialMove;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public Position getFirst() {
		return p0;
	}
	
	public Position getEnd() {
		return pf;
	}
}
