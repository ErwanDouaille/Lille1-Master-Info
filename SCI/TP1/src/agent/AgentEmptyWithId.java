package agent;

import java.awt.Color;

import environnement.Environnement;

public class AgentEmptyWithId extends AgentEmpty {

	int value = 0;
	
	public AgentEmptyWithId(Environnement environnement) {
		super(environnement);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isEmpty() {
		return true;
	}
	
	public String toString() {
		return "--" + this.value + "--";
	}
	
	public Color color() {
		switch (this.value) {
		case 1:
			return new Color(255, 255, 255);
		case 2:
			return new Color(200, 255, 200);
		case 3:
			return new Color(155, 255, 155);
		case 4:
			return new Color(100, 200, 100);
		case 5:
			return new Color(50, 155, 50);
		case 6:
			return new Color(30, 100, 30);
		case 7:
			return new Color(10, 50, 10);
		default:
			return Color.BLACK;
		}
	}	
}
