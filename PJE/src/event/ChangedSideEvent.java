package event;

import java.awt.AWTEvent;

public class ChangedSideEvent extends AWTEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean leftSide;
	
	public ChangedSideEvent(Object source,int id,boolean leftSide){
		super(source, id);
		this.leftSide=leftSide;
	}
	
	public boolean isLeftSide(){
		return this.leftSide;
	}

	public Integer getCursorId() {
		return super.getID();
	}

}