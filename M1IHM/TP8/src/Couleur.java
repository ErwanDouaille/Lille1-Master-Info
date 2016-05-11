/**
 * @author <a href="mailto:gery.casiez@lifl.fr">Gery Casiez</a>
 */

public class Couleur {
	private Integer red = 0;
	private Integer green = 0;
	private Integer blue = 0;
	Couleur(Integer r, Integer g, Integer b) {
			red = r; green = g; blue = b;
	}
	public Integer getR() {
		return red;
	}
	public Integer getG() {
		return green;
	}
	public Integer getB() {
		return blue;
	}
	public String toString() {
		return red + " " + green + " " + blue;
	}
}