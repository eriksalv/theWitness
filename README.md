# Prosjekt-repo for TDT4100

Dette repoet inneholder et grunnoppsett for et prosjekt som bruker Java, JavaFX og JUnit5.

For å kode prosjektet, kan du enten importere det fra url-en til dette repoet i Eclipse, eller du kan forke det til et eget repo, som du deretter kan importere og bruke.
Sistnevnte gir mulighet til å bruke git for kodesynkronisering og versjonskontroll, og er absolutt anbefalt, spesielt om dere er to som jobber sammen på prosjektet.
Videoer for begge måter å importere repoet ligger på Blackboard.

# The Witness App

Dette prosjektet er et spill basert på puzzle-spillet The Witness (2016) av Jonathan Blow og Thekla inc.

# Regler

The Witness inneholder mange forskjellige regler (puzzle-typer), og jeg har valgt å bare implementere noen av de mest brukte.

### Svarte og hvite ruter

For å fullføre et brett med svarte og hvite ruter, må man "separere" de forskjellige fargene fra hverandre. Både linjen man beveger og kantene på brettet fungerer som vegger som skiller rutene fra hverandre. Merk at det **ikke** er noe krav om at rutene med lik farge må være gruppert sammen.

### Prikker

Når det er prikker (dots) på et brett, må man gå igjennom alle prikkene på brettet før man når målet for å fullføre brettet.

### Flere farger

Et brett som inneholder flere farger fungerer på akkurat samme måte som et brett med kun svarte å hvite ruter. Den eneste forskjellen er at det er flere farger som må separeres.

# Spill logikk

Logikken i spillet er fordelt over 5 klasser: Tile.java, Grid.java, Game.java, PathChecker.java og GameCollection.java

### Tile.java

Denne klassen er den mest grunnleggende, og beskriver hver individuelle rute/tile i et brett/game. Feltene som beskriver tilstanden til en rute er:

- Hva slags type ruten er **(char type)**. Enkelte ruter skal ha kollisjon, som er gitt ved hasCollision()-metoden.
- x- og y-koordinatene til ruten **(int x, y)**.
- Om ruten inneholder en prikk eller ikke (boolean containsDot).

### Grid.java

Grid beskriver kun tilstanden til et brett, og har ingen regler for hvordan spillet skal spilles. Dette gjør at denne klassen også kan brukes som en basis for andre rutenett-baserte spill. Tilstanden til Grid er gitt av følgende felter:

- Bredde og høyde for brettet **(int width, height)**.
- Todimensjonal liste over alle Tile objektene som Grid inneholder **(List\<List\<Tile\>\>) grid)**.

I tillegg implementerer klassen Iterable\<Tile\>, og har en implementasjon av iterator() som går igjennom hvert Tile objekt i Grid rad for rad.

### Game.java og PathChecker.java

Game.java arver fra Grid.java, og beskriver hvordan spillet fungerer. I tillegg til tilstanden som arves fra Grid har Game en del ekstra felter, hvor de viktigste er:

- **LinkedHashMap\<Tile,String\> moves** som inneholder rekkefølgen for trekkene man har tatt. Tile representerer hvilken rute som trekket førte til, og String refererer til hvilken retning som førte til korresponderende rute (Up, Down, Left, Right).
- informasjon om spillet er vunnet **(boolean isGameWon)** eller tapt **(boolean isGameOver)**.
- **int[] start** og **int[] goal** er start- og mål-koordinatene til et game. Trenger disse siden det bare er en enveisassosiasjon fra Grid til Tile, og at de kan bli overskrevet av andre ruter.

PathChecker er en hjelpeklasse med kun static metoder, som skjekker om man har gått gjennom brettet på riktig måte (at alle reglene er oppfylt). Det er to hovedmetoder i klassen:

- *checkDots(Game game)* som skjekker at man har gått igjennom alle prikkene. Dette kan lett skjekkes ved å gå igjennom brettet og skjekke om det finnes noen vanlige ruter (isLine()) som inneholder en prikk (getContainsDot()). 
- Metoden **checkColorsSeparated(Game game)** er litt mer komplisert. For det første finner den alle de fargede rutene på brettet, som sendes videre enkeltvis til en hjelpemetode  **findSurroundingTiles(Game game, Tile startingTile)** som tar utgangspunkt i koordinatene til den fargede ruten som sendes inn (startingTile) og går bruker fire while løkker til å bevege seg oppover, nedover, til venstre og til høyre helt fram til den neste ruten er en del av linjen man beveger (movedLine) eller til den når slutten av brettet. For hver rute som løkkene går igjennom kjøres det to while løkker til, som går i de to "motsatte" retningene av det den ytre while løkken går i. Alle tiles som løkkene går igjennom blir lagt til i et Set\<Tile\>, som til slutt utgjør hashmappet **LinkedHashMap\<Tile,Set\<Tile\>\> surroundingTilesList** med den hver fargede tile i brettet som en unik key, og surroundTiles-settet som tilhører ruten som value. Det eneste som gjenstår er å skjekke at alle settene som tilhører forskjellige farger er disjunkte. Merk at det ikke holder å skjekke om et set inneholder to forskjellige farger, fordi findSurroundingTiles-algoritmen finner ikke alle rutene rundt en tile i alle tilfeller. Dette problemet kunne antakeligvis ha blitt løst gjennom bruk av rekursjon i stedet for while-løkker.

### GameCollection.java

Meningen med GameCollection-klassen, er i hovedsak å ha en samling av Game-objekter, som lett kan brukes av selve applikasjonen til å representere en samling av nivåer/levels som man går igjennom etter hverandre. I tillegg inneholder klassen en map over hvilke nivåer i samlingen som har blitt vunnet før, som brukes i appen til å kun tillate å gå til neste nivå dersom man har fullført det man er på. Man kan derfor si at GameCollection fungerer som en observatør av Game-objekter. GameCollection tillater i i tillegg å slette nivåer man har fullført, og å legge til nye nivåer/erstatte eksisterende nivåer (sistnevnte er ikke brukt i appen).

# Filbehandling

Grensesnittet ISaveHandler implementeres av to klasser, SaveHandler.java og LevelEnumerator.java, og viser til klasser som håndterer filer på en eller annen måte.

### SaveHandler.java

SaveHandler gir støtte for å lagre et GameCollection-objekt til en fil, og laste filen til et GameCollection-objekt. save-filene lagres i resources-mappen. En konsekvens av dette er at det ikke bør være mulig å lagre nye save-filer utover de som ligger i mappen fra før. SaveHandler inneholder også en metode **getFilePathFromResource(String filename)** som hovedsakelig brukes av andre klasser til å finne filstien til en gitt mappe i resources-mappen. Det er også en ubrukt metode i klassen, **getSaveFiles()**, som gir ut en liste av alle save-filene. Meningen bak metoden var å bruke den til å få de ulike filnavnene som knapper i grensesnittet, slik at man ikke trenger å skrive filnavnet inn for å lagre/laste, men jeg rakk ikke å implementere dette. 

