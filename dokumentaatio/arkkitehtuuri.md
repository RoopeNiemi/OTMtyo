# Arkkitehtuuri

#### Käyttöliittymä
Käyttöliittymä koostuu yhdestä näkymästä, johon piirretään kaikki pelissä tapahtuvat asiat. Käyttöliittymäluokassa pyörii myös ajastin, joka päivittää pelitilannetta. Pelitilanteen päivittyessä käyttöliittymä hakee GameLogic luokan kautta piirrettävät asiat, ja piirtää ne oikeaan kohtaan näkymää. Uutta kenttää luotaessa käyttöliittymä hakee MapLoader-luokan kautta karttamatriisin tiedostosta map.txt. 

### Sovelluslogiikka
Sovelluslogiikkaluokan GameLogic kautta päivitetään pelitilannetta. Sen avulla päivitetään muun muassa pelaajan liikkumissuunta, päivitetään hirviöiden reitti, tarkistetaan pelaajan ja hirviön osuminen toisiinsa. Pelin alussa sen kautta luodaan pelissä tarvittavat oliot. 

GameState-luokka pitää kirjaa sellaisista tapahtumista kuten aktiivisten hirviöiden määrä ja pelaajan nykyiset pisteet. Lisäksi GameState-luokassa on tieto siitä onko nykyinen peli ohi pelaajan elämien loppumisen seurauksena, sekä siitä onko nykyinen kenttä ohi. GameState-luokan kautta ollaan myös yhteydessä HighScoreDao luokkaan.

HighScoreDao-luokan kautta haetaan nykyinen high score tietokannasta highscore.db. Vastaavasti pelin loputtua HighScoreDaon kautta päivitetään high score tietokantaan, jos pelaajan keräämät pisteet ovat korkeammat kuin vanha highscore.



#### Ohjelman luokkarakenne
![alt text](https://github.com/RoopeNiemi/OTMtyo/blob/master/dokumentaatio/kuvat/Luokkakaavio.jpg)

### Pelaajan liike ja tilanteen päivitys

Pelissä liikutaan nuolinäppäimien avulla. Pelaajan painaessa nuolinäppäintä suunta laitetaan jonoon, ja suunnanmuutos suoritetaan silloin kun se on mahdollista toteuttaa. Jos käännös uuteen suuntaan ei ole mahdollinen missään vaiheessa, pelaaja jatkaa vanhaan suuntaan kunnes osuu seinään.  
Sekvenssikaavio tapahtumista kun pelaaja painaa nuolinäppäintä
![alt text](https://github.com/RoopeNiemi/OTMtyo/blob/master/dokumentaatio/kuvat/pelaajanLiikkuminenSekvenssikaavio.png)

Pelitilanne päivitetään useita kertoja sekunnissa. Päivityksen yhteydessä liikutetaan kaikkia aktiivisia hahmoja sekä tarkistetaan pistetilanne. Hirviöiden liikkumisen yhteydessä tarkistetaan myös osuminen pelaajaan. 
Sekvenssikaavio tilanteen päivityksestä.
![alt text](https://github.com/RoopeNiemi/OTMtyo/blob/master/dokumentaatio/kuvat/tilanteenP%C3%A4ivitysSekvenssikaavio.png)


