import java.util.Scanner;

// Done
public class Main {
	
	private DirectMapped D ;
	private AssociativeMapped A ;
	private Line line ;
	private Set set ;
	private SetMapped S ;
	
	public static void main(String[] args) {
		
		
		Scanner input = new Scanner(System.in);
		
		int MappingFunction;
		System.out.print("\n_Choose the Mapping Function of cache :\n    1: direct \n    2: Associative\n    3: Set-Associative\n >> "); 	
		// 1: direct   	2: Associative   3: Set-Associative
		MappingFunction = input.nextInt();
		
		CacheBuilder B = new CacheBuilder();
		
		if ( MappingFunction == 1 )
			B.buildDirectMapped();
		else if ( MappingFunction == 2 )
			B.buildAssociativeCache();
		else if ( MappingFunction == 3 )
			B.buildSetAssociativeCache();
		else
			System.out.println("Error.");
				
	}
	
}