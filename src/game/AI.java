package game;

import java.util.Random;

// TODO Change name probably
public class AI {
	
	static int[] indices = new int[2];

   /* public static int[] getRandomIndices(int n) {
        
        Random random = new Random();
        indices[0] = random.nextInt(n);
        indices[1] = random.nextInt(n);
        return indices;
    }*/
    
 public static int[] getIndices(int n, Algorithmtwo p) {
    	
	 // Un-comment lines to do the parity version
	 	int x,y,a,b;
        do
    	{
    		/*Random random = new Random();
    		indices[1] = random.nextInt(n);
    		if(indices[1] % 2 == 0)
    		{
    			indices[0] = random.nextInt(n);
    			if(indices[0] % 2 == 0)
    				indices[0] += random.nextBoolean() ? 1 : -1 ;
    		}
    		else
    		{
    			indices[0] = random.nextInt(n);
    			if(indices[0] %2 != 0)
    				indices[0] += random.nextBoolean() ? 1 : -1 ;
    		}
    		x = indices[0];
    		y = indices[1];*/
        	
        	
        	Random random = new Random();
            do{
        		indices[1] = random.nextInt(6); // Keep generating y while y is invalid, until y is valid.
            }while(indices[1]<0 || indices[1]>=6);
            
        		if(indices[1] % 2 == 0) // If y is even
        		{
        			do{indices[0] = random.nextInt(6); // Keep Generating x while x is even until x is odd
        			}while(indices[0]%2==0 && indices[0]>=0 && indices[0]<=6);        			
        			
        			//if(indices[0]%2 == 0) //If x is even
        			//	indices[0] += random.nextBoolean() ? 1 : -1 ;
        		}
        		else
        		{
        			//indices[0] = random.nextInt(6); // y is odd
        			do{
        			    indices[0] = random.nextInt(6); // Keep generating x while x is odd until x is even
        			}while(indices[0]%2!=0 && indices[0]>=0 && indices[0]<=6);
        			
        			
        			//if(indices[0] %2 != 0) //Check if x is odd
        			//	indices[0] += random.nextBoolean() ? 1 : -1 ;
        		}
        		x = indices[0];
        		y = indices[1];
        	
        	
    	}while(p.customBoard[x][y] == 1);
    	
    	p.customBoard[x][y] = 1; // Mark as visited	
    	for(a=0;a<n;a++)
    		for(b=0;b<n;b++)
    			{
    				System.out.print(p.customBoard[a][b]);
    			}
    	System.out.println(" ");
    	return indices;
   }
}