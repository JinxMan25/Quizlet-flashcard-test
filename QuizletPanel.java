import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zoj5041
 */
public class QuizletPanel extends JPanel{
    JButton setUpButton, checkMeButton, switchButton;
    JLabel search;
    private BufferedImage image;
    boolean isGetByCapital = true;
    JTextField userAnswerField1;
    JLabel verdict;
    

    public QuizletPanel(){
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      try {
        image = ImageIO.read(new File("Quizlet_logo.png"));
        JLabel piclabel = new JLabel(new ImageIcon(image));
        piclabel.setAlignmentX(CENTER_ALIGNMENT);
        add(piclabel);
      } catch (IOException ex){
      }

      search = new JLabel("Search: ");
      search.setAlignmentX(CENTER_ALIGNMENT);
      add(search);
      
      userAnswerField1 = new JTextField("                    ");
      userAnswerField1.setMaximumSize(userAnswerField1.getPreferredSize());
      add(userAnswerField1);
      
      switchButton = new JButton("Go!");
      switchButton.setAlignmentX(CENTER_ALIGNMENT);
      add(switchButton);
      switchButton.addActionListener(new SwitchButtonListener());
      
        
    }

    class SetUpButtonActionListener implements ActionListener{

        public void actionPerformed(ActionEvent ae) {
            userAnswerField1.setText("          ");
            
            verdict.setText(" ");
            
        }
    }
    class SwitchButtonListener implements ActionListener{

        public void actionPerformed(ActionEvent ae) {
          if (isGetByCapital) {
            switchButton.setText("Get answer by Capital");
            isGetByCapital = false;
          } else {
            switchButton.setText("Get answer by State ");
            isGetByCapital = true;
          }
            
        }
    }
}

