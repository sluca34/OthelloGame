public class Move{
	private int move;
	private double value;

	public Move(int move){
		this.move = move;
		this.value = 0;
	}

	public Move(){
		this.move = 0;
		this.value = 0;
	}

	public int getMove(){
		return move;
	}

	public double getValue(){
		return value;
	}

	public void setValue(double value){
		this.value = value;
	}


	public String toString(){
		return "" + move;
	}

	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Move m = (Move) obj;
		return move == m.move;
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31*hash + move;
		return hash;
	}

	public static void main(String [] args){
		Move thisMove = new Move(10);
		System.out.println(thisMove);
	}
}