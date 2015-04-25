
import java.awt.Color;
import java.awt.CardLayout;
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
    static JPanel mainPanel;
    static JPanel buttonPanel;

    public static void main(String[] args){

       frame = new JFrame("Quizlet");

       CardLayout cl = new CardLayout();
       mainPanel = new JPanel();
       buttonPanel = new QuizletPanel();

       mainPanel.setLayout(cl);
       mainPanel.add(buttonPanel, "1");
       cl.show(mainPanel, "1");

       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       //frame.setLayout(new GridLayout(1, 1));

       frame.setSize(1000, 500);
       frame.setBackground(Color.WHITE);
       frame.add(mainPanel);

       frame.setVisible(true);
    }
}    

