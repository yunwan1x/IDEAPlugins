package com.vs2010wy.tool.ui;

import com.vs2010wy.tool.util.ProductUtil;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class AboutPanel extends JPanel {
    private class LinkListener extends MouseAdapter {
        String link;
        JLabel label;

        public LinkListener(String link, JLabel label) {
            this.link = link;
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                Desktop.getDesktop().browse(new URI(link));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setText("<HTML><FONT color=\"#00aa99\"><U>" + link + "</U></FONT></HTML>");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setText("<HTML><FONT color=\"#000099\"><U>" + link + "</U></FONT></HTML>");
        }

    }

    public AboutPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("offline-review-tool " + ProductUtil.getInstance()
                .getProperty("version", "0.1"));
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        this.add(title);
        this.add(new JLabel(" "));
        this.add(new JLabel("written by ifreefly"));
        this.add(new JLabel(" "));
        this.add(createLinkLabel("https://github.com/ifreefly/offline-review-plugin-intellij"));
    }

    private JLabel createLinkLabel(String link) {
        String linkPattern = "<HTML><FONT color=\"#000099\"><U> %s </U></FONT></HTML>";

        JLabel label = new JLabel(String.format(linkPattern, link));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new LinkListener(link, label));
        return label;
    }
}
