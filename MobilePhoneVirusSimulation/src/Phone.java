/**
 *
 * @author diya
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Phone extends Thread implements Comparable<Phone> {
  int x = 0; 
  int y = 0;  
  int vx = 0; 
  int vy = 0;  
  int delay = 10;  
  int width;  
  int height;  
  
  boolean isInfected;
  boolean moveToRepair = false; 
  int infectionRange = 20;
  Timer infectedTimer;
  int infectedTime = 500;
  boolean timeStart = false;
  int countdown;
  int time = 0;
  
  RepairShop repairShop;
  
  public Phone() {

    x = 0;
    y = 0;
    vx = 1;
    vy = 1;
    isInfected = false;
    infectedTimer = new Timer();
  }
  
  public void setRange(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  @Override
  public void run() {  
    move();
  }
   
  public void move() {
    if (x > width || x < 0)
      vx *= -1; 
    if (y > height || y < 0)
      vy *= -1; 
    
    try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    x += vx;
    y += vy;
  }  

    @Override
    public int compareTo(Phone otherPhones) {
      if (this.isInfected && !otherPhones.isInfected) {
          return -1;
      }
      else if (!this.isInfected && otherPhones.isInfected) {
          return 1;
      }
      return 0;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setIsInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }
    
    // synchronizing moveForRepair 
    public synchronized void moveForRepair(int repairX, int repairY) {
        this.x = repairX;
        this.y = repairY;
        moveToRepair = true; 
    }
    
    public boolean toRepair() {
        return moveToRepair;
    }
    
    // getting distance between phones 
    public boolean virusDistance(Phone phOther) {
        int distanceX = Math.abs(this.x - phOther.x);
        int distanceY = Math.abs(this.y - phOther.y);
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        return distance <= infectionRange;
    }
    
    // synchronizing moveForRepair 
    public synchronized void startTimer() {
        if (timeStart) {
        return;
        }
        time = infectedTime;
        System.out.println("Timer started at " + getCountdown() + " seconds.");
        
        infectedTimer = new Timer();
        infectedTimer.scheduleAtFixedRate(new TimerTask() {
    
        @Override
        public void run() {
            if (time <= 0) { 
                setIsInfected(false);
                infectedTimer.cancel();
                infectedTimer.purge();
                }
            else {
                countdown = time;
                time--;
                }
            //timeStart = true;
            }
        }, 0, 1000);
    }
    
    public int getCountdown() {
        return time;
    }
    
    // checking if timer is done
    public boolean timerDone() {
        return time <= 0;
    }
}
