import java.awt.event.*;
import java.awt.Graphics;
import java.io.*;
import java.awt.Image;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JComponent;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
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
    public static Boolean checkedTerm = false;
    static JPanel mainPanel;
    static public double distance;
    static JPanel buttonPanel;
    static CardLayout cl = new CardLayout();
    static JPanel test = new JPanel();
    static ExtendingClass testPanel = new ExtendingClass();
 
    public static void main(String[] args){
 
       frame = new JFrame("Quizlet");
 
       mainPanel = new JPanel();
       QuizletPanel buttonPanel = new QuizletPanel(frame);
 
       test.add(new JButton("test"));
 
       mainPanel.setLayout(cl);
 
       mainPanel.add(buttonPanel, "1");
       mainPanel.add(test, "2");
       mainPanel.add(testPanel, "testPanel");
 
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
              getResults();
            }
            public void getResults(){
              SwingWorker<Boolean,String> worker = new SwingWorker<Boolean,String>(){
                protected Boolean doInBackground() throws Exception {
                  try {
                  publish("Test");
                  JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/search/sets?q="+userAnswerField1.getText()+"&client_id=QbgwbRMGAU&whitespace=1");
 
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
                  return true;
 
                  } catch (IOException ex){
                  } catch (JSONException ex){
                  } 
                  return null;
                }
              protected void process(List<String> chunks){
                System.out.println("Testing123");
                ImageIcon ii = new ImageIcon(this.getClass().getResource("./ajax-loader.gif"));
                    imageLabel.setIcon(ii);
                      imageLabel.setAlignmentX(CENTER_ALIGNMENT);
                  searchResultsPanel.add(imageLabel, java.awt.BorderLayout.CENTER);
                  searchResultsPanel.revalidate();
                  searchResultsPanel.repaint();
 
              }
              protected void done(){
            
                try {
                  Boolean status = get();
                  System.out.println("Done");
                  searchResultsPanel.remove(imageLabel);
                  searchResultsPanel.revalidate();
                  searchResultsPanel.repaint();
                  
                } catch (InterruptedException ex){
                } catch (ExecutionException x){
                }
              }
            };
            worker.execute();
 
            }
        }
 
    public class JSONResult extends JLabel implements MouseListener{
      public String id;
      public JSONArray flashTerms;
      public JPanel whatever = new JPanel();
      public String terms;
      public Font goBack = new Font("Arial", Font.BOLD, 15);
     
      public JSONResult(String name){
       super(name);
       this.setOpaque(true);
       this.addMouseListener(this);
     } 
      public String getMultiLine(String def){
        int counter = 0;
        String[] words = def.split("\\s+");
        System.out.println(words);
        String threeWords = "<html>";
        for (int i = 0; i < words.length; i++) {
        if (counter == 5){
          threeWords = threeWords + " " + words[i] + "<br>";
          counter = 0;
        } else {
        threeWords = threeWords + " " + words[i];
        }
        counter++;
        }
        return threeWords+"</html>";
      }
      public void getSearchResults(){
        final String theID = this.id;
        SwingWorker<Boolean,Integer> worker = new SwingWorker<Boolean,Integer>(){
          protected Boolean doInBackground() throws Exception {
             try {
               publish(12);
               JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/sets/"+theID+"?client_id=QbgwbRMGAU&whitespace=1&total_results=40");
 
              //System.out.println(json.getJSONArray("sets").getJSONObject(0));
              flashTerms = json.getJSONArray("terms");
              for (int i = 0;i<=flashTerms.length(); i++) {
                JSONObject firstResult = flashTerms.getJSONObject(i);
                String title = firstResult.get("term").toString();
                String definition = firstResult.get("definition").toString();
                definition = getMultiLine(definition);
 
                termsButton resultLabel  = new termsButton(title, definition);
                resultLabel.setFont(goBack);
                //resultLabel.setHorizontalAlignment(SwingConstants.LEFT);
                //resultLabel.setVerticalAlignment(SwingConstants.TOP);
                resultLabel.setAlignmentX(CENTER_ALIGNMENT);
                whatever.add(resultLabel);
                whatever.revalidate();
                whatever.repaint();
              }
              return true;
             } catch (IOException ex){
             } catch (JSONException ex){
               }
            return null;
          }
        protected void process(List<Integer> chunks){
          Integer number = chunks.get(chunks.size()-1);
          System.out.println(number);
        }
        protected void done(){
      
          try {
            Boolean status = get();
            System.out.println("DONE!");
          } catch (InterruptedException ex){
          } catch (ExecutionException x){
          }
        }
      };
      worker.execute();
 
      }
 
     public void mouseClicked(MouseEvent e){
 
       whatever.setLayout(new GridLayout(4,2));
       mainPanel.add(new JScrollPane(whatever), "test");
       ImageIcon goBackImage = new ImageIcon(this.getClass().getResource("back.png"));
 
       Image img = goBackImage.getImage();
       Image newImg = img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH);
       goBackImage = new ImageIcon(newImg);
 
       JButton flashTestButton = new JButton("Test yourself b4 u rek yoself");
       flashTestButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
           testPanel.setTerms(flashTerms);
           cl.show(mainPanel, "testPanel");
          
         }
       });
       flashTestButton.setFont(new Font("Arial", Font.BOLD, 12));
 
       JButton testButton = new JButton(goBackImage);
 
       testButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae) {
           cl.show(mainPanel, "1");
         }
       });
 
       whatever.add(testButton);
       whatever.add(flashTestButton);
      getSearchResults();
 
 
       cl.show(mainPanel,"test");
 
     }
 
     public void mousePressed(MouseEvent e) {
     }
 
     public void mouseReleased(MouseEvent e) {
     }
 
     public void mouseEntered(MouseEvent e) {
 
       this.setBackground(new Color(235,248,255));
 
     }
 
     public void mouseExited(MouseEvent e) {
       this.setBackground(new Color(238,238,238));
       this.setForeground(Color.black);
     }
    }
 
    }
    public static class termsButton extends JButton implements ActionListener, MouseListener{
      String term, definition;
      Boolean showingTerm = true;
 
      public termsButton(String term, String definition){
        super(term);
        this.term = term;
        this.definition = definition;
        this.addActionListener(this);
        this.addMouseListener(this);
      }
 
      public void actionPerformed(ActionEvent ae){
        System.out.println("Clicked");
        if (showingTerm){
          this.setFont(new Font("Arial",Font.PLAIN,12 ));
          this.setText(this.definition);
          showingTerm = false;
        } else {
          this.setFont(new Font("Arial", Font.BOLD, 15));
          this.setText(this.term);
          showingTerm = true;
        }
      }
       public void mouseEntered(MouseEvent e) {
         if (showingTerm){
           this.setText("<html>"+this.term+"<br>"+"Click to flip to definition</html>");
         } else {
           this.setText(this.definition.substring(0, definition.length()-7)+"<br>"+"Click to flip to term</html>");
         }
 
       }
 
       public void mouseClicked(MouseEvent e) {
 
       }
       public void mouseExited(MouseEvent e) {
         if (showingTerm){
           this.setText(this.term);
         } else {
           this.setText(this.definition);
         }
 
       }
       public void mousePressed(MouseEvent e) {
       }
 
       public void mouseReleased(MouseEvent e) {
       }
    }
    public static class testPaint extends JComponent {
      public void paintComponent(Graphics g){
        if (checkedTerm == true){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(305,20,100,25);
        if (distance >= 90){
          g.setColor(Color.GREEN);
        g.fillRect(305,20,100,25);
        } else if (distance >= 75){
          g.setColor(new Color(216,255,174));
        g.fillRect(305,20,75,25);
        } else if (distance >= 50){
          g.setColor(Color.YELLOW);
        g.fillRect(305,20,62,25);
        } else if (distance >= 35){
          g.setColor(new Color(235,162,117));
        g.fillRect(305,20,40,25);
        } else if ( distance >= 0){
          g.setColor(Color.RED);
        g.fillRect(305,20,15,25);
        }
        }
      }
    }
  public static class ExtendingClass extends JPanel{
 
    public static JSONArray thisFlashArray;
    public static String term;
    public static String definition;
    public static ArrayList<Integer> termsList;
    public static JTextArea userInput;  
    public static JButton backButton;
    public static JLabel randomFlashCard;
    public static JPanel theBack = new JPanel();
    public static JPanel theFlow = new JPanel(new FlowLayout());
    public static JPanel theFlow2 = new JPanel(new FlowLayout());
    public static JPanel centerFlow = new JPanel();
 
    public ExtendingClass(){

    this.setLayout(new BorderLayout());
    centerFlow.setLayout(new BoxLayout(centerFlow, BoxLayout.PAGE_AXIS));
    
    ImageIcon goBackImage = new ImageIcon(this.getClass().getResource("back.png"));
 
       Image img = goBackImage.getImage();
       Image newImg = img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH);
       goBackImage = new ImageIcon(newImg);
       backButton = new JButton(goBackImage);
       JButton backButton2 = new JButton(goBackImage);

       backButton.setAlignmentX(CENTER_ALIGNMENT);
       theFlow.add(backButton);
       theFlow2.add(backButton2);
       backButton2.setVisible(false);
       this.add(theFlow, BorderLayout.WEST);
       this.add(theFlow2, BorderLayout.EAST);
 
 
  backButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
    
        cl.show(mainPanel, "test");
  
  }
      }); 
       
    }
 
    public void setTerms(JSONArray flashArray){
      thisFlashArray = flashArray;
      termsList = new ArrayList<>();
 
      Random generator =  new Random();
      for(int i = 0; i < flashArray.length(); i++){
        termsList.add(i);
      }
 
      int getRandomFlashCard = generator.nextInt(flashArray.length());
      flashArray.remove(getRandomFlashCard);
      try {
        if(randomFlashCard != null || userInput != null){
          centerFlow.removeAll();
        }


        JSONObject flashObject = flashArray.getJSONObject(getRandomFlashCard);
        term = flashObject.get("term").toString();
        definition = flashObject.get("definition").toString();
        randomFlashCard = new JLabel(term);
        randomFlashCard.setHorizontalAlignment(SwingConstants.CENTER);
        centerFlow.add(randomFlashCard);

        userInput = new JTextArea(5,20);
        userInput.setAlignmentX(CENTER_ALIGNMENT);
        centerFlow.add(userInput);
        JButton checkButton = new JButton("Check answer");
        JLabel testLabel = new JLabel("          ");
        testLabel.setAlignmentX(CENTER_ALIGNMENT);

        checkButton.setAlignmentX(CENTER_ALIGNMENT);
        checkButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){
            checkedTerm = true;

            distance = jaccard.Jaccard2(definition, userInput.getText());
            distance = (1-(distance-0.05))*100;
            repaint();
          }
        });
        centerFlow.add(checkButton);
        centerFlow.add(testLabel);
        centerFlow.add(new testPaint());

        userInput.setMaximumSize(userInput.getPreferredSize());
        userInput.setAlignmentX(CENTER_ALIGNMENT);
        randomFlashCard.setAlignmentX(CENTER_ALIGNMENT);
        

        this.add(centerFlow, BorderLayout.CENTER);
        this.revalidate();
        frame.setSize(800, 350);
      } catch(JSONException ex) {
      }
        
    }
  }
}  
