# Testausdokumentti

### Yksikkö- ja integraatiotestaus

Testit noudattavat projektissa samaa pakkausrakennetta kuin ns. normaaliluokat. Ohjelman toiminnan kannalta oleellisimmat testit ovat luokan GameLogic testit, joissa testataan ohjelman osien toimimista peliympäristössä ja niiden vuorovaikutusta toisten osien kanssa. Muita luokkia varten on tehty yksikkötestejä. 


#### HighScoreDao
HighScoreDao-luokka käyttää erillistä tietokantaa nimeltä "test.db". Testeissä testataan tietokannasta hakua ja 
uuden high scoren päivittämistä tietokantaan.

#### Testauskattavuus

Sovelluksen testauksen rivikattavuus on 84%, ja haaraumakattavuus 67%. Tähän ei lasketa mukaan käyttöliittymää.

![alt text](https://github.com/RoopeNiemi/OTMtyo/blob/master/dokumentaatio/kuvat/testikattavuus.png)
