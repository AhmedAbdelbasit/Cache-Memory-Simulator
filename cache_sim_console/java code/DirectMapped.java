import java.lang.Math ;
import java.util.Scanner ;

public class DirectMapped {
	
	public static int MemorySize, CacheSize ;
	public static boolean WriteThrough ;
	public static boolean WriteAllocate ;
	public static int BlockNumber ;
	
	public static int WordField;
	public static int LineField;
	public static int TAG;
	
	public static int LineSize;
	public static int NumOfLines;
	public static int[] Lines;						// contains block number.    ""TAG""
	public static boolean[] IsCacheModified;		// for write back policy 
	
	public static int Line_Number;
	public static boolean H_M ;
	
	public static int NumOfHits ;
	public static int NumOfMemoryReferences;
	public static int NumOfWritesToCache;
	public static int NumOfWritesToMemory;
	
 	public DirectMapped(){
		NumOfHits = 0 ;
		NumOfMemoryReferences = 0 ;
		NumOfWritesToCache = 0 ;
		NumOfWritesToMemory = 0 ;
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("_Enter Main Memory size\n >> ");
		MemorySize = input.nextInt();
		System.out.print("_Enter Cache size\n >> ");
		CacheSize = input.nextInt();
		System.out.print("_Enter Block size\n >> ");
		LineSize = input.nextInt();
		WordField = (int)(Math.log(LineSize)/Math.log(2) +.1);
		
		//System.out.print("*****************");
		int i;

		System.out.print("_Choose Write-Hit policy :\n    1: Write-Through \n    2: Write-Back\n >> ");						// 1: Write-Through				2: Write-Back
		i = input.nextInt() ;
		if ( i == 1)
			WriteThrough = true;
		else
			WriteThrough = false;

		System.out.print("_Choose Write-Miss policy :\n    1: Write-Allocate\n    2: No-Write-Allocate\n >> ");			// 1: Write-Allocate			2: No-Write-Allocate
		i = input.nextInt() ;
		if ( i == 1)
			WriteAllocate = true;
		else
			WriteAllocate = false;
		System.out.println();
		
		NumOfLines = CacheSize/LineSize ;
		LineField = (int)(Math.log(NumOfLines)/Math.log(2) +.1 );
		Lines = new int[NumOfLines];
		for ( i=0 ; i<NumOfLines ; i++ ){
			Lines[i] = -1;
		}
		
		IsCacheModified = new boolean[NumOfLines];
		for ( i=0 ; i<NumOfLines ; i++ ){
			IsCacheModified[i] = false;
		}
		
	}
	
	public void readBlock(int Address){
		NumOfMemoryReferences += 1 ;
		BlockNumber = Address/LineSize ;
		System.out.println("# Block number " + BlockNumber );
	
		Line_Number = BlockNumber % NumOfLines ;
		System.out.println("# Line number " + Line_Number);
		TAG = Address >> ( WordField + LineField );
		System.out.println("# Tag : " + TAG );
		
		H_M = this.checkHit(Line_Number , BlockNumber );
		
		if (H_M){
			System.out.println("# Cache Hit ");
			System.out.println("# No Allocate-Line ");
			System.out.println("# No-Write-Back");
			NumOfHits += 1;
		}else{
			
			System.out.println("# Cache Miss ");
			System.out.println("# Allocate-Line ");
			if (IsCacheModified[Line_Number]){
				if ( !(WriteThrough) ){
					System.out.println("# Write-Back");
				}else
					System.out.println("# No-Write-Back");
			}else{
				System.out.println("# No-Write-Back");
			}
			Lines[Line_Number] = BlockNumber;
			IsCacheModified[Line_Number] = false;
		}
		
	}
	
	public void writeBlock(int Address){
		
		NumOfMemoryReferences += 1 ;
		BlockNumber = Address/LineSize;
		System.out.println("# Block number " + BlockNumber );
		
		Line_Number = BlockNumber % NumOfLines ;
		System.out.println("# Line number " + Line_Number);
		TAG = Address >> ( WordField + LineField );
		System.out.println("# Tag : " + TAG );
		
		H_M = this.checkHit(Line_Number , BlockNumber );
		
		if (H_M){
			NumOfHits += 1 ;
			System.out.println("# Cache Hit ");
			System.out.println("# No Allocate-Line ");
			System.out.println("# No-Write-Back");
			if (!(WriteThrough))
				IsCacheModified[Line_Number] = true;
		
		}else{
			System.out.println("# Cache Miss ");
			if (WriteAllocate){
				System.out.println("# Allocate-Line ");
				if (IsCacheModified[Line_Number]){
					System.out.println("# Write-Back");
				}else{
					System.out.println("# No-Write-Back");
				}
				Lines[Line_Number] = BlockNumber;
				IsCacheModified[Line_Number] = true;
			}else{
				System.out.println("# No Allocate-Line ");
				System.out.println("# No-Write-Back");
			}
		}	
	}
	
	public boolean checkHit(int line ,int block){
		
		if ( Lines[line] == block )
			return true;
		else
			return false;
	
	}
	
	public void printStatistics(){
		System.out.print("\n****************** Statistics ******************\n");
		System.out.print("\n  >> Number of cache Hit = " + NumOfHits );
		System.out.print("\n  >> Number of cache Miss= " + (NumOfMemoryReferences-NumOfHits) );
		System.out.print("\n  >> Hit Ratio = " + NumOfHits + "/" + NumOfMemoryReferences );
		System.out.print("\n\n************************************************\n");
	}
}