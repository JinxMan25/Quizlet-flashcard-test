
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zoj5041
 */
public class QuizletFlashTest{

    static JFrame frame;
    static JPanel buttonPanel;

    public static void main(String[] args){

       frame = new JFrame("State-Capital Matcher");
       frame.setSize(1000, 1000);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setBackground(Color.WHITE);

       frame.setLayout(new GridLayout(1, 1));

       buttonPanel = new QuizletPanel();
       frame.add(buttonPanel);

       frame.setVisible(true);
    }
}    

