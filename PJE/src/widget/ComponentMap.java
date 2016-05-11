package widget;

import java.util.HashMap;
import java.util.Map.Entry;

import event.DiscreteEvent;
import event.GestureAnalyzer;

import mygeom.BlobQueue;
import mygeom.Point2;

public class ComponentMap {

	private HashMap<MTComponent,BlobQueue> CMap; 
	private MTContainer container;
	private GestureAnalyzer gestureAnalyzer;
	
	public ComponentMap(MTContainer container){
		this.CMap=new HashMap<MTComponent,BlobQueue>();
		this.gestureAnalyzer = new GestureAnalyzer();
		this.container = container;
	}
	
	 public void addBlob(int id, Point2 p){
		 MTComponent component = this.container.whichIs(p);
		 if(component==null)
			 return;
		 BlobQueue blob = this.CMap.get(component);
		 if(blob==null)
			 blob = new BlobQueue();
		 blob.addCursor(id, p);
		 this.CMap.put(component, blob);
		 this.gestureAnalyzer.analyze(component, blob, "add", id, p);
		 component.fireDiscretePerformed(new DiscreteEvent(component));
	 }
	 
	 public void updateBlob(int id,Point2 p){
		 for (Entry<MTComponent,BlobQueue>entry : this.CMap.entrySet()) {
			BlobQueue blob=entry.getValue();
			if(blob.checkId(id)){
				blob.updateCursor(id, p);
				this.gestureAnalyzer.analyze(entry.getKey(), blob, "update", id, p);
				return;
			}
		}		 
	 }
	 public void removeBlob(int id,Point2 p){
		for (Entry<MTComponent,BlobQueue>entry : this.CMap.entrySet()) {
			BlobQueue blob=entry.getValue();
			if(blob.checkId(id)){			
				this.gestureAnalyzer.analyze(entry.getKey(), blob, "remove", id, p);
				blob.removeCursor(id, p);	
				return;
			}
		}
	 }
	
	 public HashMap<MTComponent,BlobQueue> getCmap(){
		 return this.CMap;
	 }
}