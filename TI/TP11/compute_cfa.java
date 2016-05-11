import ij.*;
import ij.plugin.filter.*;
import ij.process.*;
import ij.gui.*;

public class compute_cfa implements PlugInFilter {

	ImagePlus imp;	// Fen�tre contenant l'image de r�f�rence
	int width;		// Largeur de la fen�tre
	int height;		// Hauteur de la fen�tre

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return compute_cfa.DOES_RGB;
	}

	public void run(ImageProcessor ip) {

		// Lecture des dimensions de la fen�tre
		width = imp.getWidth();
		height = imp.getHeight();

		// Dispositions possibles pour le CFA
		String[] orders = {"R-G-R", "B-G-B", "G-R-G", "G-B-G"};

		// D�finition de l'interface
		GenericDialog dia = new GenericDialog("G�n�ration de l'image CFA...", IJ.getInstance());
		dia.addChoice("D�but de premi�re ligne :", orders, orders[2]);
		dia.showDialog();

		// Lecture de la r�ponse de l'utilisateur
		if (dia.wasCanceled()) return;
		int order = dia.getNextChoiceIndex();

		// G�n�ration de l'image CFA
		ImageProcessor cfa = this.cfa(order);
		this.imp = new ImagePlus("calcul cfa", cfa);
		imp.show();

		ImageProcessor  r = cfa_samples(cfa ,0);
		ImageProcessor  g = cfa_samples(cfa ,1);
		ImageProcessor  b = cfa_samples(cfa ,2);

		float[] hrb = {0.25f,0.5f,0.25f, 0.5f,1.0f,0.5f, 0.25f,0.5f,0.25f};// RED and BLUE
		float[] hg ={0.0f,0.25f,0.0f, 0.25f,1.0f,0.25f,0.0f,0.25f,0.0f	}; // GREEN


		Convolver convolve = new Convolver();
		convolve.setNormalize(false);
		convolve.convolve(r,hrb,3,3);		
		convolve.convolve(g,hg,3,3);		
		convolve.convolve(b,hrb,3,3);
		
		// Calcul des �chantillons de chaque composante de l'image CFA
		ImageStack samples_stack = imp.createEmptyStack();
		samples_stack.addSlice("rouge", r);	// Composante R
		samples_stack.addSlice("vert", g);// Composante G
		samples_stack.addSlice("bleu", b);	// Composante B

		ImagePlus cfa_samples_imp = imp.createImagePlus();
		cfa_samples_imp.setStack("�chantillons couleur CFA", samples_stack);
		cfa_samples_imp.show();

	}

	public ImageProcessor cfa(int row_order) {
		// Image couleur de r�f�rence et ses dimensions
		ImageProcessor ip = imp.getProcessor();
		width = imp.getWidth();
		height = imp.getHeight();

		int pixel_value = 0;	// Valeur du pixel source
		ImageProcessor cfa_ip = new ByteProcessor(width,height);	// Image CFA g�n�r�e

		// �chantillons G
		for (int y=0; y<height; y+=2) {
			for (int x=0; x<width; x+=2) {
				pixel_value = ip.getPixel(x,y);
				int green = (int)(pixel_value & 0x00ff00)>>8;
			cfa_ip.putPixel(x,y,green);
			}
		}
		for (int y=1; y<height; y+=2) {
			for (int x=1; x<width; x+=2) {
				pixel_value = ip.getPixel(x,y);
				int green = (int)(pixel_value & 0x00ff00)>>8;
			cfa_ip.putPixel(x,y,green);
			}
		}
		// �chantillons R
		for (int y=0; y<height; y+=2) {
			for (int x=1; x<width; x+=2) {
				pixel_value = ip.getPixel(x,y);
				int red = (int)(pixel_value & 0xff0000)>>16;
			cfa_ip.putPixel(x,y,red);
			}
		}
		// �chantillons B
		for (int y=1; y<height; y+=2) {
			for (int x=0; x<width; x+=2) {
				pixel_value = ip.getPixel(x,y);
				int blue = (int)(pixel_value & 0x0000ff);
				cfa_ip.putPixel(x,y,blue);
			}
		}

		return cfa_ip;
	}

	public ImageProcessor  cfa_samples(ImageProcessor imp, int row_order) {
		width = imp.getWidth();
		height = imp.getHeight();
		ImageProcessor composante_imp = new ByteProcessor(width,height);
		for(int j=0; j<height;j++){
			for(int i=0; i<width ;i++){
				if(row_order==0)
					if(j%2==0 && i%2==1)
						composante_imp.putPixel(i,j,imp.getPixel(i,j));
				if(row_order==1)
					if((j+i)%2==0)
						composante_imp.putPixel(i,j,imp.getPixel(i,j));
				if(row_order==2)
					if(j%2==1 && i%2==0)
						composante_imp.putPixel(i,j,imp.getPixel(i,j));	
			}			
		}		
		return composante_imp;
	}
}
