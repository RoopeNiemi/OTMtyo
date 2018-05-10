# Testausdokumentti

Ohjelmaa on testattu sekä yksikkötestein että integraatiotestein.

### Yksikkö- ja integraatiotestaus

Testit noudattavat projektissa samaa pakkausrakennetta kuin ns. normaaliluokat. Testeissä testataan ohjelman 
kannalta oleellisia asioita, kuten hirviön reitinhakua, tilanteen päivittämistä, pelaajan liikkumista. 


#### HighScoreDao
HighScoreDao-luokka käyttää erillistä tietokantaa nimeltä "test.db". Testeissä testataan tietokannasta hakua ja 
uuden high scoren päivittämistä tietokantaan.

#### Testauskattavuus

Sovelluksen testauksen rivikattavuus on 84%, ja haaraumakattavuus 67%. Tähän ei lasketa mukaan käyttöliittymää.

![alt text](https://github.com/RoopeNiemi/OTMtyo/tree/master/dokumentaatio/kuvat/testikattavuus.png)
