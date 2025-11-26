package components;

import utils.UIStyles;
import javax.swing.*;
import java.awt.*;

public class StyledTitle extends JLabel {
    
    public enum TitleSize {
        LARGE(28),
        MEDIUM(22),
        SMALL(18);
        
        private final int fontSize;
        
        TitleSize(int fontSize) {
            this.fontSize = fontSize;
        }
        
        public int getFontSize() {
            return fontSize;
        }
    }
    
    public StyledTitle(String text, TitleSize size) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, size.getFontSize()));
        setForeground(new Color(51, 51, 51));
    }
    
    public StyledTitle(String text) {
        this(text, TitleSize.MEDIUM);
    }
}