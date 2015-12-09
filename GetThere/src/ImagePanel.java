import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class ImagePanel extends JPanel 

{
	private static final int CircleDiam = 10;
	BufferedImage image;
	double scale;
	GeneralPath path;
	Node startNode;
	Node endNode;
	private EndUserGUI gui;
	

	public ImagePanel(EndUserGUI gui)
	{

		this.gui = gui;
		scale = 1.0;
		setBackground(new Color(74, 1, 1));


	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
			int w = getWidth();
			int h = getHeight();
			
			if(image != null){
				int imageWidth = image.getWidth();
				int imageHeight = image.getHeight();
				double x = (w - scale * imageWidth)/2;
				double y = (h - scale * imageHeight)/2;
				AffineTransform at;
				at = AffineTransform.getTranslateInstance(x,y);
				at.scale(scale, scale);
				g2.drawRenderedImage(image, at);
			

			if(path != null){
				path.transform(at);
				g2.setColor(Color.BLACK);
				g2.draw(path);
				g2.setStroke(new BasicStroke(2));
				g2.draw(path);
				g2.setColor(Color.BLUE);
				g2.draw(path);
			}

			if(startNode != null && gui.getCurrentlyShownMap().toString() == startNode.getMapName()){
				Point2D before1 = new Point(), after1 = new Point(), before2 = new Point(), after2 = new Point();

				before1.setLocation(startNode.getX()-(CircleDiam+3)/2, startNode.getY()-(CircleDiam+3)/2);
				before2.setLocation(startNode.getX()-CircleDiam/2, startNode.getY()-CircleDiam/2);
				at.transform(before1, after1);
				at.transform(before2, after2);
				g.setColor(Color.BLACK);
				g.fillOval((int)after1.getX(), (int)after1.getY(), CircleDiam+3, CircleDiam+3);
				g.setColor(Color.GREEN);
				g.fillOval((int)after2.getX(), (int)after2.getY(), CircleDiam, CircleDiam);
			}

			if(endNode != null && gui.getCurrentlyShownMap().toString() == endNode.getMapName()){
				Point2D before1 = new Point(), after1 = new Point(), before2 = new Point(), after2 = new Point();

				before1.setLocation(endNode.getX()-(CircleDiam+3)/2, endNode.getY()-(CircleDiam+3)/2);
				before2.setLocation(endNode.getX()-CircleDiam/2, endNode.getY()-CircleDiam/2);
				at.transform(before1, after1);
				at.transform(before2, after2);
				g.setColor(Color.BLACK);
				g.fillOval((int)after1.getX(), (int)after1.getY(), CircleDiam+3, CircleDiam+3);
				g.setColor(Color.RED);
				g.fillOval((int)after2.getX(), (int)after2.getY(), CircleDiam, CircleDiam);
			}
		}
	}

	public BufferedImage getImage(){
		return this.image;
	}

	/**
	 * For the scroll pane.
	 */
	public Dimension getPreferredSize()
	{	int w, h;
	if(image != null){
		w = (int)(scale * image.getWidth());
		h = (int)(scale * image.getHeight());
	}
	else{
		w = 0;
		h = 0;
	}
	return new Dimension(w, h);
	}


	public void setScale(double s)
	{
		if(this.scale != s){
			this.scale = s;     
			revalidate();      // update the scroll pane
			repaint();
		}

	}


	public double getScale(){
		return this.scale;
	}

	public void setImage(ImageIcon img){
		Image im = img.getImage();
		BufferedImage buffered = convertToBufferedImage(im);
		if(this.image == null){
			if(img != null){

				this.image = buffered;
				revalidate();
				repaint();
			}

		}
		else if(!(bufferedImagesEqual(this.image, buffered))){
			this.image = buffered;
			revalidate();
			repaint();
		}
	}

	public void setPath(GeneralPath path){
		if(this.path == null){
			if(path != null){
				this.path = path;
				revalidate();
				repaint();
			}
			else{
				return;
			}
		}
		else if(path == null){
			this.path = null;
			revalidate();
			repaint();
			return;
		}
		else if(!(this.path.equals(path))){
			this.path = path;
			revalidate();
			repaint();
		}
	}

	public void setStartNode(Node startNode){
		if(this.startNode == null){
			if(startNode != null){
				this.startNode = startNode;
				revalidate();
				repaint();
			}
			else{
				return;
			}
		}
		else if(startNode == null){
			this.startNode = null;
			revalidate();
			repaint();
			return;
		}
		else if(!(this.startNode.equals(startNode))){
			this.startNode = startNode;
			revalidate();
			repaint();
		}
	}

	public GeneralPath getPath(){
		return this.path;
	}

	public Node getStartNode(){
		return this.startNode;
	}

	public Node getEndNode(){
		return this.endNode;
	}

	public void setEndNode(Node endNode){
		if (this.endNode == null){
			if(endNode != null){
				this.endNode = endNode;
				revalidate();
				repaint();
			}
			else{
				return;
			}
		}
		else if(endNode == null){
			this.endNode = null;
			revalidate();
			repaint();
			return;
		}
		else if(!(this.endNode.equals(endNode))){
			this.endNode = endNode;
			revalidate();
			repaint();
		}


	}

	public static BufferedImage convertToBufferedImage(Image image)
	{
		BufferedImage newImage = new BufferedImage(
				image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
			for (int x = 0; x < img1.getWidth(); x++) {
				for (int y = 0; y < img1.getHeight(); y++) {
					if (img1.getRGB(x, y) != img2.getRGB(x, y))
						return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}





} 