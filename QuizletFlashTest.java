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

/*
 * Contributers: David Tahvildaran
 *               Zohaib Javed
 *               Md Samiul Huq
 *
 * Final Project for CMPSC 221
 *
 * Professor: Dr. Tom Warms
 *
 * Due date: 05/06/2015
 *
 * 
 * */
 
public class QuizletFlashTest{
 
    static JFrame frame;
    public static Boolean checkedTerm = false;
    static JPanel mainPanel;
    static public double distance;
    static JPanel homePage;
    static CardLayout cl = new CardLayout();
    static ExtendingClass testPanel = new ExtendingClass();
 
    public static void main(String[] args){
 
       frame = new JFrame("Quizlet");
 
       mainPanel = new JPanel();
       QuizletPanel homePage = new QuizletPanel(frame);
 
       //Settings it to card layout to be able to switch between each card, or view easily
       mainPanel.setLayout(cl);
 
       //Adding the rest of the "cards" to the deck
       mainPanel.add(homePage, "1");
       mainPanel.add(testPanel, "testPanel");
 
       cl.show(mainPanel, "1");
 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
 
       frame.setSize(1000, 800);
       frame.setBackground(Color.WHITE);
       frame.getContentPane().add(mainPanel);
 
       frame.setVisible(true);
    }
    //this is a view/JPanel that shows the homePage. This view will show all the results from the search 
    public static class QuizletPanel extends JPanel{
        JButton switchButton;
        JLabel search;
        private BufferedImage image;
        JTextField userAnswerField1;
        JPanel searchResultsPanel = new JPanel();
        JLabel verdict;
        JFrame frame;
        JLabel imageLabel = new JLabel();
 
        
 
      //initializes the view with the quizlet logo and search field and button
      public QuizletPanel(JFrame theFrame){
        frame = theFrame;
          
        addHomePage();    
 
      }

      //adds components like the quizlet logo, search field and button to the main view that is the homepage
      public void addHomePage(){
 
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
        //add logo
        try {
          image = ImageIO.read(new File("Quizlet_logo.png"));
          JLabel piclabel = new JLabel(new ImageIcon(image));
          piclabel.setAlignmentX(CENTER_ALIGNMENT);
          add(piclabel);
        } catch (IOException ex){
        }
 
          //adds search field label
          search = new JLabel("Search: ");
          search.setAlignmentX(CENTER_ALIGNMENT);
          add(search);
          
          //adds search field 
          userAnswerField1 = new JTextField("                    ");
          userAnswerField1.setMaximumSize(userAnswerField1.getPreferredSize());
          add(userAnswerField1);
          
          //adds search query button
          switchButton = new JButton("Go!");
          switchButton.setAlignmentX(CENTER_ALIGNMENT);
          add(switchButton);
          switchButton.addActionListener(new getQuizletJSON());
          searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.PAGE_AXIS));

          //adds main panel to scroll pane so that user is able to scroll through results
          JScrollPane searchScrollPane = new JScrollPane(searchResultsPanel);
          searchScrollPane.setBorder(BorderFactory.createEmptyBorder());
          add(searchScrollPane);
      }
 
      private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }
 
      //Fetches JSON object from the Quizlet API
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
 
      //action listener that executes the query and returns a JSON/hash object from Quizlet api,
      //i.e returns sets related to user query and displays to the view
      class getQuizletJSON implements ActionListener {

          public void actionPerformed(ActionEvent ae) {
            searchResultsPanel.removeAll();
            getResults();
          }
          public void getResults(){
            //this is so that the GUI doesn't get frozen when this function is called. 
            SwingWorker<Boolean,String> worker = new SwingWorker<Boolean,String>(){
              protected Boolean doInBackground() throws Exception {
                try {
                publish("Test");
                //
                JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/search/sets?q="+userAnswerField1.getText()+"&client_id=QbgwbRMGAU&whitespace=1");

                //creates an array by getting the "sets" array from the JSON/hash object: json
                JSONArray setArray = json.getJSONArray("sets");
                //iterates through each set and sets a JLabel onto the view
                for (int i = 0;i<=30; i++) {
                  JSONObject firstResult = setArray.getJSONObject(i);
                  //gets title of the single set of flashcards
                  String title = firstResult.get("title").toString();

                  JSONResult resultLabel  = new JSONResult(title);
                  resultLabel.setAlignmentX(CENTER_ALIGNMENT);
                  resultLabel.terms = firstResult.get("term_count").toString();
                
                  //gets id so that program can query flashcards with id using the Quizlet API
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
            //this displays the loading indicator .gif while the application is waiting for the API request
            protected void process(List<String> chunks){
              System.out.println("Testing123");
              ImageIcon ii = new ImageIcon(this.getClass().getResource("./ajax-loader.gif"));
                  imageLabel.setIcon(ii);
                    imageLabel.setAlignmentX(CENTER_ALIGNMENT);
                searchResultsPanel.add(imageLabel, java.awt.BorderLayout.CENTER);
                searchResultsPanel.revalidate();
                searchResultsPanel.repaint();

            }
            //removes the loading indicator once results are returned from the API request
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
 

    //these are the results from the API request. i.e, the titles of sets related to the search query returned form the API request
    public class JSONResult extends JLabel implements MouseListener{
      public String id;
      public JSONArray flashTerms;
      public JPanel whatever = new JPanel();
      public String terms;
      public Font goBack = new Font("Arial", Font.BOLD, 15);
     
      //sets the Jlabel with the title of the set
      public JSONResult(String name){
       super(name);
       this.setOpaque(true);
       this.addMouseListener(this);
     } 
     //implementation of word-wrapping 
     //puts a html breakline tag every 5 words so that 
     //the jbutton doesn't display text in a single line but in multilines
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
      
      //on click, this executes
      //this gets the ters and definitions associated with the set clicked in the homepage
      public void getSearchResults(){
        final String theID = this.id;
        SwingWorker<Boolean,Integer> worker = new SwingWorker<Boolean,Integer>(){
          protected Boolean doInBackground() throws Exception {
             try {
               publish(12);
               //API request for terms associated with the set
               JSONObject json = readJsonFromUrl("https://api.quizlet.com/2.0/sets/"+theID+"?client_id=QbgwbRMGAU&whitespace=1&total_results=40");
 
              //System.out.println(json.getJSONArray("sets").getJSONObject(0));
              flashTerms = json.getJSONArray("terms");
              //iterates through each terms+definition and keeps adding button with the term
              for (int i = 0;i<=flashTerms.length(); i++) {
                JSONObject firstResult = flashTerms.getJSONObject(i);
                String title = firstResult.get("term").toString();
                String definition = firstResult.get("definition").toString();
                definition = getMultiLine(definition);
 
                termsButton resultLabel  = new termsButton(title, definition);
                resultLabel.setFont(goBack);
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
 
    //Displays and constructs the flash cards view where it displays all the terms associated with the set
     public void mouseClicked(MouseEvent e){
 
       whatever.setLayout(new GridLayout(4,2));
       mainPanel.add(new JScrollPane(whatever), "test");
       ImageIcon goBackImage = new ImageIcon(this.getClass().getResource("back.png"));
 
       //back button
       Image img = goBackImage.getImage();
       Image newImg = img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH);
       goBackImage = new ImageIcon(newImg);
 
       //button to switch to the testing view
       JButton flashTestButton = new JButton("Test yourself!");
       flashTestButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
           testPanel.setTerms(flashTerms);
           cl.show(mainPanel, "testPanel");
         }
       });

       flashTestButton.setFont(new Font("Arial", Font.BOLD, 26));
 
       JButton goBackButton = new JButton(goBackImage);
 
       //goes back to the homepage
       goBackButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae) {
           cl.show(mainPanel, "1");
           frame.setSize(1000, 800);
         }
       });
 
       whatever.add(goBackButton);
       whatever.add(flashTestButton);
       //executes the query 
       getSearchResults();
 
 
       //goes to the flashcards view
       cl.show(mainPanel,"test");
       frame.setSize(1000, 800);
 
     }
 
     public void mousePressed(MouseEvent e) {
     }
 
     public void mouseReleased(MouseEvent e) {
     }
 
     //hover over to highlight to a blue shade
     public void mouseEntered(MouseEvent e) {
 
       this.setBackground(new Color(235,248,255));
 
     }
 
     //mouseout to remove blue highlight to jlabel
     public void mouseExited(MouseEvent e) {
       this.setBackground(new Color(238,238,238));
       this.setForeground(Color.black);
     }
    }
 
    }
    
    //this is the button/flashcard that is viewed in the flash cards view in a gridlayout
    public static class termsButton extends JButton implements ActionListener, MouseListener{
      String term, definition;
      Boolean showingTerm = true;
 
      //initializes the instance variables and adds itself to a mouse and action listener
      public termsButton(String term, String definition){
        super(term);
        this.term = term;
        this.definition = definition;
        this.addActionListener(this);
        this.addMouseListener(this);
      }
 
      //on button click, displays the definition. If definition is already displayed, displays the term instead
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
      
      //a notice to let the user know to click to show definition
       public void mouseEntered(MouseEvent e) {
         if (showingTerm){
           this.setText("<html>"+this.term+"<br>"+"Click to flip to definition</html>");
         } else {
           this.setText(this.definition.substring(0, definition.length()-7)+"<br>"+"Click to flip to term</html>");
         }
 
       }
 
       public void mouseClicked(MouseEvent e) {
 
       }

       //removes the notice as mouse leaves the "flashcard"/button
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

    //This class paints the gauge meter depending on string distance that the jaccard similarity algorithm returns
    public static class testPaint extends JComponent {

      //paints a rectangle with an appropriate size and color depending on the distance computed
      //from the jaccard distance algorithm
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
        } else {
          super.paintComponent(g);
        }
      }
    }
   
  //Creates the view for testing the user
  public static class ExtendingClass extends JPanel {
 
    public static JSONArray thisFlashArray;
    public static String term;
    public static String definition;
    public static ArrayList<Integer> termsList;
    public static JTextArea userInput;  
    public static JButton backButton;
    public static testPaint gaugeMeter = new testPaint();
    public static JLabel randomFlashCard;
    public static JPanel theBack = new JPanel();
    public static JPanel theFlow = new JPanel(new FlowLayout());
    public static JPanel theFlow2 = new JPanel(new FlowLayout());
    public static JPanel centerFlow = new JPanel();
 
    //initializes the view/panel by adding back button 
    public ExtendingClass(){

    this.setLayout(new BorderLayout());
    centerFlow.setLayout(new BoxLayout(centerFlow, BoxLayout.PAGE_AXIS));
    
    ImageIcon goBackImage = new ImageIcon(this.getClass().getResource("back.png"));
 
      //get image from local directory
       Image img = goBackImage.getImage();
       //scales the image to an appropriate size
       Image newImg = img.getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH);
       goBackImage = new ImageIcon(newImg);
       backButton = new JButton(goBackImage);
       JButton backButton2 = new JButton(goBackImage);

       //centers and adds image to the view
       backButton.setAlignmentX(CENTER_ALIGNMENT);
       theFlow.add(backButton);
       theFlow2.add(backButton2);
       backButton2.setVisible(false);
       this.add(theFlow, BorderLayout.WEST);
       this.add(theFlow2, BorderLayout.EAST);
 
 

    //listener to go back to the previous view (flashcards view)
    backButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cl.show(mainPanel, "test");
        frame.setSize(1000, 800);
      }
    }); 
         
  }
  //gets random int from an arraylist of ints and not just a range of numbers to access the array of terms and retrieve by index
  //also removes random int from arraylist of ints so that no flashcards are shown more than once to the user
    public int getRandom(){
      Random generator =  new Random();
 
      int getRandomFlashCard = generator.nextInt(termsList.size());
      termsList.remove(getRandomFlashCard);
      return getRandomFlashCard;
    }

    //sets the term and definition instance variables for the testing view
    public void setTermAndDef(){
      try {
        JSONObject flashObject = thisFlashArray.getJSONObject(getRandom());
        term = flashObject.get("term").toString();
        definition = flashObject.get("definition").toString();
      } catch(JSONException ex) {
      }

    }
 
    //adds the term and textarea field to the panel
    public void setTerms(JSONArray flashArray){
      thisFlashArray = flashArray;
      termsList = new ArrayList<>();

      for(int i = 0; i < flashArray.length(); i++){
        termsList.add(i);
      }
 
      //removes all components in centerFlow if this testing view was accessed previously
      if(randomFlashCard != null || userInput != null){
        centerFlow.removeAll();
      }

      //retrieves random flashcard from terms array
      //and adds it to the centerFlow panel in the CENTER of the border layout
      setTermAndDef();
      randomFlashCard = new JLabel(term);
      randomFlashCard.setHorizontalAlignment(SwingConstants.CENTER);
      centerFlow.add(randomFlashCard);

      //text area for user input
      userInput = new JTextArea(5,40);
      userInput.setAlignmentX(CENTER_ALIGNMENT);
      centerFlow.add(userInput);

      JButton checkButton = new JButton("Check answer");
      JLabel testLabel = new JLabel("          ");
      testLabel.setAlignmentX(CENTER_ALIGNMENT);

      JButton nextButton = new JButton("Next term");
      nextButton.setAlignmentX(CENTER_ALIGNMENT);

      //clears textarea and retreives random flashcard/term for user to test him/herself
      nextButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ae){
          userInput.setText(null);
          //false so that painComponent will clear and paint nothing
          checkedTerm = false;

          setTermAndDef();
          randomFlashCard.setText(term);
          repaint();
        }
      });

        checkButton.setAlignmentX(CENTER_ALIGNMENT);
        //this checks for string similarity
        //and returns a double (0-1) where the closer to zero, the similar the string is to the definition
        checkButton.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){
            checkedTerm = true;

            //logic to have the double value returned from the jaccard algorithm
            //to be a value getting closer to hundred instead of zero, meaning more accuracy of the user's answer
            distance = jaccard.Jaccard2(definition, userInput.getText());
            distance = (1-(distance-0.05))*100;
            repaint();
          }
        });
        //adds all the next, check button, label and gauge meter in the cenFlow panel
        centerFlow.add(nextButton);
        centerFlow.add(checkButton);
        centerFlow.add(testLabel);
        centerFlow.add(gaugeMeter);

        userInput.setMaximumSize(userInput.getPreferredSize());
        userInput.setAlignmentX(CENTER_ALIGNMENT);
        randomFlashCard.setAlignmentX(CENTER_ALIGNMENT);

        //this adds the centerFlow panel to the center of the border layout
        this.add(centerFlow, BorderLayout.CENTER);
        this.revalidate();
        frame.setSize(800, 350);
    }
  }
}  
