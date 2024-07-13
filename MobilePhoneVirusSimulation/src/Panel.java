/**
*
* @author diya
*/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel implements KeyListener, ComponentListener {

    Phone phone = new Phone();
    Image healthy;
    Image repair;
    Image infected; 
    private Graphics repairShop;
    private ArrayList<Phone> phonesList = new ArrayList<>();
    private Timer repairTime;
    private boolean repairShopFull = false;
    public int infectionRange = 20;
            
    JFrame frame;

    public Panel(JFrame frame) {
        this.frame = frame;
        phone.setRange(frame.getWidth(), frame.getHeight());
        this.addKeyListener(this);
        this.addComponentListener(this);
        this.setFocusable(true);

        // getting images
        healthy = new ImageIcon("phones/healthy.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        repair = new ImageIcon("phones/repair.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        infected = new ImageIcon("phones/infected.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT);

        // creating a timer for all phones
        Timer timer = new Timer(1, t -> {
            for (Phone phones : phonesList) {
                phones.move();
            }
            repaint();
            });
        timer.start();

        // creating a timer for time in repair shop (10 seconds)
        repairTime = new Timer(10000, t -> {
        if (repairShopFull == true) {
            for (Phone currentPhone : phonesList) {
                if (currentPhone.moveToRepair) {
                    System.out.println(currentPhone + " --- fixed!!");
                    currentPhone.moveToRepair = false;
                    currentPhone.setIsInfected(false);
                    break;
                }
            }
            repairShopFull = false;
        }
        
        // getting infected phone and moving to repair shop
        for (Phone currentPhone : phonesList) {
            if (currentPhone.isInfected && repairShopFull == false) {
                currentPhone.moveToRepair = true;
                repairShopFull = true;
                repairTime.restart();
                break;
            }
        }
        repaint();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        RepairShop repairShop = new RepairShop();
        boolean phoneAtRepair = false;
        repairShop.draw(g);
        boolean firstInfectedPhone = false;
       
        Iterator <Phone> iterator = phonesList.iterator();
            while (iterator.hasNext()) {
            Phone currentPhone = iterator.next();
            
            // setting image for phone in repair shop 
            if (currentPhone.toRepair()) {
                phoneAtRepair = true;
                g.drawImage(repair, 70, 100, this);
            } 
            // setting image for phone infected also infecting other phones within 20 pixels range
            else if (currentPhone.isInfected()) {
                g.drawImage(infected, currentPhone.x, currentPhone.y, this);
                for (Phone otherPhone : phonesList) {
                    if (!otherPhone.isInfected() && currentPhone.virusDistance(otherPhone)) { 
                        otherPhone.setIsInfected(true);
                        otherPhone.startTimer();
                        System.out.println("Phone: " + otherPhone + " infected at position (" + otherPhone.x + "," + otherPhone.y + ")");
                        repaint();
                    }
                }
            }
            // setting image for healthy phone
            else {
                g.drawImage(healthy, currentPhone.x, currentPhone.y, this);
            }
            
            // timer for infected phone in repair shop 
            if (currentPhone.isInfected() && currentPhone.toRepair()) {
                g.drawString("Life: "+ currentPhone.getCountdown(), 70, 100);
            }
            // timer for infected phone
            else if (currentPhone.isInfected()) { 
                g.drawString("Life: " + currentPhone.getCountdown(), currentPhone.x, currentPhone.y);
                repaint();
            }
            
            // removing phone
            while (iterator.hasNext()) {
                if (currentPhone.isInfected() && currentPhone.timerDone()) {
                    System.out.println("BEFORE: " + phonesList);
                    iterator.remove();
                    System.out.println(currentPhone + " ---- Phone removed"); 
                    System.out.println("AFTER: " + phonesList);
                    repaint();
                    break;
                    }
                break;
                }
            }
        phone.run();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        // infecting phone if 'v' is pressed
        if (ke.getKeyCode() == KeyEvent.VK_V) {
            // infecting random phone
            if (!phonesList.isEmpty()) {
                int randomI = (int) (Math.random() * phonesList.size());    
                Phone randomPhone = phonesList.get(randomI);
                randomPhone.setIsInfected(true);
                randomPhone.startTimer();
                repaint();
                
                // moving phone to repair shop
                for (Phone phones : phonesList) {
                    if (phones.isInfected && !phones.moveToRepair && !repairShopFull) {
                        phones.moveToRepair = true;
                        System.out.println(phones + "Phone moved for repair"); 
                        repairShopFull = true;
                        repairTime.restart();
                        break;
                        }
                    }
            }
            repaint();
        
        // adding phones if up key is pressed
        } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
            
            // setting random coordinates for phone
            int randomX = (int) (Math.random() * frame.getWidth());
            int randomY = (int) (Math.random() * frame.getHeight());
                
            System.out.println("Adding new Phone object:");
            System.out.println("Before adding: phonesList size = " + phonesList.size());
            
            // new phone code
            Phone addNewPhone = new Phone();
            addNewPhone.setRange(frame.getWidth(), frame.getHeight());
            addNewPhone.x = randomX;
            addNewPhone.y = randomY;
   
            // synchronising phonesList
            synchronized (phonesList) {
                phonesList.add(addNewPhone);
            }
            
            Thread thread = new Thread(addNewPhone);
            thread.start();

            System.out.println("After adding: phonesList size = " + phonesList.size());
            System.out.println("New Phone coordinates: x = " + randomX + ", y = " + randomY);
            
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void componentResized(ComponentEvent ce) {
        phone.setRange(frame.getWidth(), frame.getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent ce) {

    }

    @Override
    public void componentShown(ComponentEvent ce) {

    }

    @Override
    public void componentHidden(ComponentEvent ce) {

    }
}
