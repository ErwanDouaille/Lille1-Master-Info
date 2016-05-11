package event;

import java.util.EventObject;
import java.util.Map.Entry;

import oneDollarRecognizer.OneDollarRecognizer;
import oneDollarRecognizer.TemplateManager;
import mygeom.BlobQueue;
import mygeom.Point2;
import mygeom.Vector2;
import widget.MTComponent;

public class GestureAnalyzer {
	
	private InternalGestureState igs;

	public void analyze(MTComponent component, BlobQueue blobqueue, String state, Integer id, Point2 p){
		if(state.equals("add"))
			this.add( component, blobqueue, id, p);
		else if(state.equals("update"))
			this.update( component, blobqueue, id, p);
		else if(state.equals("remove"))
			this.remove( component, blobqueue, id, p);			
	}

	public void add(MTComponent component, BlobQueue blob, int id, Point2 p) {
		this.igs = new InternalGestureState(component);
		if(blob.getCursor().size()==1){
			this.igs.motionTranslateBegin(new Vector2(p.getX(), p.getY()));		
		} else if(blob.getCursor().size()==2){
			Point2 p1 = p;
			for (Entry<Integer, mygeom.Path> entry :blob.getCursor().entrySet()) {
					if(entry.getKey()!=id)
						p1 = entry.getValue().liste.get(entry.getValue().liste.size()-1);
			}	
			this.igs.motionTRS(p1.asVector2(), p.asVector2());
		}
	}

	public void update(MTComponent key, BlobQueue blob, int id, Point2 p) {	
		if(blob.getCursor().size()==1){
			this.igs.motionTranslateUpdate(new Vector2(p.getX(), p.getY()));
			Vector2 v = this.igs.computeTranslation();
			key.fireSRTPerformed(new SRTEvent(key, v, 0.0, 1.0));			
		} else if(blob.getCursor().size()==2){
			Point2 p1 = p;
			for (Entry<Integer, mygeom.Path> entry :blob.getCursor().entrySet()) {
					if(entry.getKey()!=id)
						p1 = entry.getValue().liste.get(entry.getValue().liste.size()-1);
			}	
			this.igs.motionTRSUpdate(p.asVector2(), p1.asVector2());	
			key.fireSRTPerformed(new SRTEvent(
					key, 
					this.igs.computeTRSTranslation(), 
					this.igs.computeTRSRotation(), 
					this.igs.computeTRSScale()));
		}
	}

	public void remove(MTComponent key, BlobQueue blob, int id, Point2 p) {
		GestureEvent event = new GestureEvent(key, null, 0);
		if(blob.getCursor().get(id).getListe().size()>5){
			OneDollarRecognizer one = this.igs.getOneDollarRecognizer();
			TemplateManager manager = new TemplateManager("data/gestures.xml");			
			event = one.recognize(key, blob.getCursor().get(id).getListe(), manager.getTemplates());
			event.setDoigt(blob.getCursor().size());
			key.fireGesturePerformed((EventObject)event);				
		}
	}
	

	
}
