package game;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Algorithmtwo {
	
	Queue<Point> queue = new LinkedList<>(); 
    int customBoard[][] = new int[10][10];
    
    public Algorithmtwo(int n)
    {
    	int x,y;
    	for(x=0;x<n;x++)
    		for(y=0;y<n;y++)
    			customBoard[x][y]=0;
    }
	public Queue<Point> getQueue() {
		return queue;
	}
	public void setQueue(Queue<Point> queue) {
		this.queue = queue;
	}
	public int[][] getCustomBoard() {
		return customBoard;
	}
	public void setCustomBoard(int[][] customBoard) {
		this.customBoard = customBoard;
	}
    
    

}
