package event;

import java.util.EventListener;
import java.util.EventObject;

public interface GestureEventListener extends EventListener {
	
	public void gesturePerformed(EventObject fooEvent);

}
