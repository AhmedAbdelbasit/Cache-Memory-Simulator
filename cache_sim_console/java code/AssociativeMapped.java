import java.util.Scanner ;

public class AssociativeMapped {
	
	// Memory Information
	public static int MemorySize, CacheSize ;	
	public static int ReplacementAlgorithm; 	// 1: FIFO    2: LFU    3: LRU
	public static boolean WriteThrough ;		// True for Write-Through && False for Write-Back
	public static boolean WriteAllocate ;		// True for Write-Allocate && False for NO-Write-Allocate
	public static int NumOfLines;				// number of lines in cache = cacheSize/LineSize
	public static Line[] Lines;					// array of objects for cache Lines 
	public static int LineSize;					// Number of Bytes in each line
	public static int Line_Number;				// Choosen withregard to replacement algrithm "Miss" or cache search tags "Hit"
	public static int BlockNumber ;				// Address / BlockSize
	
	// Some Pointers used in Replacement
	public static int NextEmptyLine;			// To allocate empty cache first
	public static int InCounter;				// Used in FIFO algorithm as the last input order
	public static boolean H_M ;					// true for hit - false for miss
	
	// parameters for performance Statistics :
	public static int NumOfHits ;
	public static int NumOfMemoryReferences;
	public static int NumOfWritesToCache;		
	public static int NumOfWritesToMemory;
	
	// Constructor
	public AssociativeMapped(){
		
		// Constructor for cache 
		InCounter = 1 ;											// first line to be allocated has order 1			
		NumOfHits = 0 ;											// initially no hits
		NumOfMemoryReferences = 0 ;								// initially no instructions for memory references
		NumOfWritesToCache = 0 ;								// initially is zero
		NumOfWritesToMemory = 0 ;								//  initially is zero
		NextEmptyLine = 0;										// initially all cache is empty
		
		Scanner input = new Scanner(System.in);
		
		// Inputs for Cache characterestics 
		System.out.print("_Enter Main Memory size\n >> ");
		MemorySize = input.nextInt();
		System.out.print("_Enter Cache size\n >> ");
		CacheSize = input.nextInt();
		System.out.print("_Enter Block size\n >> ");
		LineSize = input.nextInt();
		
		int i;		// general purpose
		System.out.print("_Choose Replacement Algorithm :\n    1: FIFO\n    2: LFU\n    3: LRU\n    4: Random\n >> ") ;
		ReplacementAlgorithm = input.nextInt();
		System.out.print("_Choose Write-Hit policy :\n    1: Write-Through \n    2: Write-Back\n >> ");
		i = input.nextInt() ;
		if ( i == 1)
			WriteThrough = true;
		else
			WriteThrough = false;
		System.out.print("_Choose Write-Miss policy :\n    1: Write-Allocate\n    2: No-Write-Allocate\n >> ");	
		i = input.nextInt() ;
		if ( i == 1)
			WriteAllocate = true;
		else
			WriteAllocate = false;
		System.out.println();
		
		// Some Basic calculations for cache parameters
		NumOfLines = CacheSize/LineSize ;
		// initializing Line objects
		Lines = new Line[NumOfLines];
		for ( i=0 ; i<NumOfLines ; i++ ){
			Lines[i] = new Line();
		}
	}
	
	// function to deal with Read Instructions
	public void readBlock(int Address){
		
		NumOfMemoryReferences += 1 ;							
		BlockNumber = Address/LineSize ;						// calculating block number from the address
		System.out.println("# Block number " + BlockNumber );	// output of block number (1)
		
		Line_Number = this.checkHit( BlockNumber );
		if (Line_Number == -1)
			H_M = false;
		else
			H_M = true;
		
		if (H_M){
			
			// Read HIT
			System.out.println("# Cache Hit ");
			System.out.println("# Line number " + Line_Number);
			System.out.println("# No Allocate-Line ");
			System.out.println("# No-Write-Back");
			NumOfHits += 1;
			Lines[Line_Number].makeHit(false,true);
			
		}else{ // Read Miss 
			
			System.out.println("# Cache Miss ");
			System.out.println("# Allocate-Line ");
			
			if ( NextEmptyLine < NumOfLines ) { // if there is empty lines >> No replacement
				
				Line_Number = NextEmptyLine;
				NextEmptyLine +=1;
				System.out.println("# Line number " + Line_Number);
				//Lines[Line_Number].allocateBlock(BlockNumber,false,InCounter);
				System.out.println("# No-Write-Back");
				
			}else{ // Depends on Replacement Algorithm 		1: FIFO    2: LFU    3: LRU  
				if ( ReplacementAlgorithm == 1 ) 	// FIFO Check for First-in Line
					getFirstIn();		
				else if ( ReplacementAlgorithm == 2 ) 	// LFU Check for NumOfAccess
					getLeastFreqUsed();
				else if ( ReplacementAlgorithm == 3 ) 	// LRU Check for NumOfAccess
					getLeastRecentlyUsed();
					
				System.out.println("# Line number " + Line_Number);
				checkForWriteBack();
			}
			
			Lines[Line_Number].allocateBlock(BlockNumber,false,InCounter); 		// flase if read - true if write
			increaseLinesUsage();
			InCounter += 1;
		}
		
		System.out.println("# Num of Access : " + Lines[Line_Number].NumOfAccess );
		System.out.println("# Input order to cache : " + Lines[Line_Number].InputOrder );
	}
	
	// function to deal with Write Instructions
	public void writeBlock(int Address){
		
		NumOfMemoryReferences += 1 ;
		BlockNumber = Address/LineSize ;
		System.out.println("# Block number " + BlockNumber );
		
		Line_Number = this.checkHit( BlockNumber );
		if (Line_Number == -1)
			H_M = false;
		else
			H_M = true;
		
		if (H_M){
			
			// Write HIT Done
			System.out.println("# Cache Hit ");
			System.out.println("# Line number " + Line_Number);
			System.out.println("# No Allocate-Line ");
			System.out.println("# No-Write-Back");
			NumOfHits += 1;
			// some changes here
			if ( !(WriteThrough ) )
				Lines[Line_Number].IsCacheModified = true;
			increaseLinesUsage();
			Lines[Line_Number].NumOfAccess += 1;
			Lines[Line_Number].LastUsed = 0;
			
			System.out.println("# Num of Access : " + Lines[Line_Number].NumOfAccess );
			System.out.println("# Input order to cache : " + Lines[Line_Number].InputOrder );
				
		}else{ // Write Miss 
			
			System.out.println("# Cache Miss ");
			if ( WriteAllocate ){
				System.out.println("# Allocate-Line ");
				
				if ( NextEmptyLine < NumOfLines ) { // if there is empty lines >> No replacement
				
					Line_Number = NextEmptyLine;
					NextEmptyLine +=1;
					System.out.println("# Line number " + Line_Number);
					Lines[Line_Number].allocateBlock(BlockNumber,true,InCounter);
					System.out.println("# No-Write-Back");
					
				}else{ // Depends on Replacement Algorithm 		1: FIFO    2: LFU    3: LRU  
					if ( ReplacementAlgorithm == 1 ) 	// FIFO Check for First-in Line
						getFirstIn();		
					else if ( ReplacementAlgorithm == 2 ) 	// LFU Check for NumOfAccess
						getLeastFreqUsed();
					else if ( ReplacementAlgorithm == 3 ) 	// LRU Check for NumOfAccess
						getLeastRecentlyUsed();
					
					System.out.println("# Line number " + Line_Number);
					checkForWriteBack();
					Lines[Line_Number].allocateBlock(BlockNumber,true,InCounter);
				}
				increaseLinesUsage();
				Lines[Line_Number].LastUsed = 0;
				InCounter += 1;
				
				System.out.println("# Num of Access : " + Lines[Line_Number].NumOfAccess );
				System.out.println("# Input order to cache : " + Lines[Line_Number].InputOrder );
			}else{
				System.out.println("# NO-Allocate-Line ");
				System.out.println("# NO-WriteBack ");
			}
			
		}
		
	}
		
	// Done
	public void printStatistics(){
		System.out.print("\n****************** Statistics ******************\n");
		System.out.print("\n  >> Number of cache Hit = " + NumOfHits );
		System.out.print("\n  >> Number of cache Miss= " + (NumOfMemoryReferences-NumOfHits) );
		System.out.print("\n  >> Hit Ratio = " + NumOfHits + "/" + NumOfMemoryReferences );
		System.out.print("\n\n************************************************\n");
	}
	
	// Done
	public void increaseLinesUsage(){
		for (int i=0 ; i<NumOfLines ; i++ ){
			if ( i == Line_Number )
				continue ;
			Lines[i].LastUsed += 1 ;
		}
	}
	
	// Done
	public void getFirstIn(){
		int min=InCounter;
		for ( int i=0 ; i<NumOfLines ; i++ ){
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
		for ( int i=(NumOfLines-1) ; i>=0 ; i-- ){
			if (Lines[i].NumOfAccess <= min && Lines[i].InputOrder<F ){
				min = Lines[i].NumOfAccess;
				Line_Number = i;
			}
		}
	}
	
	// Done
	public void getLeastRecentlyUsed(){
		int max = 0;
		for (int i=0 ; i<NumOfLines ; i++ ){
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
	
	// Done
	public int checkHit(int block) {
		for ( int i=0 ; i<NumOfLines ; i++ )
			if ( Lines[i].Tag == block )
				return i;
		return -1;
	}

}