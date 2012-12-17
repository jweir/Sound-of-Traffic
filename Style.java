import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

interface Style {
	Font smallFont = new Font("Helvetica", Font.PLAIN, 11);
	Font bodyFont = new Font("Helvetica", Font.PLAIN, 12);
	Font bodyFontBold = new Font("Helvetica", Font.BOLD, 12);
	Font largeFont = new Font("Helvetica", Font.BOLD, 16);
	Font fontFixedSmall = new Font("Helvetica", Font.BOLD, 9);
	
	Color colorBG = new Color(230,240,200);
	Color colorBGActive = new Color(240,255,200);
	Color colorBGMax = new Color(230,245,160);
	Color colorBGInactive = new Color(210,210,210);
	
	Color colorGrid = new Color(255,255,255);
	
	Color colorTrans = new Color(0,0,0,0);
	Color colorPortActive = new Color(255,255,128);
	Color colorPortInactive = new Color(180,170,170);
	
	Color colorFont = new Color(80,80,80);
	Color colorFontUserModified = new Color(40,60,20);
	
	Color colorBarInactive = new Color(90,74,64);
	Color colorBarActive = new Color(160,220,64);
	Color colorBarMax = new Color(120,160,64);
	Color colorBarTotal = new Color(90,74,64);
		
	static int iconWidth = 80;
	static int iconHeight = 80;
}
