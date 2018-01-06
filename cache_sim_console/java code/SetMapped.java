import java.util.Scanner ;
import java.lang.Math;

public class SetMapped {
	
	// Memory Information # Done
	public static int MemorySize, CacheSize ;	
	//public static int ReplacementAlgorithm; 	// 1: FIFO    2: LFU    3: LRU
	//public static boolean WriteThrough ;		// True for Write-Through && False for Write-Back
	//public static boolean WriteAllocate ;		// True for Write-Allocate && False for NO-Write-Allocate
	public static int NumOfSets;				// number of Sets in cache 
	public static Set[] Sets;					// array of objects for cache Sets 
	//public static int KWays;					// Number of Lines in each Set
	public static int SetNumber;				// Choosen by direct mapping 
	public static int BlockNumber ;				// Address / BlockSize
	public static int LineSize ;				// Number of bytes in Block-Line
	public static int WordField;
	public static int SetField;
	public static int TAG;
	// Some Pointers used in Replacement # Done
	public static boolean H_M ;					// true for hit - false for miss
	
	// parameters for performance Statistics : # Done
	public static int NumOfHits ;
	public static int NumOfMemoryReferences;
	public static int NumOfWritesToCache;		
	public static int NumOfWritesToMemory;
	

	public SetMapped(){
		NumOfHits = 0 ;
		NumOfMemoryReferences = 0 ;
		NumOfWritesToCache = 0 ;
		NumOfWritesToMemory = 0 ;
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("_Enter Main Memory size\n >> ");
		MemorySize = input.nextInt();
		System.out.print("_Enter Cache size\n >> ");
		CacheSize = input.nextInt();
		
		System.out.print("_Enter Number of Lines in Set\n >> ");
		Set.K_Ways = input.nextInt();
		System.out.print("_Enter Block size\n >> ");
		LineSize = input.nextInt();
		WordField = (int)((Math.log(LineSize)/Math.log(2))+.1);
		
		Set.LineSize = LineSize;
		//System.out.print("*****************");
		int i;
		System.out.print("_Choose Replacement Algorithm :\n    1: FIFO\n    2: LFU\n    3: LRU\n    4: Random\n >> ") ;
		Set.ReplacementAlgorithm = input.nextInt();
		System.out.print("_Choose Write-Hit policy :\n    1: Write-Through \n    2: Write-Back\n >> ");						// 1: Write-Through				2: Write-Back
		i = input.nextInt() ;
		if ( i == 1)
			Set.WriteThrough = true;
		else
			Set.WriteThrough = false;

		System.out.print("_Choose Write-Miss policy :\n    1: Write-Allocate\n    2: No-Write-Allocate\n >> ");			// 1: Write-Allocate			2: No-Write-Allocate
		i = input.nextInt() ;
		if ( i == 1)
			Set.WriteAllocate = true;
		else
			Set.WriteAllocate = false;
		System.out.println();
		
		NumOfSets = CacheSize/(Set.K_Ways*LineSize) ;
		SetField = (int)((Math.log(NumOfSets)/Math.log(2)) + .1);
		Sets = new Set[NumOfSets];
		for ( i=0 ; i<NumOfSets ; i++ ){
			Sets[i] = new Set();
		}
		
	}
	
	
	public void readBlock(int Address){
		
		NumOfMemoryReferences +=1 ;
		BlockNumber = Address / LineSize ;
		System.out.println("# Block Number : " + BlockNumber );
		SetNumber = BlockNumber % NumOfSets ;
		System.out.println("# Set Number : " + SetNumber );
		TAG = (Address>>(WordField+SetField));
		System.out.println("# Tag : " + TAG);
		H_M = Sets[SetNumber].checkHit(BlockNumber);
		
		if(H_M){
			
			NumOfHits += 1;
			System.out.println("# Cache Hit ");
			System.out.println("# Line Number in the Set : " + Sets[SetNumber].getLineHit(BlockNumber,false));
			System.out.println("# No-Allocate-Line");
			System.out.println("# No-Write-Back");
		
		}else{
			
			System.out.println("# Cache Miss ");
			System.out.println("# Allocate-Line");
			
			Sets[SetNumber].allocateLine(BlockNumber,false);
			
		}
		
	}
	
	
	public void writeBlock(int Address){
		
		NumOfMemoryReferences +=1 ;
		BlockNumber = Address / LineSize ;
		System.out.println("# Block Number : " + BlockNumber );
		SetNumber = BlockNumber % NumOfSets ;
		System.out.println("# Set Number : " + SetNumber );
		TAG = (Address>>(WordField+SetField));
		System.out.println("# Tag : " + TAG);
		
		H_M = Sets[SetNumber].checkHit(BlockNumber);
		
		if(H_M){
			
			NumOfHits += 1;
			System.out.println("# Cache Hit ");
			System.out.println("# Line Number in the Set : " + Sets[SetNumber].getLineHit(BlockNumber,true));
			System.out.println("# No-Allocate-Line");
			System.out.println("# No-Write-Back");
		
		}else{
			
			System.out.println("# Cache Miss ");
			
			if (Set.WriteAllocate){
				System.out.println("# Allocate-Line");
				Sets[SetNumber].allocateLine(BlockNumber,true);
			}else{
				System.out.println("# No-Allocate-Line");
				System.out.println("# No-Write-Back ");
			}
				
			
		}
		
	}
	
	
	public void printStatistics(){
		System.out.print("\n****************** Statistics ******************\n");
		System.out.print("\n  >> Number of cache Hit = " + NumOfHits );
		System.out.print("\n  >> Number of cache Miss= " + (NumOfMemoryReferences-NumOfHits) );
		System.out.print("\n  >> Hit Ratio = " + NumOfHits + "/" + NumOfMemoryReferences );
		System.out.print("\n\n************************************************\n");
	}
	
}