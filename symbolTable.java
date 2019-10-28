import java.util.*;

public class symbolTable {
	
	ArrayList<Hashtable<String, tableObject>> list = new ArrayList<Hashtable<String, tableObject>>();
	
	public symbolTable(){//creating a new table into the array for new scope
		list.add(new Hashtable<String, tableObject>());
	}
	
	public void release(){//leaving the current scope, may need to save it elsewhere depending on how java saves or holds values
		list.remove(list.size()-1);
	}
	
	public tableObject find(String thing) {//finds the closest tableObject based on scope
		tableObject found = null;

		//iterates through arraylist from end to first looking for object, if it finds returns at closest to current scope
		for(int i = list.size()-1; i >= 0; i--) {
			found = list.get(i).get(thing);
			if(found != null) {
				i = -1;
			}
		}
		
		if(found == null) {
			System.out.println("ERROR: There is no symbol table reference for: " + thing);
			System.exit(1);
		}
		
		return found;
	}
	
	public void add(String iden, Operation typeNew, String val) {//adds a tableObject to the current scope
		tableObject obj = new tableObject(iden, typeNew, val);
		list.get(list.size()-1).put(iden, obj);
	}
}
