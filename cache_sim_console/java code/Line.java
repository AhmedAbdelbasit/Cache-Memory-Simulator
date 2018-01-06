import java.util.Scanner ;

public class Line {
	
	public int InputOrder;
	public int LastUsed;				// for LRU Replacement algorithm
	public int NumOfAccess;				// for LFU Replacement algorithm
	public boolean IsCacheModified;		// for write back policy 
	public int Tag;
	
	public Line(){
		LastUsed = 0;
		NumOfAccess = 0;
		IsCacheModified = false;
		Tag = -1;
	}
	
	public void allocateBlock( int Block, boolean write , int order){
		Tag = Block;
		LastUsed = 0;
		InputOrder = order ;
		//System.out.println("# Input order " + InputOrder );
		NumOfAccess = 1;
		//System.out.println("# Number of Access " + NumOfAccess );
		if (write)
			IsCacheModified = true;
		else
			IsCacheModified = false;
	}
	
	public void makeHit(boolean write ,boolean SetWriteThrough) {
		LastUsed = 0;
		NumOfAccess +=1 ;
		//System.out.println("# Number of Access " + NumOfAccess );
		if (write && (!(Set.WriteThrough)))
			IsCacheModified = true;			
	}
	
}