import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;

/*
 * @author Jason Kirshner, Harrison Worden, Jon Aurit, Alex Young
 * @version 10/16/2016
 * This class is used to display the GUI for Breakthrough.
 * 31337
 */
 
public class BreakThrough extends JFrame implements ActionListener
{
   private JPanel jpBoard;
   private JPanel jpControls;
   private JPanel jpTurn;
   private JButton [][] tiles;
   private JButton reset;
   private JButton startGame;
   private ImageIcon red = new ImageIcon("red.png");                             // Red player's peice
   private ImageIcon blue = new ImageIcon("blue.jpg");                           // Blue player's peice
   private ImageIcon black = new ImageIcon("black.png");                         // Background image for each JButton
   private ImageIcon blackHighlighted = new ImageIcon("blackHighlighted.jpg");   // Shows possible moves.  NOTE: replaced white image (white.jpg)
   private ImageIcon blueClicked = new ImageIcon("blueClicked.jpg");             // Highlights if a blue peice was clicked
   private ImageIcon redClicked = new ImageIcon("redClicked.jpg");               // Highlights if a red peice was clicked
   private ImageIcon blueTake = new ImageIcon("blueTake.jpg");                   // This image will show up when being highlighted as a possible take from a red piece
   private ImageIcon redTake = new ImageIcon("redTake.jpg");                     // This image will show up when being highlighted as a possible take from a blue piece
   private JLabel jlTurn;
   private JTextField jtfTurn;
   private int a,b,c,d,x,y;   // Variables a and b for saving chosen piece. x and y for highlighting possible moves.
   private boolean turn;            // 
   private int clearWhite = 0;
   private int stopWhite = 0;       // Used to stop white space (black highlights) from being created after a piece is moved on the blue side.
   private int stopRed = 0;         // Used to stop red highlighted images from being created after a blue piece is moved into capture range of a red piece.
   private int stopBlue = 0;
   private int blueCounter = 0;
   private int redCounter = 0;
   private JTextField jtfBlueCounter;
   private JTextField jtfRedCounter;
   
   
     
   public static void main(String [] args)
   {
      new BreakThrough();
   }
   
   public BreakThrough()
   {
      turn = Math.random() > 0.5; // Initializing the boolean variable turn
      
      JMenuBar menuBar = new JMenuBar();
   
      /* File */
      JMenu fileMenu = new JMenu("File");
      fileMenu.setMnemonic('F');
      menuBar.add(fileMenu);
   
      // Exit
      JMenuItem exitMenuItem = new JMenuItem("Exit");
      exitMenuItem.setMnemonic('E');
      fileMenu.add(exitMenuItem);
      exitMenuItem.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               System.exit(0);
            }
         });
   
      /* Help */
      JMenu helpMenu = new JMenu("Help");
      helpMenu.setMnemonic('H');
      menuBar.add(helpMenu);
   
      // Instructions
      JMenuItem insMenuItem = new JMenuItem("Instructions");
      insMenuItem.setMnemonic('I');
      helpMenu.add(insMenuItem);
      insMenuItem.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               JOptionPane.showMessageDialog(null, "                                                                                             BreakThrough" + "\n" + 
                                       "1) Start:              1 player is selected to start at random." + "\n" + 
                                       "2) Movement:    Each peice can only move 1 space forward, 1 space diagonally to the left, or 1 space diagonally to the right." + "\n" + 
                                       "3) Capture:        A peice can only capture an opponents peice on a diagonal movement." + "\n" +
                                       "4) Win:                The first player to get one of their pieces across the entire board wins.");
            }
         });
   
      // About
      JMenuItem aboutMenuItem = new JMenuItem("About");
      aboutMenuItem.setMnemonic('A');
      helpMenu.add(aboutMenuItem);
      aboutMenuItem.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               JOptionPane.showMessageDialog(null, "BreakThrough" + " \n" + "Created by: Jason Kirshner, Harrison Worden, Jon Aurit, Alex Young" + "\n"
                                              + "Mini Project" + "\n" + "Version: 1.337");
            }
         });
      
      // JPanel South - Layout for Controls
      jpControls = new JPanel(new FlowLayout());
      
      // Layout for the board
      jpBoard = new JPanel(new GridLayout (8, 8));
      
      // Creates instances buttons in a 2D array of 8 x 8
      tiles = new JButton[8][8];
      
      // JPanel North - shows whos turn it is and how many pieces have been taken
      jpTurn = new JPanel();
      
      JLabel jlRedCounter = new JLabel("Red pieces taken: ");
      jpTurn.add(jlRedCounter);
      
      jtfRedCounter = new JTextField(redCounter + " ");
      jtfRedCounter.setEditable(false);
      jpTurn.add(jtfRedCounter);
      
      JLabel jlSpaceR = new JLabel("                                             "); // Used to format pieces taken on both sides of jtfTurn
      jpTurn.add(jlSpaceR);
      
      jlTurn = new JLabel("It is this player's turn: ");
      jpTurn.add(jlTurn);    
      
      jtfTurn = new JTextField(3);
      jtfTurn.setEditable(false);
      jpTurn.add(jtfTurn);
      
      JLabel jlSpaceB = new JLabel("                                             "); // Used to format pieces taken on both sides of jtfTurn
      jpTurn.add(jlSpaceB);
      
      JLabel jlBlueCounter = new JLabel("Blue pieces taken: ");
      jpTurn.add(jlBlueCounter);
      
      jtfBlueCounter = new JTextField(blueCounter + " ");
      jtfBlueCounter.setEditable(false);
      jpTurn.add(jtfBlueCounter);
      
      // Drawing the GUI
      drawBoard();
      drawControls();
      
      add(jpTurn, BorderLayout.NORTH);
      add(jpBoard, BorderLayout.CENTER);
      add(jpControls, BorderLayout.SOUTH);
      
      // JFrame settings
      setJMenuBar(menuBar);
      setPreferredSize(new Dimension(1000, 800));
      setTitle("Break Through");
      setLocationRelativeTo(null);
      setResizable(false);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
      pack();
      setLocationRelativeTo(null);
   }
   
   /* Mutator that creates the two buttons at the bottom of the JFrame */
   public void drawControls()
   {
      reset = new JButton("Reset Board");
      reset.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               resetBoard();
               startGame.setEnabled(true);
               jtfTurn.setText("");
               jtfTurn.setBackground(Color.WHITE);
               jtfBlueCounter.setText("0");
               jtfRedCounter.setText("0");
            }  
         });
      jpControls.add(reset);
      
      startGame = new JButton("Start Game");
      startGame.addActionListener(
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               turn = Math.random() > 0.5;
               if(turn == true)
               {
                  JOptionPane.showMessageDialog(null, "First move goes to Red!");
                  jtfTurn.setText("RED");
                  jtfTurn.setForeground(Color.WHITE);
                  jtfTurn.setBackground(Color.RED);
                  redCounter = 0;
                  blueCounter = 0;
               }
               else if(turn == false)
               {
                  JOptionPane.showMessageDialog(null, "First move goes to Blue!");
                  jtfTurn.setText("BLUE");
                  jtfTurn.setForeground(Color.WHITE);
                  jtfTurn.setBackground(Color.BLUE);
                  blueCounter = 0;
                  redCounter = 0;
               }
               drawPieces();
               for(int row = 0; row < tiles.length; row++)
               {
                  for(int col = 0; col < tiles.length; col++)
                  {
                     tiles[row][col].setEnabled(true);
                  }
               }
               startGame.setEnabled(false);
            }  
         });
      jpControls.add(startGame);
   }
   
   /* Lays down the blue and red pieces */
   public void drawPieces()
   {
      for(int row = 0; row < 2; row++)
      {
         for(int col = 0; col < 8; col++)
         {
            tiles[row][col].setIcon(red);
         }
      }
      
      for(int row = 6; row < 8; row++)
      {
         for(int col = 0; col < 8; col++)
         {
            tiles[row][col].setIcon(blue);
         }
      }
   }
   
   /* Adds actionlisteners to each button */
   public void drawBoard()
   {
      for(int row = 0; row < tiles.length; row++)
      {
         for(int col = 0; col < tiles.length; col++)
         { 
            tiles[row][col] = new JButton();
            tiles[row][col].setIcon(black);
            tiles[row][col].setPreferredSize(new Dimension(50, 50));
            jpBoard.add(tiles[row][col]);
            tiles[row][col].addActionListener(this);
         }
      }
   }
   /* Resets board */
   public void resetBoard()
   {
      jtfTurn.setText("");
      for(int row = 0; row < tiles.length; row++)
      {
         for(int col = 0; col < tiles.length; col++)
         { 
            tiles[row][col].setIcon(black);
            tiles[row][col].setPreferredSize(new Dimension(50, 50));
            jpBoard.add(tiles[row][col]);
            tiles[row][col].addActionListener(this);
         }
      }
   
      for(int row = 0; row < 2; row++)
      {
         for(int col = 0; col < 8; col++)
         {
            tiles[row][col].setIcon(red);
         }
      }
      
      for(int row = 6; row < 8; row++)
      {
         for(int col = 0; col < 8; col++)
         {
            tiles[row][col].setIcon(blue);
         }
      }
      
      for(int row = 0; row < tiles.length; row++)
      {
         for(int col = 0; col < tiles.length; col++)
         {
            tiles[row][col].setEnabled(false);
         }
      }
      
   }//end resetBoard()
   
   public void determineWinner()
   {
      // Winning Message
      for( int i = 0; i < 8; i++)
      {
         if (tiles[7][i].getIcon().equals(red))
         {
            gameOverSound();
            JOptionPane.showMessageDialog(null, "Red Wins!");
            jtfTurn.setText("");
            jtfTurn.setBackground(Color.WHITE);
            // Disables all buttons
            for(int row = 0; row < tiles.length; row++)
            {
               for(int col = 0; col < tiles.length; col++)
               {
                  tiles[row][col].setEnabled(false);
               }
            }
            break;
         } 
         else if(tiles[0][i].getIcon().equals(blue))
         {
            gameOverSound();
            JOptionPane.showMessageDialog(null, "Blue Wins!");
            jtfTurn.setText("");
            jtfTurn.setBackground(Color.WHITE);
            // Disables all buttons
            for(int row = 0; row < tiles.length; row++)
            {
               for(int col = 0; col < tiles.length; col++)
               {
                  tiles[row][col].setEnabled(false);
               }
            }
            break;       
         }
      } // end of for 
   } // end of determineWinner
   
   /* Halo Game Over */
   public void gameOverSound()
   {
      try
      {
         File gameOver = new File("gameover.wav");
         AudioInputStream stream = AudioSystem.getAudioInputStream(gameOver);
         AudioFormat format = stream.getFormat();
         DataLine.Info info = new DataLine.Info(Clip.class, format);
         Clip clip = (Clip) AudioSystem.getLine(info);
         clip.open(stream);
         clip.start();
      }
      catch(Exception e){}
   }
   
   /* Clears the highlighted tiles after a move has taken place */
   public void clearBlackHighlighted()
   {
      for(int i = 0; i < 8; i++) // for removing all blackHighlighted spaces that were created by highlights
      {
         for(int j = 0; j < 8; j++)
         {
            if(tiles[i][j].getIcon().equals(blackHighlighted))
            {
               tiles[i][j].setIcon(black);
            } // end of if
         } // end of inner for
      } // end of outer for
   } // end of clearWhite
   
   /* Clears the highlighted red pieces */
   public void clearRedHighlight()
   {
      for(int i = 0; i < 8; i++) // for removing all blackHighlighted spaces that were created by highlights
      {
         for(int j = 0; j < 8; j++)
         {
            if(tiles[i][j].getIcon().equals(redClicked))
            {
               tiles[i][j].setIcon(red);
            } // end of if
         } // end of inner for
      } // end of outer for
   } // end of clearRedHighlight
   
   /* Clears the highlighted blue pieces */
   public void clearBlueHighlight()
   {
      for(int i = 0; i < 8; i++) // for removing all blackHighlighted spaces that were created by highlights
      {
         for(int j = 0; j < 8; j++)
         {
            if(tiles[i][j].getIcon().equals(blueClicked))
            {
               tiles[i][j].setIcon(blue);
            } // end of if
         } // end of inner for
      } // end of outer for
   } // end of clearBlueHighlight
   
   /* Clears the hihglited black tiles with the red piece inside */
   public void clearRedTake()
   {
      for(int i = 0; i < 8; i++) // for removing all redTake spaces that were created by blue highlights
      {
         for(int j = 0; j < 8; j++)
         {
            if(tiles[i][j].getIcon().equals(redTake))
            {
               tiles[i][j].setIcon(red);
            } // end of if
         } // end of inner for
      } // end of outer for
   } // end of clearRedTake
   
   public void clearBlueTake()
   {
      for(int i = 0; i < 8; i++) // for removing all blueTake spaces that were created by blue highlights
      {
         for(int j = 0; j < 8; j++)
         {
            if(tiles[i][j].getIcon().equals(blueTake))
            {
               tiles[i][j].setIcon(blue);
            } // end of if
         } // end of inner for
      } // end of outer for
   } // end of clearBlueTake


   /**
    *********************** Move Validation ************************
    */
   public void actionPerformed(ActionEvent ae)
   {
      // User's selection
      Object choice = ae.getSource();
      
      /**
       * Red's turn
       */  
      if(turn == true)
      {
         if(clearWhite == 0)
         {
            clearBlackHighlighted();
            clearRedTake();
            clearBlueTake();
            clearWhite++;
         }
         stopBlue = 0;
         // Runs through each row
         loop:
         for(int row = 0; row < tiles.length; row++)
         {
            // Runs through each column
            for(int col = 0; col < tiles.length; col++)
            {
               if(tiles[7][col].getIcon().equals(red))
               {
                  determineWinner();
                  break loop;
               }
               // Records the selected piece
               if(choice == tiles[row][col] && tiles[row][col].getIcon().equals(red))
               {
                  a = row;
                  b = col;
                  c = col;
                  d = col;
                  
                  clearRedHighlight();
                  clearBlueHighlight();
                  clearRedTake();
                  clearBlueTake();
                  
                  // Highlight Squares where peice can move
                  x = row;
                  y = col;
                  clearBlackHighlighted();
                  tiles[row][col].setIcon(redClicked); // Changes image to when with a highlight around. So the user knows which peice is selected.
               
                  /**      
                     HighLighting Section. Each one of these is a possible combination of black spaces.   
                  **/        
                  try
                  {
                     if( y > 0 && y < 7)
                     {
                        if( tiles[x+1][y].getIcon().equals(black) || tiles[x+1][y-1].getIcon().equals(black) || tiles[x+1][y+1].getIcon().equals(black) )
                        {
                           if( tiles[x+1][y].getIcon().equals(black) ) // down
                           {
                              tiles[x+1][y].setIcon(blackHighlighted);
                           
                              if( y > 0 ) // for very right column
                              {
                                 if( tiles[x+1][y-1].getIcon().equals(black) ) // left
                                 {
                                    tiles[x+1][y-1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x+1][y+1].getIcon().equals(black) ) // right
                                    {
                                       tiles[x+1][y+1].setIcon(blackHighlighted);
                                    } // end of if
                                 } // down, left, right
                              }
                              
                              if( y < 7 )
                              {
                                 if( tiles[x+1][y+1].getIcon().equals(black) ) // right
                                 {
                                    tiles[x+1][y+1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x+1][y-1].getIcon().equals(black) ) // left
                                    {
                                       tiles[x+1][y-1].setIcon(blackHighlighted); 
                                    }
                                 } 
                              } 
                           } 
                           
                           if( y > 0 ) // Stopping left col from causing error
                           {   
                              if( tiles[x+1][y-1].getIcon().equals(black) ) // left
                              {
                                 tiles[x+1][y-1].setIcon(blackHighlighted);
                              
                                 if( tiles[x+1][y].getIcon().equals(black) ) // down  
                                 {
                                    tiles[x+1][y].setIcon(blackHighlighted);
                                 
                                    if ( tiles[x+1][y+1].getIcon().equals(black) ) // right
                                    {
                                       tiles[x+1][y+1].setIcon(blackHighlighted);
                                    } 
                                 } 
                                 if( tiles[x+1][y+1].getIcon().equals(black) ) // right
                                 {
                                    tiles[x+1][y+1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x+1][y].getIcon().equals(black) ) // down
                                    {
                                       tiles[x+1][y].setIcon(blackHighlighted);
                                    }
                                 } // end of if     
                              }
                           }       
                                              
                           if( tiles [x+1][y+1].getIcon().equals(black) ) // right
                           {
                              tiles[x+1][y+1].setIcon(blackHighlighted);
                           
                              if( tiles[x+1][y].getIcon().equals(black) ) // down
                              {
                                 tiles[x+1][y].setIcon(blackHighlighted);
                              
                                 if( tiles[x+1][y-1].getIcon().equals(black) ) // left
                                 {
                                    tiles[x+1][y-1].setIcon(blackHighlighted);
                                 }
                              } // end of right, down, left
                           
                              if( y > 0 )
                              {
                                 if( tiles[x+1][y-1].getIcon().equals(black) ) // left
                                 {
                                    tiles[x+1][y-1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x+1][y].getIcon().equals(black) ) // down
                                    {
                                       tiles[x+1][y].setIcon(blackHighlighted);
                                    }
                                 } // end of right, left, down
                              } // end of y > 0
                           
                           }
                        }
                     } // end of y restraints
                    
                     if( y < 7 )
                     {
                        if( tiles[x+1][y].getIcon().equals(black)|| tiles[x+1][y+1].getIcon().equals(black) )
                        {
                           if( tiles [x+1][y+1].getIcon().equals(black) ) // right
                           {
                              tiles[x+1][y+1].setIcon(blackHighlighted);
                           
                              if( tiles[x+1][y].getIcon().equals(black) ) // down
                              {
                                 tiles[x+1][y].setIcon(blackHighlighted);  
                              }   
                           }
                           if( tiles[x+1][y].getIcon().equals(black) ) // down  
                           {
                              tiles[x+1][y].setIcon(blackHighlighted);
                                 
                              if ( tiles[x+1][y+1].getIcon().equals(black) ) // right
                              {
                                 tiles[x+1][y+1].setIcon(blackHighlighted);
                              } 
                           }
                        } 
                     } 
                     
                     if( tiles[x+1][y].getIcon().equals(black)|| tiles[x+1][y-1].getIcon().equals(black) )
                     {
                        if( tiles [x+1][y-1].getIcon().equals(black) ) // left
                        {
                           tiles[x+1][y-1].setIcon(blackHighlighted);
                           
                           if( tiles[x+1][y].getIcon().equals(black) ) // down
                           {
                              tiles[x+1][y].setIcon(blackHighlighted);  
                           }   
                        }
                        if( tiles[x+1][y].getIcon().equals(black) ) // down  
                        {
                           tiles[x+1][y].setIcon(blackHighlighted);
                                 
                           if ( tiles[x+1][y-1].getIcon().equals(black) ) // left
                           {
                              tiles[x+1][y-1].setIcon(blackHighlighted);
                           } 
                        } 
                     }                      
                  } // end of try
                  catch(ArrayIndexOutOfBoundsException aioobe){}
                                  
                  /**      
                     Section for changing possible blue captures. Only left and right moves
                  **/        
                  try
                  {
                     if( stopBlue == 0 )
                     {
                        if( y > 0 )
                        {
                           if( tiles[x+1][y-1].getIcon().equals(blue) || tiles[x+1][y+1].getIcon().equals(blue) ) 
                           {                                
                              if( tiles[x+1][y-1].getIcon().equals(blue) ) // left
                              {
                                 tiles[x+1][y-1].setIcon(blueTake);
                              
                                 if( tiles[x+1][y+1].getIcon().equals(blue) ) // right  
                                 {
                                    tiles[x+1][y+1].setIcon(blueTake);
                                 } 
                              }   
                              if( tiles [x+1][y+1].getIcon().equals(blue) ) // right
                              {
                                 tiles[x+1][y+1].setIcon(blueTake);
                              
                                 if( tiles[x+1][y-1].getIcon().equals(blue) ) // left
                                 {
                                    tiles[x+1][y-1].setIcon(blueTake);
                                 
                                 } // end of right, left
                              }
                           }
                        } // end of tiles statement (or)
                        if( y < 7 )
                        { 
                           if( tiles[x+1][y+1].getIcon().equals(blue) ) // right
                           {
                              tiles[x+1][y+1].setIcon(blueTake);  
                           }                                  
                        } 
                     } // end of stop blue
                  } // end of try
                  catch(ArrayIndexOutOfBoundsException aioobe){}               
               }
               
               try
               {
                  // Red down one
                  if(choice == tiles[a+1][b] && tiles[a][b].getIcon().equals(redClicked) && tiles[a+1][b].getIcon().equals(blackHighlighted))
                  {
                     stopBlue = 1;
                     tiles[a][b].setIcon(black);
                     tiles[a+1][b].setIcon(red);
                     turn = false;
                     jtfTurn.setText("BLUE");
                     jtfTurn.setForeground(Color.WHITE);
                     jtfTurn.setBackground(Color.BLUE);
                     tiles[x+1][y].setIcon(red);  
                     clearBlackHighlighted();                                                                                                              
                  } // end of if
                  
                  if(c > 0) // Selected piece is not on the left edge of board
                  {
                     // Red down one left one
                     if(choice == tiles[a+1][b-1] && tiles[a][b].getIcon().equals(redClicked) && tiles[a+1][b-1].getIcon().equals(blackHighlighted))
                     {
                        stopBlue = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a+1][b-1].setIcon(red);
                        turn = false;
                        jtfTurn.setText("BLUE");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.BLUE); 
                        tiles[x+1][y-1].setIcon(red);
                        clearBlackHighlighted();
                     }
                     else if(choice == tiles[a+1][b-1] && tiles[a][b].getIcon().equals(redClicked) && tiles[a+1][b-1].getIcon().equals(blueTake))
                     {
                        stopBlue = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a+1][b-1].setIcon(red);
                        turn = false;
                        jtfTurn.setText("BLUE");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.BLUE);
                        blueCounter++;
                       // String bcString = String.valueOf(blueCounter);
                       // jtfBlueCounter.setText(bcString);
                        jtfBlueCounter.setText(String.valueOf(blueCounter));
                     }
                  }
                  
                  if(d < 7) // Selected piece is not on the right edge of board
                  {
                     // Red down one right one
                     if(choice == tiles[a+1][b+1] && tiles[a][b].getIcon().equals(redClicked) && tiles[a+1][b+1].getIcon().equals(blackHighlighted))
                     {
                        stopBlue = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a+1][b+1].setIcon(red);
                        turn = false;
                        jtfTurn.setText("BLUE");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.BLUE);
                        tiles[x+1][y+1].setIcon(red); 
                        clearBlackHighlighted();
                     }
                     else if(choice == tiles[a+1][b+1] && tiles[a][b].getIcon().equals(redClicked) && tiles[a+1][b+1].getIcon().equals(blueTake))
                     {
                        stopBlue = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a+1][b+1].setIcon(red);
                        turn = false;
                        jtfTurn.setText("BLUE");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.BLUE); 
                        blueCounter++;
                        jtfBlueCounter.setText(String.valueOf(blueCounter));
                     }
                  }
               }
               catch(ArrayIndexOutOfBoundsException aioobe){}
            }//inner red loop
         }//outer red loop
      }//end red turn
      
      
      
   
      /**
       * Blue's turn
       */
      if(turn == false)
      {  
         clearRedHighlight();
         
         if(clearWhite == 1)
         {
            clearBlackHighlighted();
            clearRedTake();
            clearWhite--;
         }
         stopWhite = 0;
         stopRed = 0;
         // Running through each row
         loop:
         for(int row = 0; row < tiles.length; row++)
         {  
            // Running through each column
            for(int col = 0; col < tiles.length; col++)
            {
               if(tiles[0][col].getIcon().equals(blue))
               {
                  determineWinner();
                  break loop;
               }
               // Records the selected piece
               if(choice == tiles[row][col] && tiles[row][col].getIcon().equals(blue))
               {
                  a = row;
                  b = col;
                  c = col;
                  d = col;
                  
                  clearBlueHighlight(); // Clears blue highlighted pieces when user clicks a different blue piece
                  
                                    // Highlight Squares where peice can move
                  x = row;
                  y = col;
                  clearBlackHighlighted();
                  clearRedTake();
               
                  /**      
                     HighLighting Section. Each one of these is a possible combination of black spaces.   
                  **/        
                  try
                  {
                     if( stopWhite == 0 ) // Used as a counter to keep highlighted moves from popping up after the second click
                     {
                        tiles[row][col].setIcon(blueClicked);
                        
                        if( y > 0 && y < 7 )
                        {
                           if( tiles[x-1][y].getIcon().equals(black) || tiles[x-1][y-1].getIcon().equals(black) || tiles[x-1][y+1].getIcon().equals(black) ) 
                           {
                              if( tiles[x-1][y].getIcon().equals(black) ) // up
                              {
                                 tiles[x-1][y].setIcon(blackHighlighted);
                              
                                 if( y > 0 ) // Stopping left col from causing error
                                 {
                                    if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                    {
                                       tiles[x-1][y-1].setIcon(blackHighlighted);
                                    
                                       if( tiles[x-1][y+1].getIcon().equals(black) ) // right
                                       {
                                          tiles[x-1][y+1].setIcon(blackHighlighted);
                                       } // end of if
                                    } // up, left, right
                                 }
                                 else if( tiles[x-1][y+1].getIcon().equals(black) ) // right
                                 {
                                    tiles[x-1][y+1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                    {
                                       tiles[x-1][y-1].setIcon(blackHighlighted); 
                                    }
                                 }  
                              } 
                              
                              if( y > 0 ) // Stopping left col from causing error
                              { 
                                 if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                 {
                                    tiles[x-1][y-1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x-1][y].getIcon().equals(black) ) // up 
                                    {
                                       tiles[x-1][y].setIcon(blackHighlighted);
                                    
                                       if ( tiles[x-1][y+1].getIcon().equals(black) ) // right
                                       {
                                          tiles[x-1][y+1].setIcon(blackHighlighted);
                                       } 
                                    } 
                                 }
                              }
                              
                              if( y > 0 ) // Stopping left col from causing error
                              {
                                 if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                 {
                                    if( tiles[x-1][y+1].getIcon().equals(black) ) // right
                                    {
                                       tiles[x-1][y+1].setIcon(blackHighlighted);
                                    
                                       if( tiles[x-1][y].getIcon().equals(black) ) // up
                                       {
                                          tiles[x-1][y].setIcon(blackHighlighted);
                                       }
                                    } // end of if right
                                 } // end of if left
                              }// end of y > 0
                              
                              if( tiles [x-1][y+1].getIcon().equals(black) ) // right
                              {
                                 tiles[x-1][y+1].setIcon(blackHighlighted);
                              
                                 if( tiles[x-1][y].getIcon().equals(black) ) // up
                                 {
                                    tiles[x-1][y].setIcon(blackHighlighted);
                                 
                                    if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                    {
                                       tiles[x-1][y-1].setIcon(blackHighlighted);
                                    }
                                 } // end of right, up, left
                                 
                                 else if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                                 {
                                    tiles[x-1][y-1].setIcon(blackHighlighted);
                                 
                                    if( tiles[x-1][y].getIcon().equals(black) ) // up
                                    {
                                       tiles[x-1][y].setIcon(blackHighlighted);
                                    }
                                 } // end of right, left, up
                              }// end of right, left, up
                           
                           
                           } // end of if
                        } // end of y > 0 && y < 7
                        
                        if( y > 0 )
                        {                       
                           if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                           {
                              tiles[x-1][y-1].setIcon(blackHighlighted);
                                 
                              if( tiles[x-1][y].getIcon().equals(black) ) // up 
                              {
                                 tiles[x-1][y].setIcon(blackHighlighted);
                              }
                           }
                           if( tiles[x-1][y].getIcon().equals(black) ) // up
                           {
                              tiles[x-1][y].setIcon(blackHighlighted);
                                 
                              if( tiles[x-1][y-1].getIcon().equals(black) ) // left
                              {
                                 tiles[x-1][y-1].setIcon(blackHighlighted);
                              } 
                           }                                 
                        }
                        
                        if( y < 7 )
                        {
                           if( tiles[x-1][y+1].getIcon().equals(black) ) // right
                           {
                              tiles[x-1][y+1].setIcon(blackHighlighted);
                                 
                              if( tiles[x-1][y].getIcon().equals(black) ) // up 
                              {
                                 tiles[x-1][y].setIcon(blackHighlighted);
                              }
                           }
                           if( tiles[x-1][y].getIcon().equals(black) ) // up
                           {
                              tiles[x-1][y].setIcon(blackHighlighted);
                                 
                              if( tiles[x-1][y+1].getIcon().equals(black) ) // right
                              {
                                 tiles[x-1][y+1].setIcon(blackHighlighted);
                              } 
                           }  
                        }
                     } // end of stopWhite
                  } // end of try
                  catch(ArrayIndexOutOfBoundsException aioobe){}
               
                  /**      
                     Section for changing possible blue captures. Only left and right moves
                  **/        
                  try
                  {
                     if( stopRed == 0 )
                     {
                        if( y > 0 && y < 7 )
                        {
                           if( tiles[x-1][y-1].getIcon().equals(red) || tiles[x-1][y+1].getIcon().equals(red) ) 
                           {
                              if( y > 0 ) // Stopping left col from causing error
                              { 
                                 if( tiles[x-1][y-1].getIcon().equals(red) ) // left
                                 {
                                    tiles[x-1][y-1].setIcon(redTake);
                                 
                                    if( tiles[x-1][y+1].getIcon().equals(red) ) // right  
                                    {
                                       tiles[x-1][y+1].setIcon(redTake);
                                    } 
                                 }
                              }// end of if y > 0
                              
                              if( tiles [x-1][y+1].getIcon().equals(red) ) // right
                              {
                                 tiles[x-1][y+1].setIcon(redTake);
                              
                                 if( tiles[x-1][y-1].getIcon().equals(red) ) // left
                                 {
                                    tiles[x-1][y-1].setIcon(redTake);
                                 
                                 } // end of right, left
                                 else if( tiles[x-1][y-1].getIcon().equals(red) ) // left
                                 {
                                    tiles[x-1][y-1].setIcon(redTake);
                                 
                                 }
                              }// end of right, left, down
                           } // end of if ||
                        } // end of y > 0 && y < 7
                        
                        if( y > 0 ) // Accounts for columns 1-7
                        {
                           if( tiles[x-1][y-1].getIcon().equals(red) ) // left
                           {
                              tiles[x-1][y-1].setIcon(redTake);   
                           } 
                        }
                        
                        if ( y < 7 ) // Accounts for columns 1-6
                        {
                           if( tiles[x-1][y+1].getIcon().equals(red) ) // right
                           {
                              tiles[x-1][y+1].setIcon(redTake);   
                           } // end of right, left
                        }
                     } // end of stopRed
                     
                  } // end of try
                  catch(ArrayIndexOutOfBoundsException aioobe){}
               }
               
               try
               {
                  // Blue up one
                  if(choice == tiles[a-1][b] && tiles[a][b].getIcon().equals(blueClicked) && tiles[a-1][b].getIcon().equals(blackHighlighted))
                  {
                     stopWhite = 1;
                     stopRed = 1;
                     tiles[a][b].setIcon(black);
                     tiles[a-1][b].setIcon(blue);
                     turn = true;
                     jtfTurn.setText("RED");
                     jtfTurn.setForeground(Color.WHITE);
                     jtfTurn.setBackground(Color.RED);
                     tiles[x-1][y].setIcon(blue);
                     clearBlackHighlighted();
                  }
                  if(c > 0) // Selected piece is not on the left edge of board
                  {
                     // Blue up one left one
                     if(choice == tiles[a-1][b-1] && tiles[a][b].getIcon().equals(blueClicked) && tiles[a-1][b-1].getIcon().equals(blackHighlighted))
                     {
                        stopWhite = 1;
                        stopRed = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a-1][b-1].setIcon(blue);
                        turn = true;
                        jtfTurn.setText("RED");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.RED);
                        clearBlackHighlighted(); 
                        
                     }
                     else if(choice == tiles[a-1][b-1] && tiles[a][b].getIcon().equals(blueClicked) && tiles[a-1][b-1].getIcon().equals(redTake))
                     {
                        stopWhite = 1;
                        stopRed = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a-1][b-1].setIcon(blue);
                        turn = true;
                        jtfTurn.setText("RED");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.RED);
                        redCounter++;
                        jtfRedCounter.setText(String.valueOf(redCounter));
                        
                     }
                  }
                  if(d < 7) // Selected piece is not on the right edge of board
                  {
                     // Blue up one right one
                     if(choice == tiles[a-1][b+1] && tiles[a][b].getIcon().equals(blueClicked) && tiles[a-1][b+1].getIcon().equals(blackHighlighted))
                     {
                        stopWhite = 1;
                        stopRed = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a-1][b+1].setIcon(blue);
                        turn = true;
                        jtfTurn.setText("RED");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.RED);
                        clearBlackHighlighted();
                     }
                     else if(choice == tiles[a-1][b+1] && tiles[a][b].getIcon().equals(blueClicked) && tiles[a-1][b+1].getIcon().equals(redTake))
                     {
                        stopWhite = 1;
                        stopRed = 1;
                        tiles[a][b].setIcon(black);
                        tiles[a-1][b+1].setIcon(blue);
                        turn = true;
                        jtfTurn.setText("RED");
                        jtfTurn.setForeground(Color.WHITE);
                        jtfTurn.setBackground(Color.RED);
                        redCounter++;
                        jtfRedCounter.setText(String.valueOf(redCounter));
                     }
                  }
               } // end of try
               catch(ArrayIndexOutOfBoundsException aioobe){}
            }//inner blue loop
         }//outer blue loop
      }//end Blue's turn
   }//end actionPerformed 
}//end class