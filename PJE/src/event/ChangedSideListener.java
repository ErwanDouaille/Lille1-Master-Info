package event;

import java.util.EventListener;

public interface ChangedSideListener extends EventListener{
	public void changedSidePerformed(ChangedSideEvent evt);

}
