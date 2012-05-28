/**
 * 
 */
package gui.dialogs;


import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.EuclideDialog;
import gui.EuclideGui;

/**
 * @author dlegland
 *
 */
public class AboutDialog extends EuclideDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	URI uri = null;

	JButton okButton = new JButton("OK");
	JButton hyperlink;
	
	public AboutDialog(EuclideGui gui) {
		super(gui, "About Euclide", true);
		
		try {
			 uri = new URI("http://jeuclide.sourceforge.net/");
		} catch(URISyntaxException ex) {
			//TODO: something ?
		}
		this.setLocationRelativeTo(gui.getCurrentFrame());
        
		hyperlink = new JButton("<html><u>http://jeuclide.sourceforge.net/</u></html>");
		hyperlink.setBorderPainted(false);
		hyperlink.setOpaque(false);
		hyperlink.setBackground(this.getBackground());
        hyperlink.setToolTipText(uri.toString());
		hyperlink.addActionListener(this);

		okButton.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		addItem(panel, new JLabel("Euclide"));
		addItem(panel, new JLabel(" v - " + gui.getVersion()));
		addItem(panel, new JLabel(" "));
		addItem(panel, new JLabel("Dynamic geometry editor."));
		addItem(panel, new JLabel(" "));
		addItem(panel, new JLabel("Web page :"));
		addItem(panel, hyperlink);
		addItem(panel, new JLabel(" "));
		addItem(panel, okButton);
		
        
//		linkPanel.addActionListener(this);
		this.setContentPane(panel);
		this.pack();			
	}

	private void addItem(JPanel panel, JComponent comp){
		comp.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(comp);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == okButton) {
			this.setVisible(false);
		}
		if (event.getSource() == hyperlink) {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(uri);
				} catch (IOException e) {
					// TODO: error handling
				}
			} else {
				// TODO: error handling
			}

		}
	}	
}
