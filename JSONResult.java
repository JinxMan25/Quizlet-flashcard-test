import javax.swing.JLabel;
import java.awt.event.*;
import java.awt.Color;

public class JSONResult extends JLabel implements MouseListener {
  public String id;
  public String terms;
 
 public JSONResult(String name){
   super(name);
   this.setOpaque(true);
   this.addMouseListener(this);
 } 
 public void mouseClicked(MouseEvent e) {

       System.out.println(this.id);
   }

 public void mousePressed(MouseEvent e) {
       }

 public void mouseReleased(MouseEvent e) {
       }

 public void mouseEntered(MouseEvent e) {
   this.setBackground(Color.BLACK);
   this.setForeground(Color.WHITE);
       }

 public void mouseExited(MouseEvent e) {
   this.setBackground(Color.white);
   this.setForeground(Color.black);
       }
  }
