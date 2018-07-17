package game;

import board.Board;
import board.Creator;
import board.SmallBoard;
import ship.Ship;
import ship.ShipPiece;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

public class Window {

    private JFrame frame;
    private final int windowWidth = 900;
    private final int windowHeight = 600;

    public static int numberOfBattleships, lengthOfBattleships;
    public static int numberOfCruisers, lengthOfCruisers;
    public static int numberOfDestroyers, lengthOfDestroyers;
    public static int numberOfSubmarines, lengthOfSubmarines;
    public static int boardSize;
    private boolean gameRunning;
    public static Mode gameMode;
    
    //public static Stack st = new Stack();
    
    

    private JLabel playersTurn;
    private boolean playerOneTurn;
    private final String playerOneTurnString = "Player 1's turn";
    private final String playerTwoTurnString = "Player 2's turn";

    public void setupMenu() {
        frame = new JFrame();
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.setMinimumSize(new Dimension(windowWidth, windowHeight));
        frame.setResizable(false);
        frame.pack();
        Menu menu = new Menu(frame);
        menu.setup();
        while (!menu.isReadyToStart()) {}
        gameRunning = true;
        setupBoard();
    }

    public void setupBoard() {
        Ship[] playerOneShips = setupShips(true);
        Ship[] playerTwoShips = setupShips(false);

        Board board = new Board(selectShipPositions(playerOneShips, true));
        SmallBoard smallBoard = new SmallBoard(selectShipPositions(playerTwoShips, false));
        smallBoard.setLocation(board.getWidth() + 10, board.getY());

        playersTurn = new JLabel(playerTwoTurnString);
        playersTurn.setForeground(Color.WHITE);
        playersTurn.setSize(100, 100);
        playersTurn.setLocation(board.getWidth() + 10, smallBoard.getHeight() + 10);
        playersTurn.setVisible(true);

        frame.setPreferredSize(new Dimension(smallBoard.getX() + smallBoard.getWidth() + 10, frame.getHeight()));
        frame.setSize(frame.getPreferredSize());
        frame.pack();
        frame.getContentPane().add(board);
        frame.getContentPane().add(smallBoard);
        frame.getContentPane().add(playersTurn);
        frame.addMouseListener(board);
        frame.setVisible(true);

        beginGame(playerOneShips, playerTwoShips, board, smallBoard);
    }

    private Ship[] setupShips(boolean isPlayerOne) {
        Ship[] battleships = createShips(numberOfBattleships, lengthOfBattleships, isPlayerOne);
        Ship[] cruisers = createShips(numberOfCruisers, lengthOfCruisers, isPlayerOne);
        Ship[] destroyers = createShips(numberOfDestroyers, lengthOfDestroyers, isPlayerOne);
        Ship[] submarines = createShips(numberOfSubmarines, lengthOfSubmarines, isPlayerOne);

        Ship[] playerShips = concatShipArray(battleships, cruisers);
        playerShips = concatShipArray(playerShips, destroyers);
        playerShips = concatShipArray(playerShips, submarines);
        return playerShips;
    }

    private Ship[] createShips(int numberOfShips, int shipSize, boolean isPlayerOne) {
        Ship[] ships = new Ship[numberOfShips];
        for (int i = 0; i < numberOfShips; i++) {
            ShipPiece[] shipArray = new ShipPiece[shipSize];
            for (int j = 0; j < shipSize; j++) {
                ShipPiece shipPiece = new ShipPiece(isPlayerOne);
                shipArray[j] = shipPiece;
            }
            ships[i] = new Ship(shipArray);
        }
        return ships;
    }

    private Ship[] concatShipArray(Ship[] a, Ship[] b) {
        Ship[] ship = new Ship[a.length + b.length];
        System.arraycopy(a, 0, ship, 0, a.length);
        System.arraycopy(b, 0, ship, a.length, b.length);
        return ship;
    }

    private Object[][] selectShipPositions(Ship[] ships, boolean isPlayerOne) {
        Creator creator = new Creator(ships, boardSize, frame);
        creator.setup();
        frame.getContentPane().add(creator);
        frame.getContentPane().repaint();
        frame.setVisible(true);

        switch (gameMode) {
            case HumanVsAI:
                if (!isPlayerOne) {
                    creator.triggerRandomButton();
                    creator.triggerEndSetupButton();
                }
                break;
            case AIVsAI:
                creator.triggerRandomButton();
                creator.triggerEndSetupButton();
            case HumanVsHuman:
            default:
                break;
        }

        while (!creator.isCompleteSetup()) {}
        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        return creator.getBoardArray();
    }

    private void beginGame(Ship[] playerOneShips, Ship[] playerTwoShips, Board board, SmallBoard smallBoard) {
        changeTurns(board, smallBoard);
        setPlayerOneTurn(false);
        
        Algorithmtwo p1 = new Algorithmtwo(boardSize);
        Algorithmtwo p2 = new Algorithmtwo(boardSize);

        while (gameRunning) {
            switch (gameMode) {
                case HumanVsAI:
                    if (!playerOneTurn) {
                    	int[] indices = new int[2];
                        int i,j,flag;
                        System.out.println("Board Size ="+boardSize);
                        
                        if(p2.queue.isEmpty())
                        	{
                        		indices = AI.getIndices(boardSize,p2);
                        		System.out.println("If queue is empty, Indices:"+indices[1]+","+indices[0]);
                        		//int bl = board.getArray().length;
                        		//System.out.println("Bl="+bl);
                        	}
                        else
                        {
                        	Point elementToPop = (Point)p2.queue.poll();
                        	indices[0] = elementToPop.x;
                        	indices[1] = elementToPop.y;
                        	System.out.println("Popped Element:"+indices[1]+","+indices[0]);
                        }
                        
                        flag = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                       
                        i=indices[0];
                        j=indices[1];
                        Point hitPoint = new Point(i,j);
                        
                        // && p2.customBoard[i-1][j]!=1
                        // && p2.customBoard[i+1][j]!=1
                        // && p2.customBoard[i][j-1]!=1
                        // && p2.customBoard[i][j+1]!=1
                        
                        // In case of a hit 
                        if(flag == 1)
                        {
                        	System.out.println("HitPoint"+hitPoint);
                        	if((i-1)>=0 && p2.customBoard[i-1][j]!=1)
                        	{
                        		Point left = new Point(i-1,j);
                        		p2.queue.add(left);
                        		System.out.println("Added LEFT"+left);
                        	}
                        	if((i+1)<board.getArray().length && p2.customBoard[i+1][j]!=1)
                        	{
                        		Point right = new Point(i+1,j);
                        		p2.queue.add(right);
                        		System.out.println("Added RIGHT"+right);
                        	}
                        	if((j-1)>=0 && p2.customBoard[i][j-1]!=1)
                        	{
                        		Point up = new Point(i,j-1);
                        		p2.queue.add(up);
                        		System.out.println("Added UP"+up);
                        	}
                        	if((j+1)<board.getArray().length && p2.customBoard[i][j+1]!=1)
                        	{
                        		Point down = new Point(i,j+1);
                        		p2.queue.add(down);
                        		System.out.println("Added DOWN"+down);
                        	}
                        }
                        MouseEvent mouseEvent = new MouseEvent(frame, 0, 0, 0, indices[0], indices[1], 1, false);
                        for (MouseListener mouseListener : frame.getMouseListeners()) {
                            mouseListener.mousePressed(mouseEvent);
                        }
                    }
                    break;
                case AIVsAI:
                    //int[] indices = AI.getRandomIndices(board.getArray().length);
                    //while (!board.selectedPositionOnBoardByPlayer(indices[0], indices[1])) {
                       // indices = AI.getRandomIndices(board.getArray().length);
                   // }
                	/*if (!playerOneTurn) {
                    	int[] indices = new int[2];
                        int i,j,flag;
                        if(p2.queue.isEmpty())
                        	indices = AI.getIndices(boardSize,p1);
                        else
                        {
                        Point elementToPop = (Point)p2.queue.poll();
                        indices[0] = elementToPop.x;
                    	indices[1] = elementToPop.y;	
                        }
                        flag = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                       
                        i=indices[0];
                        j=indices[1];
                        
                        if(flag == 1)
                        {
                        // In case of a hit 
                        	if((i-1)>=0)
                        	{
                        		Point up = new Point(i-1,j);
                        		p2.queue.add(up);
                        	}
                        	if((i+1)<board.getArray().length)
                        	{
                        		Point down = new Point(i+1,j);
                        		p2.queue.add(down);
                        	}
                        	if((j-1)>=0)
                        	{
                        		Point left = new Point(i,j-1);
                        		p2.queue.add(left);
                        	}
                        	if((j+1)<board.getArray().length)
                        	{
                        		Point right = new Point(i,j+1);
                        		p2.queue.add(right);
                        	}
                        }
                	
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    MouseEvent mouseEvent = new MouseEvent(frame, 0, 0, 0, indices[0], indices[1], 1, false);
                    for (MouseListener mouseListener : frame.getMouseListeners()) {
                        mouseListener.mousePressed(mouseEvent);
                    }
                	}
                	
                	else
                	{
                		int[] indices = new int[2];
                        int i,j,flag;
                        if(p2.queue.isEmpty())
                        	indices = AI.getIndices(boardSize,p1);
                        else
                        {
                        Point elementToPop = (Point)p2.queue.poll();
                        indices[0] = elementToPop.x;
                    	indices[1] = elementToPop.y;	
                        }
                        flag = board.selectedPositionOnBoardByPlayer(indices[0], indices[1]);
                       
                        i=indices[0];
                        j=indices[1];
                        
                        if(flag == 1)
                        {
                        // In case of a hit 
                        	if((i-1)>=0)
                        	{
                        		Point up = new Point(i-1,j);
                        		p2.queue.add(up);
                        	}
                        	if((i+1)<board.getArray().length)
                        	{
                        		Point down = new Point(i+1,j);
                        		p2.queue.add(down);
                        	}
                        	if((j-1)>=0)
                        	{
                        		Point left = new Point(i,j-1);
                        		p2.queue.add(left);
                        	}
                        	if((j+1)<board.getArray().length)
                        	{
                        		Point right = new Point(i,j+1);
                        		p2.queue.add(right);
                        	}
                        }
                	
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    MouseEvent mouseEvent = new MouseEvent(frame, 0, 0, 0, indices[0], indices[1], 1, false);
                    for (MouseListener mouseListener : frame.getMouseListeners()) {
                        mouseListener.mousePressed(mouseEvent);
                    }
                	}*/
                    break;
                case HumanVsHuman:
                    default:
                        break;

            }
            boolean playerOneAllShipsDead = true;

            for (int i = 0; i < playerOneShips.length; i++) {
                if (playerOneShips[i].checkIfDead()) {
                    for (int j = 0; j < playerOneShips[i].getShipPieces().length; j++) {
                        playerOneShips[i].getShipPieces()[j].setShipImage("dead.png");
                    }
                } else {
                    playerOneAllShipsDead = false;
                }
            }

            board.repaint();
            smallBoard.repaint();

            boolean playerTwoAllShipsDead = true;

            for (int i = 0; i < playerTwoShips.length; i++) {
                if (playerTwoShips[i].checkIfDead()) {
                    for (int j = 0; j < playerTwoShips[i].getShipPieces().length; j++) {
                        playerTwoShips[i].getShipPieces()[j].setShipImage("dead.png");
                    }
                } else {
                    playerTwoAllShipsDead = false;
                }
            }

            board.repaint();
            smallBoard.repaint();

            if (playerOneAllShipsDead || playerTwoAllShipsDead) {
                gameRunning = false;
                for (int i = 0; i < board.getArray().length; i++) {
                    for (int j = 0; j < board.getArray()[i].length; j++) {
                        if ((board.getArray()[i][j].equals(1))) {
                            board.getArray()[i][j] = 0;
                        }
                    }
                }
                new End(frame, !playerOneAllShipsDead).loadEndScreen();
            }
        }
    }

    private void changeTurns(Board board, SmallBoard smallBoard) {
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ChangeTurns changeTurns = new ChangeTurns((JPanel) frame.getContentPane(), board, smallBoard);
                final Object[][] board1Temp = board.getArray();
                final Object[][] board2Temp = smallBoard.getArray();
                if (!board.isTurn() && gameRunning) {
                    board.setVisible(false);
                    smallBoard.setVisible(false);
                    board.setArray(board2Temp);
                    smallBoard.setArray(board1Temp);
                    changeTurns.loadChangeTurnScreen(gameMode, playerOneTurn);
                    setPlayerOneTurn(!playerOneTurn);
                    if (playerOneTurn) playersTurn.setText(playerOneTurnString);
                    else playersTurn.setText(playerTwoTurnString);
                }
            }
        });
    }

    private void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

}
