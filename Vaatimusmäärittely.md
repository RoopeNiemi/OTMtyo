# Vaatimusmäärittely

## Sovelluksen tarkoitus
Sovellus on oma versio Pacman pelistä. Pelattavuuden on tarkoitus olla samankaltainen kuin oikeassa Pacman-pelissä,
kuitenkin mm. hirviöiden tekoäly poikkeaa alkuperäisestä jonkin verran. 

## Suunnitellut toiminnallisuudet
Pelissä pelaaja pystyy liikkumaan 2D-labyrintissä, tarkoituksenaan syödä kaikki pisteet kentällä, hirviöitä välttäen.
Liikkuminen tapahtuu nuolinäppäimillä siten, että hahmo liikkuu annettuun suuntaan jos se on mahdollista, niin kauan kunnes
tulee seinä vastaan tai jonossa oleva uusi suunta on mahdollista toteuttaa. 

Pelissä on tietty määrä erikoispisteitä pisteiden seassa. Pelaajan syötyä hedelmän hirviöt joutuvat hetkellisesti paniikin valtaan
ja pakenevat pelaajaa. Paniikkivaiheessa hirviöiden nopeus puolittuu, ja pelaaja on sen keston ajan kuolematon. Tällöin pelaajan on mahdollista tilapäisesti "syödä" hirviö, jolloin hirviö palautuu lähtöruutuunsa. 

Pelissä on 4 vihollishirviötä, jotka poikkeavat hieman toisistaan käytöksen osalta. Toiminta tulee olemaan seuraavankaltainen:

  * Punainen hirviö: Jahtaa pelaajaa jatkuvasti
  * Pinkki hirviö: hakee reittiä 2 ruutua pelaajan eteen jos mahdollista, muuten 4 ruutua pelaajan taakse
  * Oranssi hirviö: Jos etäisyys pelaajasta yli tietyn määrän, hakee reittiä pelaajan luo. Muuten hakee reitin oikeaan alakulmaan, tai jos on jos oikeassa alakulmassa, oikeaan yläkulmaan
  * Sininen hirviö: Hakee reitin joko pelaajan eteen kuten pinkki hirviö, tai pelaajan luo, tai oikeaan alakulmaan. 

Lisäksi hirviöillä on "scatter" vaihe, jossa hakevat reitin jokaiseen kentän kulmaan. Tämä vaihe tulee olemaan lyhyt, ja se toistuu tietyin väliajoin.


## Jatkokehitysideoita
Pelin perustoiminnallisuuden toimiessa hyvin peliä voidaan täydentää mm. high score listalla, joka näyttäisi esim.
kyseisen kentän suurimman saavutetun pistemäärän. Lista pidettäisiin joko tietokannassa tai tekstitiedostossa. 







