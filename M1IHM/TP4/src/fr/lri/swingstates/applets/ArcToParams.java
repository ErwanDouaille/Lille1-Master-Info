package fr.lri.swingstates.applets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;


public class ArcToParams extends BasicApplet {

	Canvas c =new Canvas(500,500);
	CPolyLine test = (CPolyLine)c.newPolyLine(250,250).setFilled(true).setFillPaint(Color.YELLOW).setStroke(new BasicStroke(3)).setOutlinePaint(Color.RED);
	
	ChangeListener changed = new SliderListener ();
	JPanel sliders = new JPanel();
	JSlider startingAngle, extent, radiusX, radiusY;
	
	Font font1 = new Font("verdana", Font.PLAIN, 12);
	Font font2 = new Font("times", Font.ITALIC, 16);
	DecimalFormat formatter = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("en", "US"));
	
	CPolyLine circle;
	CSegment piBy2seg, piseg;
	
	CText zero, pi, piBy2, piBy2neg, point, center;
	
	CText infos;
	
	class SliderListener implements ChangeListener {
		
		public void stateChanged (ChangeEvent ev) {
			double startAngle = startingAngle.getValue() * Math.PI / 180.0;
			double extentAngle = extent.getValue() * Math.PI / 180.0;
			double rx = radiusX.getValue();
			double ry = radiusY.getValue();
			test.reset(250, 250);
			test.arcTo(startAngle, extentAngle, rx, ry);
			circleTrigo(startAngle, extentAngle, rx, ry);
			infos(startAngle, extentAngle, rx, ry);
		}
	}
	
	void infos(double startAngle, double extentAngle, double rx, double ry) {
		infos.setText("arcTo("+formatter.format(startAngle)+
				", "+formatter.format(extentAngle)+
				", "+formatter.format(rx)+
				", "+formatter.format(ry)+")");
	}
	
	void circleTrigo(double startAngle, double extentAngle, double rx, double ry) {
		
		Point2D ptC = new Point2D.Double(250-rx*Math.cos(startAngle), 250+ry*Math.sin(startAngle));
		center.translateTo(250-rx*Math.cos(startAngle), 250+ry*Math.sin(startAngle));
		circle.reset(250, 250).arcTo(startAngle, 2*Math.PI, rx, ry); 
		Point2D pt1 = new Point2D.Double(ptC.getX(), ptC.getY() - ry);
		Point2D pt2 = new Point2D.Double(ptC.getX(), ptC.getY() + ry);
		piBy2seg.setPoints(pt1, pt2);
		piBy2.translateTo(pt1.getX(), pt1.getY());
		piBy2neg.translateTo(pt2.getX(), pt2.getY());
		pt1 = new Point2D.Double(ptC.getX() - rx, ptC.getY());
		pt2 = new Point2D.Double(ptC.getX() + rx, ptC.getY());
		piseg.setPoints(pt1, pt2);
		pi.translateTo(pt1.getX(), pt1.getY());
		zero.translateTo(pt2.getX(), pt2.getY());
		
		
	}
	
	JSlider makeSlider (String name, int min, int max, int val, int major, int minor) {
		JLabel l = new JLabel (name);
		JSlider s = new JSlider (JSlider.HORIZONTAL, min, max, val);
		s.setMajorTickSpacing(major);
		s.setMinorTickSpacing(minor);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
		s.addChangeListener(changed);
		sliders.add(l);
		sliders.add(s);
		return s;
	}
	
	public void createGUI() {
		sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
		startingAngle = makeSlider("start", -360, 360, 0, 100, 10);
		extent = makeSlider("extent", -360, 360, 90, 100, 10);
		radiusX = makeSlider("radius x", 10, 200, 100, 100, 10); 
		radiusY = makeSlider("radius y", 10, 200, 100, 100, 10); 
		test.arcTo(0, Math.PI/2, 100, 100);
		circle = (CPolyLine) c.newPolyLine(250, 250).setFilled(false).setOutlinePaint(Color.LIGHT_GRAY).addTag("circleTrigo");
		piBy2seg = (CSegment) c.newSegment(250, 250, 250-1, 250+1).setOutlinePaint(Color.LIGHT_GRAY).addTag("circleTrigo");
		piseg = (CSegment) c.newSegment(250-1, 250+1, 250, 250).setOutlinePaint(Color.LIGHT_GRAY).addTag("circleTrigo");
		zero = (CText) c.newText(250+1, 250, "0", font1).setFillPaint(Color.BLACK).setOutlined(false);
		pi = (CText) c.newText(250-1, 250, "PI", font1).setFillPaint(Color.BLACK).setOutlined(false);
		piBy2 = (CText) c.newText(250, 250-1, "PI/2", font1).setFillPaint(Color.BLACK).setOutlined(false);
		piBy2neg = (CText) c.newText(250, 250+1, "3*PI/2", font1).setFillPaint(Color.BLACK).setOutlined(false);
		point = (CText) c.newText(250+10, 250+20, "P", font2).setFillPaint(Color.DARK_GRAY).setOutlined(false);
		center = (CText) c.newText(250, 250, "C", font2).setFillPaint(Color.DARK_GRAY).setOutlined(false);
		circleTrigo(0, Math.PI/2, 100, 100);
		
		infos = (CText) c.newText(20, 20, "", font1).setFillPaint(Color.BLUE).setOutlined(false);
		infos(0, Math.PI/2, 100, 100);
		setLayout(new FlowLayout());
		add(c);
		sliders.setPreferredSize(new Dimension(290, 500));
		add(sliders);
		setVisible(true);
		
		setSize(800, 500);
	}
	
}
