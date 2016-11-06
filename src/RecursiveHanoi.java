import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/*
 * All right, so this is a basic recursive-based Towers of Hanoi program,
 * I was looking to make an android calculator app and wanted to clear the rust covering my Java skills,
 * So I made this run through of the towers of hanoi problem.
 * 
 * I mostly had to remind myself of the graphics portion of Java,
 * It worked out well and I liked it, so here is my Recursive Hanoi.
 */
public class RecursiveHanoi extends JPanel implements Runnable, ActionListener {
	    
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static double screenWidth = screenSize.getWidth();
	static double screenHeight = screenSize.getHeight();
	//this is to simply know the user's sreen resolution for a tailored viewing experience

    static final int displayWidth = (int) ( screenWidth * 2/3 );
    static final int displayHeight = (int) ( screenHeight * 1/3 );
    //this is the actual proportions for the application
    
	static final int screenPosWidth = (int) ( (screenWidth / 2) - (displayWidth / 2) );
    static final int screenPosHeight = (int) ( (screenHeight / 2) - (displayHeight * 2/3 ) );
    //sed to place on the screen
    
   
   public static void main(String[] args) {
      JFrame window = new JFrame("Recursive Hanoi");
      window.setContentPane(new RecursiveHanoi());
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      window.pack();  
      window.setResizable(false);
      window.setLocation(screenPosWidth, screenPosHeight);
      window.setVisible(true);
      
      //this is a general main method for the graphical application
      //it merely sets the application for viewing
      
   }
   
   //private static final Color colourTheme[] = { backgroundColour , restDiskColour , moveDiskColour };
   private static final Color redTheme[] = { new Color(241, 169, 160) , new Color(214, 69, 65) , new Color(231, 76, 60) };
   private static final Color greenTheme[] = { new Color(134, 226, 213) , new Color(22, 160, 133) , new Color(42, 187, 155) };
   private static final Color blueTheme[] = { new Color(197, 239, 247) , new Color(75, 119, 190) , new Color(30, 139, 195) };
   
   private static Color currentColourTheme[] = new Color[3];
   //this is used to draw and colour the display based on one of the three themes above
   //this array holds one of the three final arrays above
   
   private static final Color borderColour = new Color(52, 73, 94);
   //the border colour is constant regardless of the three colours themes
   
   public static void colourParse(String condit){
	   //this basically ust populates the currentColourTheme with one of the three theme arrays above
	   //it is called later on based on the pressing of the themeButton, that switches the theme colours
	   	   
	   if(condit == buttonText[4]){
		   
		   themeButton.setText(buttonText[5]);
		   
		   for(int i = 0; i < currentColourTheme.length; i++)
			   currentColourTheme[i] = greenTheme[i];
		   //if its red, then turn it green
		   
	   }
	   
	   else if(condit == buttonText[5]){
		   
		   themeButton.setText(buttonText[6]);
		   
		   for(int i = 0; i < 3; i++)
			   currentColourTheme[i] = blueTheme[i];
		 //if its green, then turn it blue
	   }
	   
	   else if(condit == buttonText[6]){
		   
		   themeButton.setText(buttonText[4]);
		   
		   for(int i = 0; i < 3; i++)
			   currentColourTheme[i] = redTheme[i];
		 //if its blue, then turn it red
	   }
	   
	   else{
		   themeButton.setText(buttonText[6]);
		   for(int i = 0; i < 3; i++)
			   currentColourTheme[i] = blueTheme[i];
		 //as default it starts blue, because I like the blue theme most
	   }
	   
   }
      
   private BufferedImage OSC;   //this is an off-screen canvas, so I draw stuff here and then copy it over to the actual display
   private int status;   //this is used to determine what must be run when, and especially the thread that controls these operations
   
   private static final int runAnim = 0;       		//this means its running
   private static final int pauseAnim = 1;    		//it sets the thread to a paused state
   private static final int stepPauseAnim = 2;  	//this basically acts as both of the above, it runs once then pauses
   private static final int restartAnim = 3;  		//resets the whole simulation
     
   private static final String buttonText[] = {"Auto-Run", "Pause", "Next Step", "Restart", "RGB: Red", "RGB: Green", "RGB: Blue"};
   //this is to hold all the text values rather than create multiple strings

   private int[][] tower;		//this holds the population of the three towers later on
   private int[] towerHeight;	//this is to keep track of how high the towers are
   private int moveDisk;		//just to move
   private int moveTower;		//again to move

   private Display display;  //this is the actual display where the animation is placed

   private static JButton runPauseButton = new JButton(buttonText[0]);  //run the simulation (or pause when its already running)
   private static JButton nextStepButton = new JButton(buttonText[2]);	//step once through the solution
   private static JButton restartButton = new JButton(buttonText[3]);	//reset the whole thing
   private static JButton themeButton = new JButton(buttonText[6]);		//set the theme of the display
      
   private static final Font defaultFont = new Font("Arial", Font.BOLD, (int)(displayHeight * 0.042f));
   //this is used to set the font of the buttons, and to keep the size relative to the screen size

     private class Display extends JPanel {
      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         int x = (getWidth() - OSC.getWidth())/2;
         int y = (getHeight() - OSC.getHeight())/2;
         g.drawImage(OSC, x, y, null);
         //this is to simply create the aforementioned display for the drawing
      }
   }

   
   public RecursiveHanoi () {	//good ole constructor for the whole thing
	   
	  colourParse("start");
	   
      OSC = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_RGB);
      display = new Display();
      display.setPreferredSize(new Dimension(displayWidth, displayHeight));
      display.setBorder(BorderFactory.createLineBorder(borderColour, (int) (displayWidth * 0.006f)));
      display.setBackground(currentColourTheme[0]);
      setLayout(new BorderLayout());
      add(display, BorderLayout.CENTER);
      
      //from here onwards is where the display is set up
      
      JPanel buttonBar = new JPanel();
      add(buttonBar, BorderLayout.SOUTH);
      buttonBar.setLayout(new GridLayout(1,0));
      
      //buttons follow
      
      runPauseButton.setFont(defaultFont);
      runPauseButton.addActionListener(this);
      buttonBar.add(runPauseButton);
            
      nextStepButton.setFont(defaultFont);
      nextStepButton.addActionListener(this);
      buttonBar.add(nextStepButton);
      
      restartButton.setFont(defaultFont);
      restartButton.addActionListener(this);
      restartButton.setEnabled(false);
      buttonBar.add(restartButton);
            
      themeButton.setFont(defaultFont);
      themeButton.addActionListener(this);
      buttonBar.add(themeButton);
      
      new Thread(this).start();		//and here we go!
   }

   
   synchronized public void actionPerformed(ActionEvent evt) {
      Object source = evt.getSource();
      if (source == runPauseButton) {  //run through the solution, or pause it if already rnning
         if (status == runAnim) {  //here we pause it if its already running
            status = pauseAnim;
            nextStepButton.setEnabled(true);
            runPauseButton.setText(buttonText[0]);
            themeButton.setEnabled(true);
         }
         else {  //if its paused, or has never been run, we set it to run
            status = runAnim;
            nextStepButton.setEnabled(false);
            runPauseButton.setText(buttonText[1]);
            themeButton.setEnabled(false);
         }
      }
      else if (source == nextStepButton) {  //one step in the solution
         status = stepPauseAnim;
      }
      else if (source == restartButton) { //reset the whole thing
         status = restartAnim;
      }
      else if( source == themeButton){	//change the theme colour
    	  colourParse(themeButton.getText());
    	  
    	  Graphics g = OSC.getGraphics();
    	  currentFrameDraw(g);
    	  g.dispose();
    	  
    	  display.repaint();
      }
    	  
            
      notify();  //notify the thread of the changes, basically
   }


    public void run() {
      while (true) {
         runPauseButton.setText(buttonText[0]);
         nextStepButton.setEnabled(true);
         restartButton.setEnabled(false);
         setUpProblem();  //this sets up the problem/animation
         status = pauseAnim;
         checkStatus(); 
         restartButton.setEnabled(true);
         try {
            solve(10,0,1,2);  //we go ahead and try to solve the problem starting with ten disks
         }
         catch (IllegalStateException e) {
               // Exception was thrown because user clicked "Start Over".
         }         
      }
   }

   
   synchronized private void checkStatus() {	//this just basically checks if we should be waiting or not
      while (status == pauseAnim) {
         try {
            wait();
         }
         catch (InterruptedException e) {
         }
      }
      if (status == restartAnim)
         throw new IllegalStateException("restartAnim");
   }
   
   
    synchronized private void setUpProblem() {
	   
      moveDisk= 0;
      tower = new int[3][10];
      for (int i = 0; i < 10; i++)
         tower[0][i] = 10 - i;
      towerHeight = new int[3];
      towerHeight[0] = 10;
      if (OSC != null) {
         Graphics g = OSC.getGraphics();
         currentFrameDraw(g);
         g.dispose();
      }
      display.repaint();
      
      //basically setting up the problem with ten disks
      
   }
   

   
   private void solve(int disks, int from, int to, int spare) {
      if (disks == 1)
         moveOne(from,to);
      else {
         solve(disks-1, from, spare, to);
         moveOne(from,to);
         solve(disks-1, spare, to, from);
      }
      //this is the ehart of the recursive solution,
      //this ensures that the disks are moved to the correct tower
   }


   
   synchronized private void moveOne(int fromStack, int toStack) {
	  moveDisk = tower[fromStack][towerHeight[fromStack]-1];
      moveTower = fromStack;
      delay(260);
      towerHeight[fromStack]--;
      putDisk(currentColourTheme[2],moveDisk,moveTower);
      delay(120);
      putDisk(currentColourTheme[0],moveDisk,moveTower);
      delay(120);
      moveTower = toStack;
      putDisk(currentColourTheme[2],moveDisk,moveTower);
      delay(120);
      putDisk(currentColourTheme[1],moveDisk,moveTower);
      
      //here I move the disk to the appropriate tower, while cycling through the colours I set up
      
      tower[toStack][towerHeight[toStack]] = moveDisk;
      towerHeight[toStack]++;
      moveDisk = 0;
      if (status == stepPauseAnim)
         status = pauseAnim;
      checkStatus();
   }

   
   
   synchronized private void delay(int milliseconds) {
      try {
         wait(milliseconds);
      }
      catch (InterruptedException e) {
      }
      //rather than go through at lightning fast speed, this will actually let the user see how it is solved
   }

   
   synchronized private void putDisk(Color colour, int disk, int t) {
      Graphics g = OSC.getGraphics();
      g.setColor(colour);
      g.fillRoundRect((int) (displayWidth * 0.16f)+(int)(displayWidth * 0.325f)*t - (int) (displayWidth * 0.011f)*disk - (int) (displayWidth * 0.011f),
				(int) (displayHeight * 0.8f)-(int) (displayHeight * 0.083f)*towerHeight[t], 
				(int) (displayWidth * 0.024f)*disk+(int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f), 
				(int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f));
      g.dispose();
      display.repaint();
      //this is to actually place the disk in the appropriate tower/stack
   }


   
   synchronized private void currentFrameDraw(Graphics g) {
      //this is similar to an update function, but since it only concerns itself with drawing,
	   //I didn't name it update, since the solution function also updates in a sense
      g.setColor(currentColourTheme[0]);
      g.fillRect(0,0,displayWidth,displayHeight);
      g.setColor(borderColour);
            
      if (tower == null)
         return;
      
      g.fillRect((int) (displayWidth * 0.024f),(int) (displayHeight * 0.9f),(int) (displayWidth * 0.3f),(int) ( displayHeight * 0.04f ));
      g.fillRect((int) (displayWidth * 0.348f),(int) (displayHeight * 0.9f),(int) (displayWidth * 0.3f),(int) ( displayHeight * 0.04f ));
      g.fillRect((int) (displayWidth * 0.666f),(int) (displayHeight * 0.9f),(int) (displayWidth * 0.3f),(int) ( displayHeight * 0.04f ));
      
      g.setColor(currentColourTheme[1]);
      for (int t = 0; t < 3; t++) {
         for (int i = 0; i < towerHeight[t]; i++) {
            int disk = tower[t][i];
            g.fillRoundRect((int) (displayWidth * 0.16f)+(int)(displayWidth * 0.325f)*t - (int) (displayWidth * 0.011f)*disk - (int) (displayWidth * 0.011f),
            				(int) (displayHeight * 0.8f)-(int) (displayHeight * 0.083f)*i, 
            				(int) (displayWidth * 0.024f)*disk+(int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f), 
            				(int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f));
         }
      }
      if (moveDisk > 0) {
         g.setColor(currentColourTheme[2]);
         g.fillRoundRect((int) (displayWidth * 0.3f) + (int) (displayWidth * 0.325f) * moveTower - (int) (displayWidth * 0.011f)*moveDisk - (int) (displayWidth * 0.011f), 
		        		 (int) (displayHeight * 0.8f)-(int) (displayHeight * 0.083f)*towerHeight[moveTower],
		        		 (int) (displayWidth * 0.024f)*moveDisk+(int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f),
		        		 (int) (displayWidth * 0.024f), (int) (displayHeight * 0.071f));
      }

   }



}

/*
 * although the displayWidth and displayHeight can make it difficult to properly see how things work,
 * but the values are just percentages of the actual display,
 * I did start off with set values and then altered it in order to make sure the application works on different screens/devices
 */