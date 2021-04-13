package theWitness;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface ISaveHandler {
	
	public void save(String filename, GameCollection games) throws FileNotFoundException, URISyntaxException;
	
	public GameCollection load(String filename) throws FileNotFoundException, URISyntaxException;
	
}
