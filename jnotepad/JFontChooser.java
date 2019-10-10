
package jnotepad;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFontChooser extends JDialog{
    private boolean flag;
    private JList<String> fontNames;
    private JList<String> sizeNums;
    private ArrayList<String> blah;
    private JCheckBox bold;
    private JCheckBox italics;
    private JButton okay;
    private JButton cancel;
    private Font font;
    private Font savedFont;
    private boolean italicsTrue;
    private boolean boldTrue;
    private JScrollPane fontScroll;
    private JScrollPane sizeScroll;
    private String nameChoice;
    private int sizeChoice;
    private int boldChoice;
    private int italicsChoice;
    private String[] fontsInSwing;
    private String[] sizesInSwing;
    private Font newFonts;
    
    JFontChooser(JFrame parent){
        this.setLayout(new GridBagLayout());
        GridBagConstraints griddy = new GridBagConstraints();
        griddy.fill = GridBagConstraints.HORIZONTAL;
        griddy.insets = new Insets(2,2,5,5);
        
        fontsInSwing = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontNames = new JList<>(fontsInSwing);
        fontNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontScroll = new JScrollPane(fontNames);
        fontNames.setSelectedIndex(1);
        nameChoice = "Courier New";
        
        sizeChoice = 12;
        //ArrayList sizes = new ArrayList<>();
        String sizes[] = new String[44];
      //  blah = new ArrayList<String>();
        
        int i = 8;
        int j=0;
        do{ sizes[j] = Integer.toString(i);
            i++;
            i++;
            j++;
          }while(j<44);
        sizesInSwing = new String[44];
        i = 0;
        
        do{ sizesInSwing[i] = sizes[i];
            
            i++;
          }while(i<44);
        sizeNums = new JList<>(sizesInSwing);
        sizeNums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sizeScroll = new JScrollPane(sizeNums);
        sizeNums.setSelectedIndex(2);
        bold = new JCheckBox("Bold");
        italics = new JCheckBox("Italics");
        cancel = new JButton("Cancel");
        cancel.addActionListener(ae -> {
            this.setVisible(false);
        });
        okay = new JButton("Ok");
        okay.addActionListener( ae ->{
            try{
                nameChoice = fontNames.getSelectedValue();
                
                sizeChoice = Integer.parseInt(sizeNums.getSelectedValue());
                if(bold.isSelected()&&italics.isSelected()){
                    boldChoice = Font.BOLD;
                    italicsChoice = Font.ITALIC;
                }
                else if(bold.isSelected()){
                    boldChoice = Font.BOLD;
                }
                else if(italics.isSelected()){
                    italicsChoice = Font.ITALIC;
                }
                else{
                    italicsChoice = 0;
                    boldChoice = 0;
            }
            }catch(NullPointerException e){
            }
            this.dispose();
            this.setVisible(false);
        });
        griddy.gridx = 0;
        griddy.gridy = 0;
        griddy.gridheight = 1;
        griddy.gridwidth = 2;
        this.add(fontScroll, griddy);
        griddy.gridx = 4;
        griddy.gridy = 0;
        griddy.gridheight = 1;
        griddy.gridwidth = 2;
        this.add(sizeScroll, griddy);
        griddy.gridx= 0;
        griddy.gridy = 3;
        this.add(bold, griddy);
        griddy.gridx = 4;
        griddy.gridy = 3;
        this.add(italics, griddy);
        griddy.gridx = 0;
        griddy.gridy = 4;
        this.add(okay, griddy);
        griddy.gridx = 4;
        griddy.gridy = 4;
        this.add(cancel, griddy);
        this.setSize(300,300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //this.setVisible(true);
    }
    public Font fontStyleAndSize(Font aFont){
            return newFonts = new Font(nameChoice,boldChoice | italicsChoice,sizeChoice);
        }

}