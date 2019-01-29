// Skeletal program for the "Image Histogram" assignment
// Written by:  Minglun Gong

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

// Main class
public class ImageHistogram extends Frame implements ActionListener {
	BufferedImage input;
	int width, height;
	TextField texRad, texThres;
	ImageCanvas source, target;
	PlotCanvas plot;
	int GREY_LEVEL = 256;
	// Constructor
	public ImageHistogram(String name) {
		super("Image Histogram");
		// load image
		try {
			input = ImageIO.read(new File(name));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new ImageCanvas(input);
		plot = new PlotCanvas();
		target = new ImageCanvas(input);
		main.setLayout(new GridLayout(1, 3, 10, 10));
		main.add(source);
		main.add(plot);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		Button button = new Button("Display Histogram");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Stretch");
		button.addActionListener(this);
		controls.add(button);
		controls.add(new Label("Cutoff fraction:"));
		texThres = new TextField("10", 2);
		controls.add(texThres);
		button = new Button("Aggressive Stretch");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Equalization");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		setSize(width*2+400, height+100);
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
		// compute the histogram of the image
		if ( ((Button)e.getSource()).getLabel().equals("Display Histogram") ) {
			float red=0, green=0, blue=0;
			int redHist[] = new int[GREY_LEVEL];
			int greenHist[] = new int[GREY_LEVEL];
			int blueHist[] = new int[GREY_LEVEL];
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if ((int)red == intensity){
							redHist[intensity]++;
						}
						if ((int)green == intensity){
							greenHist[intensity]++;
						}
						if ((int)blue == intensity){
							blueHist[intensity]++;
						}
					}
				}
			for (int j=0; j<GREY_LEVEL; j++){
				//manually divide the original # of pixels by 3 in order to show them in canvas better
				redHist[j] = (int)(redHist[j]/3);
				greenHist[j] = (int)(greenHist[j]/3);
				blueHist[j] = (int)(blueHist[j]/3);
				System.out.println(j+" "+ redHist[j]+" "+greenHist[j] + " "+ blueHist[j]);
			}
			plot.setHist(redHist, greenHist, blueHist);

		}



        // Listener for each button
        if (((Button) e.getSource()).getLabel().contentEquals("Histogram Stretch")) {
            //implement algorithm
            System.out.println("Histogram stretch");
			float red=0, green=0, blue=0;
			int redHist[] = new int[GREY_LEVEL];
			int greenHist[] = new int[GREY_LEVEL];
			int blueHist[] = new int[GREY_LEVEL];
			int redHistStretch[] = new int[GREY_LEVEL];
			int greenHistStretch[] = new int[GREY_LEVEL];
			int blueHistStretch[] = new int[GREY_LEVEL];
			int minR = 0, maxR = GREY_LEVEL-1;
			int minG = 0, maxG = GREY_LEVEL-1;
			int minB = 0, maxB = GREY_LEVEL-1;
			BufferedImage output;
			output = input;
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if ((int)red == intensity){
							redHist[intensity]++;
						}
						if ((int)green == intensity){
							greenHist[intensity]++;
						}
						if ((int)blue == intensity){
							blueHist[intensity]++;
						}
					}

				}

			while(redHist[minR] == 0){
				minR ++;
			}
			while(redHist[maxR] == 0){
				maxR --;
			}
			while(greenHist[minG] == 0){
				minG ++;
			}
			while(greenHist[maxG] == 0){
				maxG --;
			}
			while(blueHist[minB] == 0){
				minB ++;
			}
			while(blueHist[maxB] == 0){
				maxB --;
			}
			System.out.println(minR+" "+maxR);
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					int newRed;
					int newGreen;
					int newBlue;
					newRed = (int) ((red - minR)*(GREY_LEVEL-1)/(maxR - minR));
					newGreen = (int) ((green - minG)*(GREY_LEVEL-1)/(maxG - minG));
					newBlue = (int) ((blue - minB)*(GREY_LEVEL-1)/(maxB - minB));
					int p = (newRed<<16) | (newGreen<<8) | newBlue;
					output.setRGB(x, y, p);
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if (newRed == intensity){
							redHistStretch[intensity]++;
						}
						if (newGreen == intensity){
							greenHistStretch[intensity]++;
						}
						if (newBlue == intensity){
							blueHistStretch[intensity]++;
						}
					}

				}
			target.resetImage(output);



			for (int j=0; j<GREY_LEVEL; j++){

				redHistStretch[j] = (int)(redHistStretch[j]/3);
				greenHistStretch[j] = (int)(greenHistStretch[j]/3);
				blueHistStretch[j] = (int)(blueHistStretch[j]/3);

			}

			plot.setHist(redHistStretch, greenHistStretch, blueHistStretch);
        }


        if (((Button) e.getSource()).getLabel().contentEquals("Aggressive Stretch")) {
			//implement algorithm
			System.out.println("Aggressive Stretch");
			float red = 0, green = 0, blue = 0;
			int redHist[] = new int[GREY_LEVEL];
			int greenHist[] = new int[GREY_LEVEL];
			int blueHist[] = new int[GREY_LEVEL];
			int redHistStretch[] = new int[GREY_LEVEL];
			int greenHistStretch[] = new int[GREY_LEVEL];
			int blueHistStretch[] = new int[GREY_LEVEL];
			int minR = 0, maxR = GREY_LEVEL - 1;
			int minG = 0, maxG = GREY_LEVEL - 1;
			int minB = 0, maxB = GREY_LEVEL - 1;
			float cutOffFra = Float.valueOf(texThres.getText());
			int cutOffNum = (int) (height * width * cutOffFra / 100);
			int count;
			BufferedImage output;
			output = input;
			for (int y = 0, i = 0; y < height; y++)
				for (int x = 0; x < width; x++, i++) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					for (int intensity = 0; intensity < GREY_LEVEL; intensity++) {
						if ((int) red == intensity) {
							redHist[intensity]++;
						}
						if ((int) green == intensity) {
							greenHist[intensity]++;
						}
						if ((int) blue == intensity) {
							blueHist[intensity]++;
						}
					}

				}


			count = 0;
			while((count < cutOffNum)&&(minR<GREY_LEVEL)){
				count += redHist[minR];
				minR++;
			}

			count = 0;
			while((count < cutOffNum)&&(maxR>0)){
				count += redHist[maxR];
				maxR--;
			}

			count = 0;
			while((count < cutOffNum)&&(minG<GREY_LEVEL)){
				count += greenHist[minG];
				minG++;
			}

			count = 0;
			while((count < cutOffNum)&&(maxG>0)){
				count += greenHist[maxG];
				maxG--;
			}

			count = 0;
			while((count < cutOffNum)&&(minB<GREY_LEVEL)){
				count += blueHist[minB];
				minB++;
			}

			count = 0;
			while((count < cutOffNum)&&(maxB>0)){
				count += blueHist[maxB];
				maxB--;
			}


			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					int newRed;
					int newGreen;
					int newBlue;
					newRed = (int) ((red - minR)*(GREY_LEVEL-1)/(maxR - minR));
					newGreen = (int) ((green - minG)*(GREY_LEVEL-1)/(maxG - minG));
					newBlue = (int) ((blue - minB)*(GREY_LEVEL-1)/(maxB - minB));

					if (newRed>(GREY_LEVEL-1)){newRed = GREY_LEVEL-1;}
					if (newRed<0){newRed = 0;}
					if (newGreen>(GREY_LEVEL-1)){newGreen = GREY_LEVEL-1;}
					if (newGreen<0){newGreen = 0;}
					if (newBlue>(GREY_LEVEL-1)){newBlue = GREY_LEVEL-1;}
					if (newBlue<0){newBlue = 0;}



					int p = (newRed<<16) | (newGreen<<8) | newBlue;
					output.setRGB(x, y, p);
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if (newRed == intensity){
							redHistStretch[intensity]++;
						}
						if (newGreen == intensity){
							greenHistStretch[intensity]++;
						}
						if (newBlue == intensity){
							blueHistStretch[intensity]++;
						}
					}

				}
			target.resetImage(output);



			for (int j=0; j<GREY_LEVEL; j++){

				redHistStretch[j] = redHistStretch[j]/3;
				greenHistStretch[j] = greenHistStretch[j]/3;
				blueHistStretch[j] = blueHistStretch[j]/3;

			}

			plot.setHist(redHistStretch, greenHistStretch, blueHistStretch);
        }


        if (((Button) e.getSource()).getLabel().contentEquals("Histogram Equalization")) {
            //implement algorithm
            System.out.println("Histogram equalization");
			int red=0, green=0, blue=0;
			int redHist[] = new int[GREY_LEVEL];
			int greenHist[] = new int[GREY_LEVEL];
			int blueHist[] = new int[GREY_LEVEL];
			int redEqualHist[] = new int[GREY_LEVEL];
			int greenEqualHist[] = new int[GREY_LEVEL];
			int blueEqualHist[] = new int[GREY_LEVEL];
			float redPDF[] = new float[GREY_LEVEL];
			float greenPDF[] = new float[GREY_LEVEL];
			float bluePDF[] = new float[GREY_LEVEL];
			float redCDF[] = new float[GREY_LEVEL];
			float greenCDF[] = new float[GREY_LEVEL];
			float blueCDF[] = new float[GREY_LEVEL];
			BufferedImage output;
			output = input;
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if ((int)red == intensity){
							redHist[intensity]++;
						}
						if ((int)green == intensity){
							greenHist[intensity]++;
						}
						if ((int)blue == intensity){
							blueHist[intensity]++;
						}
					}

				}

			for (int j=0; j<GREY_LEVEL; j++){

				redPDF[j] = (float)redHist[j]/(width * height);
				greenPDF[j] = (float)greenHist[j]/(width * height);
				bluePDF[j] = (float)blueHist[j]/(width * height);
				System.out.println(redPDF[j]);

			}
			redCDF[0] = redPDF[0];
			for (int j=1; j<GREY_LEVEL; j++){

				redCDF[j] = redCDF[j-1] + redPDF[j];
				System.out.println(j+ " "+ redCDF[j]);
			}

			greenCDF[0] = greenPDF[0];
			for (int j=1; j<GREY_LEVEL; j++){

				greenCDF[j] = greenCDF[j-1] + greenPDF[j];
				System.out.println(j+ " "+ greenCDF[j]);
			}

			blueCDF[0] = bluePDF[0];
			for (int j=1; j<GREY_LEVEL; j++){

				blueCDF[j] = blueCDF[j-1] + bluePDF[j];
				System.out.println(j+ " "+ blueCDF[j]);
			}

			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					int newRed;
					int newGreen;
					int newBlue;
					newRed = (int)(redCDF[red] * GREY_LEVEL);
					newGreen = (int)(greenCDF[green] * GREY_LEVEL);
					newBlue = (int)(blueCDF[blue] * GREY_LEVEL);

					if (newRed>(GREY_LEVEL-1)){newRed = GREY_LEVEL-1;}
					if (newRed<0){newRed = 0;}
					if (newGreen>(GREY_LEVEL-1)){newGreen = GREY_LEVEL-1;}
					if (newGreen<0){newGreen = 0;}
					if (newBlue>(GREY_LEVEL-1)){newBlue = GREY_LEVEL-1;}
					if (newBlue<0){newBlue = 0;}
					int p = (newRed<<16) | (newGreen<<8) | newBlue;
					output.setRGB(x, y, p);
				}
			target.resetImage(output);


			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(input.getRGB(x, y));
					red = clr.getRed();
					green = clr.getGreen();
					blue = clr.getBlue();
					for (int intensity=0; intensity<GREY_LEVEL; intensity++ ){
						if ((int)red == intensity){
							redEqualHist[intensity]++;
						}
						if ((int)green == intensity){
							greenEqualHist[intensity]++;
						}
						if ((int)blue == intensity){
							blueEqualHist[intensity]++;
						}
					}

				}
			for (int j=0; j<GREY_LEVEL; j++){

				redEqualHist[j] = (int)(redEqualHist[j]/3);
				greenEqualHist[j] = (int)(greenEqualHist[j]/3);
				blueEqualHist[j] = (int)(blueEqualHist[j]/3);

			}
			plot.setHist(redEqualHist, greenEqualHist, blueEqualHist);

        }


	}
	public static void main(String[] args) {
		new ImageHistogram(args.length==1 ? args[0] : "baboon.png");
	}
}

// Canvas for plotting histogram
class PlotCanvas extends Canvas {
	// lines for plotting axes and mean color locations
	LineSegment x_axis, y_axis;
	//Array of LineSegments redLine, greenLine, blueLine;
	LineSegment[] redLine = new LineSegment[255];
	LineSegment[] greenLine = new LineSegment[255];
	LineSegment[] blueLine = new LineSegment[255];
	//boolean showMean = false;
	boolean showSeg = false;

	public PlotCanvas() {
		x_axis = new LineSegment(Color.BLACK, -10, 0, 256+10, 0);
		y_axis = new LineSegment(Color.BLACK, 0, -10, 0, 200+10);
	}

	public void setHist(int[] redhist, int[] greenhist, int[] bluehist){

		for (int i = 0; i < 255; i++ ){
			redLine[i] = new LineSegment(Color.RED, i, redhist[i], i+1, redhist[i+1] );
			greenLine[i] = new LineSegment(Color.GREEN, i, greenhist[i], i+1, greenhist[i+1] );
			blueLine[i] = new LineSegment(Color.BLUE, i, bluehist[i], i+1, bluehist[i+1] );
			showSeg = true;
		}
		repaint();


	}
	// redraw the canvas
	public void paint(Graphics g) {
		// draw axis
		int xoffset = (getWidth() - 256) / 2;
		int yoffset = (getHeight() - 200) / 2;
		x_axis.draw(g, xoffset, yoffset, getHeight());
		y_axis.draw(g, xoffset, yoffset, getHeight());
		if (showSeg) {
			for (int i = 0; i < 255; i++ ){
				redLine[i].draw(g, xoffset, yoffset, getHeight());
				greenLine[i].draw(g, xoffset, yoffset, getHeight());
				blueLine[i].draw(g, xoffset, yoffset, getHeight());
			}
		}
	}
}

// LineSegment class defines line segments to be plotted
class LineSegment {
	// location and color of the line segment
	int x0, y0, x1, y1;
	Color color;
	// Constructor
	public LineSegment(Color clr, int x0, int y0, int x1, int y1) {
		color = clr;
		this.x0 = x0; this.x1 = x1;
		this.y0 = y0; this.y1 = y1;
	}
	public void draw(Graphics g, int xoffset, int yoffset, int height) {
		g.setColor(color);
		g.drawLine(x0+xoffset, height-y0-yoffset, x1+xoffset, height-y1-yoffset);
	}
}
