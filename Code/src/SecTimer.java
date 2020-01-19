import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class SecTimer {
	
    int seconds = 0;
    Timer timer;
    
	public SecTimer() {
		
	}
	
	public void begin() {
	  timer = new Timer(1000, new ChangeTime());
	  timer.start();
	}
	
	public void stop() {
		timer.stop();
	}
	
	public int getSecs() {
		return seconds;
	}
	
	class ChangeTime implements ActionListener {

	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	seconds++;
	    }
	}
}