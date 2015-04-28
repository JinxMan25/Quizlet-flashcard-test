import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Graphics;
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
      add(searchResultsPanel);
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

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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

          frame.getContentPane().removeAll();
          frame.revalidate();
          frame.repaint();
            revalidate();
            repaint();

          } catch (IOException ex){
          } catch (JSONException ex){
          } 
        }
    }

}

