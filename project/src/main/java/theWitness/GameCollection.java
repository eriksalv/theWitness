package theWitness;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class GameCollection {	
	private String name;
	private Map<Integer,Game> games; //Integer representerer nivået til Game-objektet
	private Map<Integer,Boolean> isGamesWon;
	private int levelCount = 1; //OBS: levelCount starter på 1.
	
	public GameCollection(String s, Game... games) { //tar inn et navn på collection og varargs for games i collection
		this.games=new TreeMap<Integer,Game>(); //TreeMap for å bevare rekkefølge
		this.isGamesWon=new TreeMap<Integer,Boolean>();
		Stream.of(games).forEach(game -> {
			game.setLevel(levelCount);
			this.games.put(levelCount, game);
			this.isGamesWon.put(levelCount, game.getIsGameWon());
			levelCount++;
		});
		this.games.values().forEach(game -> game.addGameCollection(this)); //legger til denne GameCollection i hvert game sin GameCollection felt
		this.name=name;
	}
	
	public static final GameCollection newGame() throws FileNotFoundException, URISyntaxException{ //lager en ny gamecollection som består av de standard, uendra nivåene i config mappa.
		GameCollection newGame = new GameCollection("new game");
		List<LevelEnumerator> levelList = Arrays.asList(LevelEnumerator.values());
		for (int i=2;i<levelList.size();i++) {
			newGame.addGame(levelList.get(i).startingTiles(), i-1);
		}
		return newGame;
	}
	
	public int getGameIndex() {
		return isGamesWon.keySet().stream()
				.filter(level -> isGamesWon.get(level)==false)
				.findFirst().orElse(1); //finner det første nivået som ikke er vunnet enda, og hvis ikke det fins, returner det første nivået
	}
	
	public Map<Integer,Boolean> getIsGamesWon() {
		return isGamesWon;
	}
	
	public Map<Integer,Game> getGames() {
		return games;
	}
	
	public void addGame(Game game, int level) {
		if (level>=levelCount+1) { //skal ikke være mulig å legge til et game med 2 eller mer level høyere enn det siste i games lista
			throw new IllegalStateException("Only " + levelCount + " levels exists");
		}
		if (games.keySet().contains(level)) {
			games.put(level, game); //overskriver game som allerede eksisterer
			isGamesWon.put(level, game.getIsGameWon());
			game.addGameCollection(this);
			game.setLevel(level);
		}
		else {
			addGame(game, game.getIsGameWon());
		}
	}
	public void addGame(Game game, boolean isGameWon) {
		games.put(levelCount, game); //legger til et nytt game eller overskriver et som allerede eksisterer (med samme LEVEL)
		isGamesWon.put(levelCount, isGameWon);
		
		game.addGameCollection(this);
		game.setLevel(levelCount);
		levelCount++;
	}
	public void removeGame(int level) { 
		if (!games.keySet().contains(level)) {
			throw new IllegalStateException("Cannot remove game that does not exist");
		} else if (games.size()==1) {
			throw new IllegalStateException("Cannot remove game when it is the only game in collection");
		} else if (!isGamesWon.get(level)) {
			throw new IllegalStateException("Cannot remove game that is not won");
		}
		games.get(level).removeGameCollection();
		
		//erstatter alle key-entry par der verdien til key er større eller lik verdien til level med neste key-verdi i map.  
		games.keySet().stream().filter(key -> key.intValue()>level).forEach(key -> games.replace(key-1, games.get(key)));
		isGamesWon.keySet().stream().filter(key -> key.intValue()>level).forEach(key -> isGamesWon.replace(key-1, isGamesWon.get(key)));
		//fjerner siste key-entry par.
		games.remove(games.size());
		isGamesWon.remove(isGamesWon.size());
		
		games.values().stream().filter(g -> g.getLevel()>level).forEach(g -> g.setLevel(g.getLevel()-1)); //minker nivået med 1 til alle games etter gamet som ble fjernet 
		levelCount--;
	}
	public void gameStateChanged(Game game,boolean isGameWon) {
		isGamesWon.put(game.getLevel(), isGameWon);
	}
	
	public boolean hasNextLevel(int currentLevel) {
		return getIsGamesWon().get(currentLevel) && getIsGamesWon().containsKey(currentLevel+1);//skjekker at isGameWon er satt til true før neste game
	}
	
	public boolean hasPrevLevel(int currentLevel) {
		return getIsGamesWon().containsKey(currentLevel-1);
	}
	@Override
	public String toString() {
		return name;
	}
}
