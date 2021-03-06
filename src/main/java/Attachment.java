import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Attachment extends Item {
	

	public Attachment() {
		super();
	}
	
	public Attachment(int ID, String name, long weight) {
		this.ID = ID;
		this.name = name;
		this.weight = weight;
		
	}
	
	public String get_CSVline(Item parent) {
		String res = "";
		
		res += surr(this.ID);
		res += del;
		
		res += surr("PJ");
		res += del;
		
		res += surr(this.name);
		res += del;
		
		if(parent != null) {
			res += surr(((Message)parent).sender.emailAddress);
			res += del;
			
			res += surr(((Message)parent).sender.name);
			res += del;
			
			ArrayList<String> liste_destinataires = new ArrayList<String>();
			
			for(Person d : ((Message)parent).receivers) {
				liste_destinataires.add(d.name + " <" + d.emailAddress + ">");
			}
			
			res += surr(String.join(", ", liste_destinataires));
			res += del;
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			res += surr(df.format(((Message)parent).send_date));
			res += del;
			
			res += surr(this.weight);
			res += del;
			
			res += surr(write_smart_size(this.weight));
			res += del;
			
			res += surr(((Message)parent).email_weight);
			res += del;
			
			res += surr(write_smart_size(((Message)parent).email_weight));
			res += del;
			
			res += surr(((Message)parent).ID);
			res += del;
			
			res += surr(((Message)parent).name);
			res += del;
			
			
			
			ArrayList<String> liste_PJs = new ArrayList<String>();
			
			for(Item PJ : ((Message)parent).children) {
				liste_PJs.add(PJ.name);
			}
			
			res += surr(String.join(", ", liste_PJs));
		}
		else {
			res += del;
			res += del;
			res += del;
			res += del;
			
			res += surr(this.weight);
			res += del;
			
			res += surr(write_smart_size(this.weight));
			res += del;
			
			res += del;
			res += del;
			res += del;
			res += del;
			
		}
		
		res += del;
		
		return res;
	}

}
