import java.util.Scanner ;

// Done
public class CacheBuilder {
	
	public static String Operation_R_W;
	public static int MemoryAddress;
	public static Scanner input = new Scanner(System.in);
	public static String[] Result = new String[8];
	public void buildDirectMapped(){
		
		DirectMapped Cache = new DirectMapped();
		System.out.print("_Enter instruction in form :  Read/Write   Memory_Address   or   Exit to close simulator\n");
		while ( true ){
			System.out.print("\n>> ");
			Operation_R_W = input.next();					// Operation Read-Write
			if ( Operation_R_W.charAt(0)=='R' || Operation_R_W.charAt(0)=='W' ){
				
				int address = input.nextInt();
				if (address >= Cache.MemorySize ){
					System.out.println("!!!!! Invalid Address, Memory Size is " + Cache.MemorySize + " Bytes Only ");
					continue ;
				}else if ( address < 0 ){
					System.out.println("!!!!! Invalid Address, It must be positive number.");
					continue ;
				}
			
				if ( Operation_R_W.charAt(0)=='R' ) // || Operation_R_W.charAt(0)=='r' )
					Cache.readBlock(address);
				else if ( Operation_R_W.charAt(0)=='W' ) // || Operation_R_W.charAt(0)=='w' )
					Cache.writeBlock(address);

			}else if ( Operation_R_W.charAt(0)=='E' ){ // || Operation_R_W.charAt(0)=='e' ){
				Cache.printStatistics();
				break ;
			}else{
				System.out.println("!!!!! Invalid Input, Undefined Instruction.");
				continue ;
			}	
		}	
	}
	
	
	public void buildAssociativeCache(){
		
		AssociativeMapped Cache = new AssociativeMapped();
		System.out.print("_Enter instruction in form :  Read/Write   Memory_Address   or   Exit to close simulator\n");
		while ( true ){
			System.out.print("\n>> ");
			Operation_R_W = input.next();				// Operation Read-Write
			if ( Operation_R_W.charAt(0)=='R' || Operation_R_W.charAt(0)=='W' ){
				
				int address = input.nextInt();
				if (address >= Cache.MemorySize ){
					System.out.println("!!!!! Invalid Address, Memory Size is " + Cache.MemorySize + " Bytes Only ");
					continue ;
				}else if ( address < 0 ){
					System.out.println("!!!!! Invalid Address, It must be positive number.");
					continue ;
				}
			
				if ( Operation_R_W.charAt(0)=='R' ) // || Operation_R_W.charAt(0)=='r' )
					Cache.readBlock(address);
				else if ( Operation_R_W.charAt(0)=='W' ) // || Operation_R_W.charAt(0)=='w' )
					Cache.writeBlock(address);

			}else if ( Operation_R_W.charAt(0)=='E' ){ // || Operation_R_W.charAt(0)=='e' ){
				Cache.printStatistics();
				break ;
			}else{
				System.out.println("!!!!! Invalid Input, Undefined Instruction.");
				continue ;
			}
		}
		
	}
	
	
	public void buildSetAssociativeCache(){
		
		SetMapped Cache = new SetMapped();
		System.out.print("_Enter instruction in form :  Read/Write   Memory_Address   or   Exit to close simulator\n");
		while ( true ){
			System.out.print("\n>> ");
			Operation_R_W = input.next();					// Operation Read-Write
			if ( Operation_R_W.charAt(0)=='R' || Operation_R_W.charAt(0)=='W' ){
				
				int address = input.nextInt();
				if (address >= Cache.MemorySize ){
					System.out.println("!!!!! Invalid Address, Memory Size is " + Cache.MemorySize + " Bytes Only ");
					continue ;
				}else if ( address < 0 ){
					System.out.println("!!!!! Invalid Address, It must be positive number.");
					continue ;
				}
			
				if ( Operation_R_W.charAt(0)=='R' ) // || Operation_R_W.charAt(0)=='r' )
					Cache.readBlock(address);
				else if ( Operation_R_W.charAt(0)=='W' ) // || Operation_R_W.charAt(0)=='w' )
					Cache.writeBlock(address);

			}else if ( Operation_R_W.charAt(0)=='E' ){ // || Operation_R_W.charAt(0)=='e' ){
				Cache.printStatistics();
				break ;
			}else{
				System.out.println("!!!!! Invalid Input, Undefined Instruction.");
				continue ;
			}	
		}
		
	}
}