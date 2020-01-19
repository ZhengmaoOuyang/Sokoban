import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SokobanGame extends JFrame {
        
        //Declare variables
        int hFrameSize = 1200;
        int vFrameSize = 730;
        int hOffset;
        int vOffset;
        int tileSideLength;
        
        int[][] templevel = new int[][]{
        	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        	  { 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
        	  { 1, 0, 0, 4, 0, 2, 0, 2, 0, 0, 2, 0, 1 },
        	  { 1, 0, 3, 4, 2, 0, 0, 0, 4, 0 ,0, 0, 1 },
        	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        	};
        	
        int hTiles = templevel[0].length;
        int vTiles = templevel.length;
        
        Player player;
        
        ArrayList<Walls> wallList = new ArrayList<Walls>();
        ArrayList<Boxes> boxList = new ArrayList<Boxes>();
        ArrayList<GoalArea> goalAreaList = new ArrayList<GoalArea>();

        public static void main(String[] args) {
                //Start running the game
                SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                                SokobanGame ouyangSoko = new SokobanGame();
                        }
                });
        }
        
        public SokobanGame() {

            JFrame frame1 = new JFrame();        
            frame1.setSize(hFrameSize, vFrameSize);   
            frame1.add(new drawSokoban());
            frame1.setTitle("Sokoban");
            frame1.setResizable(false);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setVisible(true);
                   
        }
            
        class drawSokoban extends JPanel {
        	
            BufferedImage bgImg;
    	    int numBoxesScored;
    	    boolean win = false;
    	    SecTimer tmr;
    	    Font font;
    	    int numMoves;
    	    
        	public drawSokoban() {
        		
        		addKeyListener(new KeyListener());
        		setFocusable(true);      		
                tileSideLength = Math.round(Math.min((vFrameSize/(vTiles+2)), (hFrameSize)/(hTiles+2)));
                        
                if(tileSideLength == vFrameSize/(vTiles + 2)) {
                	hOffset = (int)(hFrameSize/2.0 - (hTiles/2.0)*tileSideLength);
                	vOffset = tileSideLength;
                } else {
                	vOffset = (int)(vFrameSize/2.0 - (vTiles/2.0)*tileSideLength);
                	hOffset = tileSideLength;
                }
                
                setWorld();
                
        	}
        	
        	public void setWorld() {
        		
        		for(int i = 0; i < hTiles; i++) {
                	for(int j = 0; j < vTiles; j++) {
                		
                		if(templevel[j][i] == 1) {
                			Walls wall = new Walls(i, j, tileSideLength, hOffset, vOffset);
                			wallList.add(wall);
                		} else if(templevel[j][i] == 2) {
                			Boxes box = new Boxes(i, j, tileSideLength, hOffset, vOffset);
                			boxList.add(box);
                		} else if(templevel[j][i] == 3) {
                			player = new Player(i, j, tileSideLength, hOffset, vOffset);
                		} else if(templevel[j][i] == 4) {
                			GoalArea goal = new GoalArea(i, j, tileSideLength, hOffset, vOffset);
                			goalAreaList.add(goal);
                		}
                	}
                }
                
        		try {
        			bgImg = ImageIO.read(getClass().getResourceAsStream("/bg.jpg"));
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		
        		numMoves = 0;
        		font = new Font("Comic Sans MS", Font.PLAIN, 24);
        		tmr = new SecTimer();
        		tmr.begin();

        	}
        	
        	public void drawWorld(Graphics g) {
        		
        		g.drawImage(bgImg, 0, 0, null);
            	g.setFont(font);
                for(int a = 0; a < goalAreaList.size(); a++) {
                	GoalArea goal = goalAreaList.get(a);
                	g.drawImage(goal.getBufferedImage(), goal.getXPixel(), goal.getYPixel(), null);
                }
                
                for(int k = 0; k < wallList.size(); k++) {
                	Walls wall = wallList.get(k);
                	g.drawImage(wall.getBufferedImage(), wall.getXPixel(), wall.getYPixel(), null);
                }
                
                for(int l = 0; l < boxList.size(); l++) {
                	Boxes box = boxList.get(l);
                	g.drawImage(box.getBufferedImage(), box.getXPixel(), box.getYPixel(), null);
                }
                    
                g.drawImage(player.getBufferedImage(), player.getXPixel(), player.getYPixel(), null); 
                g.drawString("Time: " + tmr.getSecs(), 10, 30);
                g.drawString("Moves: " + numMoves, 120, 30);
                
                if(ifWon()) {

                	g.setColor(Color.RED);
                	tmr.stop();
                	g.drawString("YOU WON WOW COOL GOOD", 500, 500);
                }
                
                repaint();
                
        	}
            
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);               
                drawWorld(g);
            }   
            
            public boolean ifWon() {
            	
            	numBoxesScored = 0;
            	for(int q = 0; q < goalAreaList.size(); q++) {
        			GoalArea goal = goalAreaList.get(q);
            		for(int r = 0; r < boxList.size(); r++) {
            			Boxes box = boxList.get(r);
            			if(goal.getXPixel() == box.getXPixel() && box.getYPixel() == goal.getYPixel()) {
            				numBoxesScored++;
            				
            			}
            		}
            	} 
            	
            	if(numBoxesScored == boxList.size())
            		win = true;
            	
            	return win;
            	
            }
            
            public void restart() {
            	
            	wallList.clear();
            	boxList.clear();
            	goalAreaList.clear();
            	win = false;
              	setWorld();  
            	
            }
            class KeyListener extends KeyAdapter {
            	
            	@Override
            	public void keyPressed(KeyEvent e) {
            		
            		int keyCode = e.getKeyCode();
            		
            		if(keyCode == KeyEvent.VK_R) {
            			restart();
            		}
            		
            		if(ifWon())
            			return;
            		
            		if(keyCode == KeyEvent.VK_RIGHT) {
            			
            			for(int m = 0; m < wallList.size(); m++) {
            				if(wallList.get(m).getXPixel() - tileSideLength == player.getXPixel() && wallList.get(m).getYPixel() == player.getYPixel()) {
            					return;
            				}
            			}
            			
            			for(int n = 0; n < boxList.size(); n++) {
            				Boxes touchingBox  = boxList.get(n); 
            				if(touchingBox.getXPixel() - tileSideLength == player.getXPixel() && touchingBox.getYPixel() == player.getYPixel()) {
            					
            					for(int o = 0; o < boxList.size(); o++) {
            						Boxes twoOverBox = boxList.get(o);
            						if(twoOverBox.getXPixel() - tileSideLength == touchingBox.getXPixel() && twoOverBox.getYPixel() == touchingBox.getYPixel()) {
            							return;
            						}
            					}
            					
            					for(int p = 0; p < wallList.size(); p++) {
            						Walls twoOverWall = wallList.get(p);
            						if(twoOverWall.getXPixel() - tileSideLength == touchingBox.getXPixel() && twoOverWall.getYPixel() == touchingBox.getYPixel()) {
            							return;
            						}
            					}
            					touchingBox.setXPixel(touchingBox.getXPixel() + tileSideLength);
            				}
            			}
            			player.setXPixel(player.getXPixel() + tileSideLength);
            			revalidate();
            			
            		} else if(keyCode == KeyEvent.VK_LEFT) {
            			
            			for(int m = 0; m < wallList.size(); m++) {
            				if(wallList.get(m).getXPixel() + tileSideLength == player.getXPixel() && wallList.get(m).getYPixel() == player.getYPixel()) {
            					return;
            				}
            			}
            			
            			for(int n = 0; n < boxList.size(); n++) {
            				Boxes touchingBox  = boxList.get(n); 
            				if(touchingBox.getXPixel() + tileSideLength == player.getXPixel() && touchingBox.getYPixel() == player.getYPixel()) {
            					
            					for(int o = 0; o < boxList.size(); o++) {
            						Boxes twoOverBox = boxList.get(o);
            						if(twoOverBox.getXPixel() + tileSideLength == touchingBox.getXPixel() && twoOverBox.getYPixel() == touchingBox.getYPixel()) {
            							return;
            						}
            					}
            					
            					for(int p = 0; p < wallList.size(); p++) {
            						Walls twoOverWall = wallList.get(p);
            						if(twoOverWall.getXPixel() + tileSideLength == touchingBox.getXPixel() && twoOverWall.getYPixel() == touchingBox.getYPixel()) {
            							return;
            						}
            					}
            					touchingBox.setXPixel(touchingBox.getXPixel() - tileSideLength);
            				}
            			}
            			
            			player.setXPixel(player.getXPixel() - tileSideLength);
            			revalidate();
            			
            		} else if(keyCode == KeyEvent.VK_UP) {
            			
            			for(int m = 0; m < wallList.size(); m++) {
            				if(wallList.get(m).getYPixel() + tileSideLength == player.getYPixel() && wallList.get(m).getXPixel() == player.getXPixel()) {
            					return;
            				}
            			}
            			
            			for(int n = 0; n < boxList.size(); n++) {
            				Boxes touchingBox  = boxList.get(n); 
            				if(touchingBox.getYPixel() + tileSideLength == player.getYPixel() && touchingBox.getXPixel() == player.getXPixel()) {
            					
            					for(int o = 0; o < boxList.size(); o++) {
            						Boxes twoOverBox = boxList.get(o);
            						if(twoOverBox.getYPixel() + tileSideLength == touchingBox.getYPixel() && twoOverBox.getXPixel() == touchingBox.getXPixel()) {
            							return;
            						}
            					}
            					
            					for(int p = 0; p < wallList.size(); p++) {
            						Walls twoOverWall = wallList.get(p);
            						if(twoOverWall.getYPixel() + tileSideLength == touchingBox.getYPixel() && twoOverWall.getXPixel() == touchingBox.getXPixel()) {
            							return;
            						}
            					}
            					touchingBox.setYPixel(touchingBox.getYPixel() - tileSideLength);
            				}
            			}
            			
            			player.setYPixel(player.getYPixel() - tileSideLength);
            			revalidate();
            			
            		} else if(keyCode == KeyEvent.VK_DOWN) {	
            			
            			for(int m = 0; m < wallList.size(); m++) {
            				if(wallList.get(m).getYPixel() - tileSideLength == player.getYPixel() && wallList.get(m).getXPixel() == player.getXPixel()) {
            					return;
            				}
            			}	
            			
            			for(int n = 0; n < boxList.size(); n++) {
            				Boxes touchingBox  = boxList.get(n); 
            				if(touchingBox.getYPixel() - tileSideLength == player.getYPixel() && touchingBox.getXPixel() == player.getXPixel()) {
            					
            					for(int o = 0; o < boxList.size(); o++) {
            						Boxes twoOverBox = boxList.get(o);
            						if(twoOverBox.getYPixel() - tileSideLength == touchingBox.getYPixel() && twoOverBox.getXPixel() == touchingBox.getXPixel()) {
            							return;
            						}
            					}
            					
            					for(int p = 0; p < wallList.size(); p++) {
            						Walls twoOverWall = wallList.get(p);
            						if(twoOverWall.getYPixel() - tileSideLength == touchingBox.getYPixel() && twoOverWall.getXPixel() == touchingBox.getXPixel()) {
            							return;
            						}
            					}
            					touchingBox.setYPixel(touchingBox.getYPixel() + tileSideLength);
            				}
            			}
            			
            			player.setYPixel(player.getYPixel() + tileSideLength);
            			
            		}      	
            		numMoves++;
            	}
            }
        }       
}
