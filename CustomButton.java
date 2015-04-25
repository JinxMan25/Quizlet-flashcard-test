import javax.swing.JButton;

public class CustomButton extends JButton {
  public int value = 0;
  public CustomButton(String name){
    this(new JButton(name));
  }
}
