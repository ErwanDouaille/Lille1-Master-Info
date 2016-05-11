package mygeom;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map.Entry;

public class BlobQueue {
	
    public HashMap<Integer,Path> cursor;
    
    public BlobQueue() {
            cursor=new HashMap<Integer,Path>();
    }
    
    public HashMap<Integer,Path> getCursor(){
    	return this.cursor;
    }
    
	public synchronized void addCursor(int id, Point2 p){
		Path path = new Path();
		path.add(p);
		this.cursor.put(id, path);
	}
	
	public synchronized void updateCursor(int id, Point2 p){
		this.cursor.get(id).add(p);		
	}
	
	public synchronized void removeCursor(int id, Point2 p){
		this.cursor.get(id).clear();
		this.cursor.remove(id);		
	}
	
    public void draw(Graphics2D g, boolean bool){	
    		for(Entry<Integer, Path> entry : this.cursor.entrySet()) {
    		    Path valeur = entry.getValue();
    		    valeur.draw(g, entry.getKey().toString(), bool);
    		}
	}
    
	public boolean checkId(int id) {
		return this.cursor.containsKey(id);
	}
    

}
