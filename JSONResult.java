import javax.swing.JLabel;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JFrame;

public class JSONResult extends JLabel implements MouseListener{
  public String id;
  public String terms;
  public JFrame frame;
 
  public JSONResult(String name, JFrame theFrame){
   super(name);
   frame = theFrame;
   this.setOpaque(true);
   this.addMouseListener(this);
 } 

 public void mouseClicked(MouseEvent e){

  System.out.println(this.id);
  FlashCards test = new FlashCards(frame);

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
