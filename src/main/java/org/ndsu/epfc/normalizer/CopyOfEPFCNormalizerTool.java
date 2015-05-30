package org.ndsu.epfc.normalizer;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class CopyOfEPFCNormalizerTool extends JPanel
                             implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
    JButton openButton, saveButton, normBtn, cancelBtn;
    JTextArea log;
    JTextField ipDir;
    JTextField opDir;
    JTextField ipCount;
    JTextField opCount;
    JFileChooser fc;
    JLabel tsetlbl;
    JCheckBox tset;
    
    JLabel ipDirLbl;
    JLabel opDirLbl;
    JLabel ipCountLbl;
    JLabel opCountLbl;

    public CopyOfEPFCNormalizerTool() {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //setAlignmentX(Component.LEFT_ALIGNMENT);	
        
        ipDirLbl = new JLabel("Enter input file name:");
        ipDir = new JTextField(50);
        
        opDirLbl = new JLabel("Enter output file name:");
        opDir = new JTextField(50);
        
        ipCountLbl = new JLabel("Enter input count:");
        ipCount = new JTextField(5);
        
        opCountLbl = new JLabel("Enter output count:");
        opCount = new JTextField(5);
        
        tsetlbl = new JLabel("Output as tset file");
        tset = new JCheckBox();
        tset.addActionListener(this);
        
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser(System.getProperty("user.dir"));

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Choose file",
                                 createImageIcon("/images/Open16.gif"));
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save File...",
                                 createImageIcon("/images/Save16.gif"));
        saveButton.addActionListener(this);
        
        normBtn = new JButton("Normalize");
        normBtn.addActionListener(this);
		
		//Create the save button.  We use the image from the JLF
		//Graphics Repository (but we extracted it from the jar).
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel ipPanel = new JPanel(); //use FlowLayout
        ipPanel.add(ipDirLbl);
        ipPanel.add(ipDir);
        ipPanel.add(openButton);
        
        JPanel ipcountPanel = new JPanel(); //use FlowLayout
        ipcountPanel.add(ipCountLbl);
        ipcountPanel.add(ipCount);
      
        JPanel opcountPanel = new JPanel(); 
        opcountPanel.add(opCountLbl);
        opcountPanel.add(opCount);
        opcountPanel.add(tsetlbl);
        opcountPanel.add(tset);
        
        
        JPanel opPanel = new JPanel(); //use FlowLayout
        opPanel.add(opDirLbl);
        opPanel.add(opDir);
        opPanel.add(saveButton);
        
        JPanel btnPanel = new JPanel(); //use FlowLayout
        btnPanel.add(normBtn);
        btnPanel.add(cancelBtn);

        //Add the buttons and the log to this panel.
       
        add(leftJustify(ipPanel));
        add(leftJustify(ipcountPanel));
        add(leftJustify(opPanel));
        add(leftJustify(opcountPanel));
        add(leftJustify(btnPanel));
        add(logScrollPane);
        
        //ipPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //countPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //opPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private Component leftJustify( JPanel panel )  {
        Box  b = Box.createHorizontalBox();
        b.add( panel );
        b.add( Box.createHorizontalGlue() );
        // (Note that you could throw a lot more components
        // and struts and glue in here.)
        return b;
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
        	fc = new JFileChooser(System.getProperty("user.dir"));
        	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = fc.showOpenDialog(CopyOfEPFCNormalizerTool.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                ipDir.setText(file.getAbsolutePath());
                log.append("Input file: " + file.getAbsolutePath() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        //Handle save button action.
        } else if (e.getSource() == saveButton) {
        	fc = new JFileChooser(System.getProperty("user.dir"));
        	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = fc.showSaveDialog(CopyOfEPFCNormalizerTool.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file =  fc.getCurrentDirectory();
               
                String fname = "opfile.tset";
                try{
                	File ipfile = new File(ipDir.getText());
                	fname = ipfile.getName();

                }catch(Throwable t){
                	
                }
            	opDir.setText(file.getAbsolutePath()+"/"+fname);
                setOutputFileName();
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }else if (e.getSource() == normBtn) {
        	//MinMaxNormali normalizer = new MinMa
        	
        	try {
        		String ip = ipDir.getText();
            	int ipCnt = Integer.valueOf(ipCount.getText());
            	String op = opDir.getText();
            	int opCnt = Integer.valueOf(opCount.getText());
            	
            	
            	RunNormalizer normalizer = new RunNormalizer();
				normalizer.normalize(ip, ipCnt, opCnt,op);
				log.append("Succefully normalized input file and saved" + newline);
			} catch (Throwable e1) {
				e1.printStackTrace();
				log.append("Error normalizing input file." + newline);
			} 
            log.setCaretPosition(log.getDocument().getLength());
        }else if (e.getSource() == cancelBtn) {
        	log.append("Cancelling the operation." + newline);
        	System.exit(0);
        }else if (e.getSource() == tset) {
        	log.append("Output file will be in tset format." + newline);
        	setOutputFileName();
        	
        }
    }

	private void setOutputFileName() {
		boolean checked = tset.isSelected();
		try{
			//File opfile = new File(opDir.getText());
			String fname = opDir.getText();
			if(checked){
				fname = fname.substring(0,fname.indexOf(".csv"))+".tset";
			}else{
				fname = fname.substring(0,fname.indexOf(".tset"))+"csv";
			}
			opDir.setText(fname);
			log.append("Output Location: " + fname + "." + newline);
		}catch(Throwable t){
			
		}
	}

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CopyOfEPFCNormalizerTool.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("EPFCNormalizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new CopyOfEPFCNormalizerTool());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}