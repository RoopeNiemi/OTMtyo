# Vaatimusmäärittely

## Sovelluksen tarkoitus
Sovellus on oma versio Pacman pelistä. Pelattavuuden on tarkoitus olla samankaltainen kuin oikeassa Pacman-pelissä,
kuitenkin mm. hirviöiden tekoäly poikkeaa alkuperäisestä jonkin verran. 

## Suunnitellut toiminnallisuudet
Pelissä pelaaja pystyy liikkumaan 2D-labyrintissä, tarkoituksenaan syödä kaikki pisteet kentällä, hirviöitä välttäen.
Liikkuminen tapahtuu nuolinäppäimillä siten, että hahmo liikkuu annettuun suuntaan jos se on mahdollista, niin kauan kunnes
tulee seinä vastaan tai jonossa oleva uusi suunta on mahdollista toteuttaa. 

Pelissä on tietty määrä erikoispisteitä pisteiden seassa. Pelaajan syötyä hedelmän hirviöt joutuvat 5 sekunnin ajaksi paniikin valtaan ja pakenevat pelaajaa. Paniikkivaiheessa hirviöiden nopeus puolittuu, ja pelaaja on sen keston ajan kuolematon. Tällöin pelaajan on mahdollista tilapäisesti "syödä" hirviö, jolloin hirviö palautuu lähtöruutuunsa. 

Pelissä on 4 vihollishirviötä, jotka poikkeavat hieman toisistaan käytöksen osalta. Toiminta tulee olemaan seuraavankaltainen:

  * Punainen hirviö: Jahtaa pelaajaa jatkuvasti
  * Pinkki hirviö: hakee reittiä 2 ruutua pelaajan eteen jos mahdollista, muuten 4 ruutua pelaajan taakse
  * Oranssi hirviö: Jos etäisyys pelaajasta yli 4 ruutua, hakee reittiä pelaajan luo. Muuten hakee reitin oikeaan alakulmaan, tai jos on jos oikeassa alakulmassa, oikeaan yläkulmaan
  * Sininen hirviö: Hakee reittiä ruutuun seuraavalla tavalla: Laskee vektorin punaisen hirviön ja pelaajan edessä olevan ruudun väliin ja tuplaa kyseisen vektorin pituuden. Vektorin päätepiste on kohderuutu. Jos vektorin päätepiste on yli kartan rajojen, se siirretään kartan reunalle. 

Hirviöillä on kaksi normaalia käyttäytymismallia: normaalivaihe (tai jahtaamisvaihe) kestää 20 sekuntia ja silloin hirviöt toimivat yllä kirjoitetulla tavalla. Lisäksi hirviöillä on 7 sekunnin hajautumisvaihe, jolloin ne hakevat reittiä jokainen eri kulmaan. Tämä vaihe voi toistua 4 kertaa kentän aikana. Kun hajautumisvaihe on toteutunut 4 kertaa kentän aikana, hirviöit pysyvät jahtaamisvaiheessa loppukentän ajan.









