package event;

import java.util.EventObject;
import oneDollarRecognizer.Template;
import widget.MTComponent;

public class GestureEvent extends EventObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Template template;
	private double score;
	private Integer nbDoigt;

	public GestureEvent(Object source) {
		super(source);
	} 
	
	public GestureEvent(MTComponent key, Template template, double score){
		super(key);
		this.template=template;
		this.score=score;
	}
	
	public String getTemplateName() {
		return template.getName();
	}
	
	public double getScore() {
		return score;
	}
	
	public Template getTemplate(){
		return this.template;
	}

	public void setDoigt(int nbDoigt) {
		this.nbDoigt = nbDoigt;
	}

	public Integer getDoigt() {
		return this.nbDoigt;
	}
    
}
