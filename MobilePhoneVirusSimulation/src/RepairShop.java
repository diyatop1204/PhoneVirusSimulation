/**
*
* @author diya
*/

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class RepairShop {
	public void draw(Graphics g) {
        // creating repair shop
        g.setColor(Color.pink);
        g.fillRect(35, 40, 140, 160);
        
        g.setColor(new Color(137, 217, 113)); // pretty green
        g.fillRect(35, 40, 140, 30);
        
        g.setColor(Color.black);
        Font font = new Font("Calibri", Font.BOLD, 14);
        g.setFont(font);
        g.drawString("REPAIR SHOP", 55, 60);
        
        // small box of instructions
        g.setColor(new Color(137, 217, 113)); // pretty green
        g.fillRect(600, 40, 200, 80);
        g.setColor(Color.pink);
        g.fillRect(600, 40, 200, 30);
        
        g.setColor(Color.black);
        g.drawString("INSTRUCTIONS", 603, 60);
        g.drawString("Press '↑' to add Phones.", 603, 90);
        g.drawString("Press 'V' to infect a Phone.", 603, 110);
    }
}