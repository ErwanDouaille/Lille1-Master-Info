package event;

import java.util.EventListener;
import java.util.EventObject;

public interface DiscreteEventListener extends EventListener {
	
	public void gesturePerformed(EventObject event);

}
