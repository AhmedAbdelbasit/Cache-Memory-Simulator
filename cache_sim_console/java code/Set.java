

public class Set {
	
	public Line[] Lines;
	public static int LineSize;
	public static int K_Ways;
	public static int ReplacementAlgorithm ;			// 1: FIFO    2: LFU    3: LRU
	public static boolean WriteThrough;
	public static boolean WriteAllocate;
	public int NextEmptyLine;
	public int InCounter;
	public int Line_Number;
	
	
	public Set(){
		InCounter = 1;
		NextEmptyLine =0 ;
		Lines = new Line[K_Ways];
		for (int i=0 ; i<K_Ways ; i++ ){
			Lines[i] = new Line();
		}
	}
	
	public boolean checkHit(int B){
		for(int i=0 ; i<K_Ways ; i++ ){
			if ( Lines[i].Tag == B ){
				return true;
			}
		}
		return false;
	}
	
	public int getLineHit(int B,boolean write){
		for (int i=0 ; i<K_Ways ; i++ ){
			if ( Lines[i].Tag == B ){
				Lines[i].makeHit(write,WriteThrough);
				return i;
			}
		}
		return -1;
	}
	
	public void allocateLine(int B,boolean w){
		
		if ( NextEmptyLine < K_Ways ){
			
			Line_Number = NextEmptyLine;
			NextEmptyLine +=1;
			System.out.println("# Line number in the set " + Line_Number);
			System.out.println("# No-Write-Back");
				
			
		}else{ // Depends on Replacement Algorithm 		1: FIFO    2: LFU    3: LRU  
		
			if ( ReplacementAlgorithm == 1 ) 	// FIFO Check for First-in Line
				getFirstIn();		
			else if ( ReplacementAlgorithm == 2 ) 	// LFU Check for NumOfAccess
				getLeastFreqUsed();
			else if ( ReplacementAlgorithm == 3 ) 	// LRU Check for NumOfAccess
				getLeastRecentlyUsed();
					
			System.out.println("# Line number in the set  " + Line_Number);
			checkForWriteBack();
			
		}
		
		Lines[Line_Number].allocateBlock(B,w,InCounter); 		// flase if read - true if write
		increaseLinesUsage();
		InCounter += 1;
			
	}
	
		// Done
	public void increaseLinesUsage(){
		for (int i=0 ; i<K_Ways ; i++ ){
			if ( i == Line_Number )
				continue ;
			Lines[i].LastUsed += 1 ;
		}
	}
	
	// Done
	public void getFirstIn(){
		int min=InCounter;
		for ( int i=0 ; i<K_Ways ; i++ ){
			if (Lines[i].InputOrder < min){
				min = Lines[i].InputOrder;
				Line_Number = i;
			}
		}		
	}
	
	// Done
	public void getLeastFreqUsed(){
		int min = InCounter;
		int F = InCounter;
		for ( int i=(K_Ways-1) ; i>=0 ; i-- ){
			if (Lines[i].NumOfAccess <= min && Lines[i].InputOrder<F ){
				min = Lines[i].NumOfAccess;
				Line_Number = i;
			}
		}
	}
	
	// Done
	public void getLeastRecentlyUsed(){
		int max = 0;
		for (int i=0 ; i<K_Ways ; i++ ){
			if ( Lines[i].LastUsed > max ){ 
				max = Lines[i].LastUsed;
				Line_Number = i;
			}
		}
	}
	
	// Done
	public void checkForWriteBack(){
		if (WriteThrough){
			System.out.println("# No-Write-Back");
		}else{
			if (Lines[Line_Number].IsCacheModified){
				System.out.println("# Write-Back");
			}else{
				System.out.println("# NO-Write-Back");
			}
		}
	}
	
}