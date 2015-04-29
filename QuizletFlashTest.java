import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.nio.charset.Charset;
import java.awt.Color;

public class QuizletFlashTest{

    static JFrame frame;
    static JPanel mainPanel;
    static JPanel buttonPanel;
    static CardLayout cl = new CardLayout();
    static JPanel test = new JPanel();

    public static void main(String[] args){

       frame = new JFrame("Quizlet");

       mainPanel = new JPanel();
       QuizletPanel buttonPanel = new QuizletPanel(frame);

       test.add(new JButton("test"));

       mainPanel.setLayout(cl);
       mainPanel.add(buttonPanel, "1");
       mainPanel.add(test, "2");
       cl.show(mainPanel, "1");

       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       //frame.setLayout(new GridLayout(1, 1));

       frame.setSize(1000, 800);
       frame.setBackground(Color.WHITE);
       frame.getContentPane().add(mainPanel);

       frame.setVisible(true);
    }
    public static class QuizletPanel extends JPanel{
        JButton switchButton;
        JLabel search;
        private BufferedImage image;
        JTextField userAnswerField1;
        JPanel searchResultsPanel = new JPanel();
        JLabel verdict;
        JFrame frame;
        JLabel imageLabel = new JLabel();

        

        public QuizletPanel(JFrame theFrame){
          frame = theFrame;
          
        addHomePage();    

      }
      public void addHomePage(){

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
          switchButton.addActionListener(new getQuizletJSON());
          searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.PAGE_AXIS));
          JScrollPane searchScrollPane = new JScrollPane(searchResultsPanel);
          searchScrollPane.setBorder(BorderFactory.createEmptyBorder());
          add(searchScrollPane);
        ImageIcon ii = new ImageIcon(this.getClass().getResource("./ajax-loader.gif"));
            imageLabel.setIcon(ii);
              imageLabel.setAlignmentX(CENTER_ALIGNMENT);
          searchResultsPanel.add(imageLabel, java.awt.BorderLayout.CENTER);
      }
      public void addHome(){
        try {
          image = ImageIO.read(new File("./ajax-loader.gif"));
          JLabel piclabel = new JLabel(new ImageIcon(image));
          piclabel.setAlignmentX(CENTER_ALIGNMENT);
          frame.getContentPane().add(piclabel);
        } catch (IOException ex){
        }

          search = new JLabel("Search: ");
          search.setAlignmentX(CENTER_ALIGNMENT);
          frame.getContentPane().add(search);
          
          userAnswerField1 = new JTextField("                    ");
          userAnswerField1.setMaximumSize(userAnswerField1.getPreferredSize());
          frame.getContentPane().add(userAnswerField1);
          
          switchButton = new JButton("Go!");
          switchButton.setAlignmentX(CENTER_ALIGNMENT);
          frame.getContentPane().add(switchButton);
          switchButton.addActionListener(new getQuizletJSON());
          searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.PAGE_AXIS));
          frame.getContentPane().add(searchResultsPanel);
      }
      public void removeHomePage(){
        System.out.println("Test");
        Container pane = frame.getContentPane();

        pane.remove(QuizletPanel.this);
        pane.revalidate();
        pane.repaint();
        revalidate();
        repaint();
      }

      private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }

      public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
          BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
          String jsonText = readAll(rd);
          JSONObject json = new JSONObject(jsonText);
          return json;
        } finally {
          is.close();
        }
      }

        class SetUpButtonActionListener implements ActionListener{

            public void actionPerformed(ActionEvent ae) {
                userAnswerField1.setText("          ");
                
                verdict.setText(" ");
                
            }
        }
        class getQuizletJSON implements ActionListener{

            public void actionPerformed(ActionEvent ae) {
              searchResultsPanel.removeAll();
              try {
              JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/search/sets?q="+userAnswerField1.getText()+"&client_id=QbgwbRMGAU&whitespace=1");


              //System.out.println(json.getJSONArray("sets").getJSONObject(0));
              JSONArray setArray = json.getJSONArray("sets");
              for (int i = 0;i<=30; i++) {
                JSONObject firstResult = setArray.getJSONObject(i);
                String title = firstResult.get("title").toString();

                JSONResult resultLabel  = new JSONResult(title);
                resultLabel.setAlignmentX(CENTER_ALIGNMENT);
                resultLabel.terms = firstResult.get("term_count").toString();
                resultLabel.id = firstResult.get("id").toString();
                searchResultsPanel.add(resultLabel);
                searchResultsPanel.revalidate();
                searchResultsPanel.repaint();
              }

              frame.revalidate();
              frame.repaint();

              } catch (IOException ex){
              } catch (JSONException ex){
              } 
            }
        }

    public class JSONResult extends JLabel implements MouseListener{
      public String id;
      public String terms;
     
      public JSONResult(String name){
       super(name);
       this.setOpaque(true);
       this.addMouseListener(this);
     } 

     public void mouseClicked(MouseEvent e){
       Font goBack = new Font("Arial", Font.BOLD, 18);

       JPanel whatever = new JPanel();
       whatever.setLayout(new GridLayout(5,5));
       mainPanel.add(new JScrollPane(whatever), "test");
       JButton testButton = new JButton("Go back");
       testButton.setFont(goBack);
       testButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae) {
           cl.show(mainPanel, "1");
         }
       });

       whatever.add(testButton);

       try {
         JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/sets/"+this.id+"?client_id=QbgwbRMGAU&whitespace=1&total_results=40");


        //System.out.println(json.getJSONArray("sets").getJSONObject(0));
        JSONArray flashTerms = json.getJSONArray("terms");
        for (int i = 0;i<=flashTerms.length(); i++) {
          JSONObject firstResult = flashTerms.getJSONObject(i);
          String title = firstResult.get("term").toString();

          JButton resultLabel  = new JButton(title);
          resultLabel.setAlignmentX(CENTER_ALIGNMENT);
          whatever.add(resultLabel);
          whatever.revalidate();
          whatever.repaint();
        }
       } catch (IOException ex){
       } catch (JSONException ex){
       }

       cl.show(mainPanel,"test");

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

    }
}    

