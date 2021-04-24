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
- Todimensjonal liste over alle Tile objektene som Grid inneholder **(List(List(tile)) grid)**.

I tillegg implementerer klassen Iterable(Tile), og har en implementasjon av iterator() som går igjennom hvert Tile objekt i Grid rad for rad.

