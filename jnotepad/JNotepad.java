//
//      Name:       Osborn, Cayce
//      Project:    5(Final)
//      Due:        Friday, December 7th, 2018
//      Course:     CS-2450-01-F18
//      Description:
//                  Implementation of the JNotepad as a suitable replacement
//                  for Notepad on Windows. 
//

package jnotepad;

//importing components
import javax.swing.*;
import java.awt.Font;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class JNotepad implements ActionListener {
    private final JFrame frame;
    private JOptionPane opPane;
    private final JScrollPane textScroll;
    private final JMenuBar bar;
    private final JMenuItem newItem;
    private final JMenuItem save;
    private final JMenuItem saveAs;
    private final JMenuItem pageSetup;
    private final JMenuItem undo;
    private final JMenuItem delete;
    private final JMenuItem find;
    private final JMenuItem findNext;
    private final JMenuItem viewHelp;
    private final JMenuItem wordWrap;
    private final JMenu edit;
    private final JMenuItem print;
    private final JMenuItem open;
    private final JMenuItem exit;
    private final JMenuItem font;
    private final JMenu color;
    private final JMenuItem about;
    private final JMenu file;
    private final JMenu format;
    private final JMenu help;
    private final JMenu view;
    private final JMenuItem statusBar;
    private final JMenuItem foreground;
    private final JMenuItem background;
    private Font fontChooser;
    private JFontChooser selectedFont;
    private final JFileChooser openSesame;
    private JFileChooser saveSesame;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem replace;
    private JMenuItem goTo;
    private JMenuItem selectAll;
    private JMenuItem timeDate;
    private BufferedWriter saveFile;
    private final JTextArea myText;
    private JPopupMenu popUp;
    private JOptionPane aboutFrame;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private String oldText;
    private String newText;
    private String temp;
    private BufferedReader in;
    private File myFile;
    private JDialog findString;
    private Highlighter highlightIt;
    private DefaultHighlighter.DefaultHighlightPainter highlightColor;
    private int begin;
    private int end;
    private DefaultHighlighter.DefaultHighlightPainter dehighlight;
    private Object coloredText;
    private Highlighter.Highlight[] unhighlightIt;
    private String gotIt;
    private String index;
    private FileNameExtensionFilter javaOnly;
    private FileNameExtensionFilter textOnly;
    
    JNotepad(){
        //creation of the frame and the windowevent for the save dialog.
        frame = new JFrame("JNotepad");
        frame.addWindowListener(new WindowAdapter(){
            //Override the function for window closing events
            @Override
            public void windowClosing(WindowEvent we){
                //Close window if user chooses
                if(myText!=null){
                    if(JOptionPane.showConfirmDialog(frame, "Would you like to save?", 
                        "Confirm Exit" ,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                        try{
                            openSesame.showSaveDialog(frame);
                            saveFile = new BufferedWriter(new FileWriter(openSesame.getSelectedFile()));
                            myText.write(saveFile);
                        }
                        catch(IOException e){}
                            frame.dispose();
                            System.exit(0);
                    }
                    else{
                        frame.dispose();
                        System.exit(0);
                    }
                }
            }
        });
        frame.setLayout(new BorderLayout());
        frame.setIconImage(new ImageIcon("JNotepad.png").getImage());
        
        //creating the text area and the variables for comparison in
        //later methods.
        myText = new JTextArea();
        oldText = myText.getText();
        //myText.getDocument().addUndoableEditListener(this);
        textScroll = new JScrollPane(myText);
        
        //creating new document menu item
        newItem = new JMenuItem("New");
        newItem.addActionListener(this);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        newItem.setMnemonic(KeyEvent.VK_N);
        
        //creating save menuitem
        save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.setActionCommand("Save");
        
        //creating saveas menuitem
        saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(this);
        saveAs.setMnemonic(KeyEvent.VK_A);
        saveAs.setActionCommand("Save As");
        
        //creating pagesetup menu item
        pageSetup = new JMenuItem("Page Setup...");
        pageSetup.addActionListener(this);
        pageSetup.setMnemonic(KeyEvent.VK_U);
        pageSetup.setActionCommand("Page");
        
        //creating undo menu item
        undo = new JMenuItem("Undo");
        undo.addActionListener(this);
        undo.setMnemonic(KeyEvent.VK_U);
        undo.setActionCommand("Undo");
        
        //creating delete menu item
        delete = new JMenuItem("Delete");
        delete.addActionListener(this);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
        delete.setActionCommand("Delete");
        delete.setMnemonic(KeyEvent.VK_L);
        
        //creating find menu item
        find = new JMenuItem("Find...");
        find.addActionListener(this);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        find.setActionCommand("Find");
        find.setMnemonic(KeyEvent.VK_F);
        
        //creating find next menu item
        findNext = new JMenuItem("Find Next");
        findNext.addActionListener(this);
        findNext.setMnemonic(KeyEvent.VK_N);
        findNext.setActionCommand("Find Next");
        
        //creating edit menu
        edit = new JMenu("Edit");
        edit.addActionListener(this);
        edit.setMnemonic(KeyEvent.VK_E);
        edit.setActionCommand("Edit");
        
        //creating print menu item
        print = new JMenuItem("Print...");
        print.addActionListener(this);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        print.setActionCommand("Print");
        print.setMnemonic(KeyEvent.VK_P);
        
        //creating status bar to put into statuspanel
        statusBar = new JMenuItem("Status Bar");
        statusBar.addActionListener(this);
        statusBar.setActionCommand("Status");
        statusBar.setMnemonic(KeyEvent.VK_S);
        
        //setting up viewhelp menuitem
        viewHelp = new JMenuItem("View Help");
        viewHelp.setActionCommand("View Help");
        viewHelp.setMnemonic(KeyEvent.VK_H);
        
        //setting up wordwrap menuitem
        wordWrap = new JMenuItem("Word Wrap");
        wordWrap.addActionListener(this);
        wordWrap.setMnemonic(KeyEvent.VK_W);
        wordWrap.setActionCommand("WordWrap");
        
        bar = new JMenuBar();
        //setting up file menu
        file = new JMenu("File");
        file.addActionListener(this);
        file.setMnemonic(KeyEvent.VK_F);
        
        //setting up format menu
        format = new JMenu("Format");
        format.addActionListener(this);
        format.setMnemonic(KeyEvent.VK_O);
        
        //setting up help menu
        help = new JMenu("Help");
        help.addActionListener(this);
        help.setMnemonic(KeyEvent.VK_H);
        
        //setting up color menu
        color = new JMenu("Color");
        color.addActionListener(this);
        color.setMnemonic(KeyEvent.VK_C);
        
        //setting up view menu
        view = new JMenu("View");
        view.addActionListener(this);
        view.setMnemonic(KeyEvent.VK_V);
        
        //setting up dialog for file selection via menuitem open
        openSesame = new JFileChooser();
        javaOnly = new FileNameExtensionFilter("Java Files","java");
        textOnly = new FileNameExtensionFilter("Text Files","txt");
        openSesame.setFileFilter(javaOnly);
        openSesame.addChoosableFileFilter(textOnly);
        
        //setting up the open menuitem
        open = new JMenuItem("Open...");
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.addActionListener(this);
        open.setActionCommand("Open");
        
        //creating a dialog for menuitem exit
        exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setActionCommand("Exit");
        exit.addActionListener(this);
        
        //setting the font selector
        font = new JMenuItem("Font...");
        font.addActionListener(this);
        font.setActionCommand("Font");
        font.setMnemonic(KeyEvent.VK_F);
        
        //setting up a dialog for the about menuitem
        about = new JMenuItem("About JNotepad");
        about.addActionListener(this);
        about.setActionCommand("About");
        about.setMnemonic(KeyEvent.VK_A);
        
        //setting up the text color
        foreground = new JMenuItem("Set Foreground...");
        foreground.addActionListener(this);
        foreground.setActionCommand("Foreground");
        foreground.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK| ActionEvent.ALT_MASK));
        
        //setting up the background color
        background = new JMenuItem("Set Background...");
        background.addActionListener(this);
        background.setMnemonic(KeyEvent.VK_B);
        background.setActionCommand("Background");
        
        //creating a "useless" statuspanel
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cut.setMnemonic(KeyEvent.VK_T);
        cut.setActionCommand("Cut");

        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        paste.setMnemonic(KeyEvent.VK_P);

        paste.setActionCommand("Paste");
        
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copy.setMnemonic(KeyEvent.VK_C);
        copy.setActionCommand("Copy");
        
        replace = new JMenuItem("Replace...");
        replace.addActionListener(this);
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
        replace.setMnemonic(KeyEvent.VK_R);
        replace.setActionCommand("Replace");
        
        goTo = new JMenuItem("Go To...");
        goTo.addActionListener(this);
        goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,ActionEvent.CTRL_MASK));
        goTo.setMnemonic(KeyEvent.VK_G);
        goTo.setActionCommand("Go To");
        
        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setMnemonic(KeyEvent.VK_A);
        selectAll.setActionCommand("Select All");
        
        timeDate = new JMenuItem("Time/Date");
        timeDate.addActionListener(this);
        timeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        timeDate.setMnemonic(KeyEvent.VK_D);
        timeDate.setActionCommand("Time Date");
        
        selectedFont = new JFontChooser(frame);
        
        //adding everything to their respective menus and then to the frame
        edit.add(undo);
        edit.addSeparator();
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.addSeparator();
        edit.add(find);
        edit.add(findNext);
        edit.add(replace);
        edit.add(goTo);
        edit.addSeparator();
        edit.add(selectAll);
        edit.add(timeDate);
        color.add(foreground);
        color.add(background);
        file.add(newItem);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(pageSetup);
        file.add(print);
        file.addSeparator();
        file.add(exit);
        format.add(wordWrap);
        format.add(font);
        format.addSeparator();
        format.add(color);
        view.add(statusBar);
        help.add(viewHelp);
        help.addSeparator();
        help.add(about);
        bar.add(file);
        bar.add(edit);
        bar.add(format);
        bar.add(view);
        bar.add(help);
        frame.add(bar, BorderLayout.NORTH);
        frame.add(textScroll, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        
        //creating the popup menu for the mouse right click
        popUp = new JPopupMenu();
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        cut.setText("Cut");
        copy.setText("Copy");
        paste.setMnemonic(KeyEvent.VK_P);
        cut.setMnemonic(KeyEvent.VK_T);
        copy.setMnemonic(KeyEvent.VK_C);
        popUp.add(cut);
        popUp.add(copy);
        popUp.add(paste);
        myText.addMouseListener(new MouseListener(){
            @Override
            public void mouseExited(MouseEvent me){}
            @Override
            public void mouseEntered(MouseEvent me){}
            @Override
            public void mouseReleased(MouseEvent me){
                if (me.isPopupTrigger()) {
                    popUp.show(me.getComponent(), me.getX(), me.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent me){
                if (me.isPopupTrigger()) {
                    popUp.show(me.getComponent(), me.getX(), me.getY());
                }
            }
            @Override
            public void mouseClicked(MouseEvent me){}
        });
    }
    //invoke an instance of JNotepad
    public static void main(String args[]) throws Exception {
        java.awt.EventQueue.invokeLater(() -> {
            new JNotepad();
        });
    }
    //My actionperformed section split into a switch case for all 
    //relative actions performed by user.
    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        switch(action){
            
            case "Font":
                selectedFont.setModal(true);
                selectedFont.setVisible(true);
                fontChooser = selectedFont.fontStyleAndSize(fontChooser);
                myText.setFont(fontChooser);
                break;
            
            case "Open":
                int returnVal = openSesame.showOpenDialog(null);
                
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    myFile = openSesame.getSelectedFile();
                }
                try{
                    myText.setText(null);
                    in = new BufferedReader(new FileReader(myFile));
                    temp = in.readLine();
                    while(temp != null){
                        myText.append(temp + "\n");
                        temp = in.readLine();
                    }
                }
                catch(IOException e){
                }
                oldText = myText.getText();
                break;
                
            case "About":
                aboutFrame = new JOptionPane();
                aboutFrame.setSize(350,200);
                String stringy2 = new String("<html><center>JNotepad</center><br/><center>A program to display my knowledge"
                    + "of all things JSwing</center><br/><center>Created by Cayce Osborn(c)</center><br/>"
                    + "<center>Dec 7th, 2018</center></html>");
                
                aboutFrame.showMessageDialog(null,stringy2,"About JNotepad",JOptionPane.PLAIN_MESSAGE);
                aboutFrame.setVisible(true);
                break;
                
            case "Exit":
                int choice = JOptionPane.showConfirmDialog(null, "Would you like to save?",
                        "Select an option", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                if(choice == 0){
                    System.exit(0);
                }
                else if(choice == 1){
                    System.exit(0);
                }
                break;
                
            case "Background":
                Color change2 = JColorChooser.showDialog(null,"Choose font color", Color.WHITE);
                myText.setBackground(change2);
                break;
                
            case "Foreground":
                Color change = JColorChooser.showDialog(null,"Choose font color", Color.BLACK);
                myText.setForeground(change);
                break;
                
            case "Save":
                try{
                    saveFile = new BufferedWriter(new FileWriter(openSesame.getSelectedFile()));
                myText.write(saveFile);
                }
                catch(IOException e){}
                break;
            case "New":
                newText = myText.getText();
                if(oldText.equals(newText)){
                    
                }
                else{
                    //openSesame.showSaveDialog(frame);
                    int status = openSesame.showSaveDialog(frame);
                    if(status == JFileChooser.APPROVE_OPTION){
                        try{
                            saveFile = new BufferedWriter(new FileWriter(openSesame.getSelectedFile()));
                            myText.write(saveFile);
                        }
                        catch(IOException e){}
                        myText.setText(null);
                }}
                break;
                
            case "Save As":
                try{
                    openSesame.showSaveDialog(frame);
                    saveFile = new BufferedWriter(new FileWriter(openSesame.getSelectedFile()));
                    myText.write(saveFile);
                }
                catch(IOException e){}
                break;
            case "Page Setup":
                break;
            case "Print...":
                break;
            case "Undo":
                break;
            case "Cut":
                break;
            case "Copy":
                break;
            case "Paste":
                break;
            case "Select All":
                myText.selectAll();
                break;
            case "Delete":
                try{
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_DELETE);
                }
                 catch (AWTException ex) {
                Logger.getLogger(JNotepad.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "Find":
                JTextField findField = new JTextField();
                JButton findBtn = new JButton("Find");
                
                highlightIt = myText.getHighlighter();
                highlightColor = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
                findBtn.addActionListener((ActionEvent a)->{
                    try{
                        gotIt = findField.getText();
                        index = myText.getText();
                        begin = index.indexOf(gotIt);
                        end = begin + gotIt.length();
                        highlightIt.addHighlight(begin,end,highlightColor);
                        
                    }catch(BadLocationException toot){
                        System.out.println("Exact match was not found.");
                    }
                });
                JDialog findDialog = new JDialog();
                findDialog.setLayout(new BorderLayout());
                findDialog.add(findBtn, BorderLayout.SOUTH);
                findDialog.setLocationRelativeTo(frame);
                findDialog.setSize(75,100);
                findDialog.add(findField);
                findDialog.addWindowListener(new WindowAdapter(){
                 //Override the function for window closing events
                    @Override
                    public void windowClosing(WindowEvent we){
                            highlightIt.removeAllHighlights();
                    }
                });
                findDialog.setVisible(true);
                
                break;
            case "Find Next":
                begin = index.indexOf(gotIt, end);
                end = begin + gotIt.length();
                try{
                    highlightIt.addHighlight(begin,end,highlightColor);
                }catch(BadLocationException toot){
                    System.out.println("No more matches found.");
                }
                break;

            case "WordWrap":
                if(myText.getLineWrap() == true){
                    myText.setLineWrap(false);
                    myText.setWrapStyleWord(false);
                }
                else if(myText.getLineWrap() == false){
                    myText.setLineWrap(true);
                    myText.setWrapStyleWord(true);
                }
                break;
            
            case "Time Date":
                SimpleDateFormat timedate = new SimpleDateFormat(" h:mm a  MM/d/yyyy ");
                Calendar currentCalendar = Calendar.getInstance();
                Date currentTime = currentCalendar.getTime();
                myText.append(timedate.format(currentTime));
                break;
            default:
                break;
        }
    }
}