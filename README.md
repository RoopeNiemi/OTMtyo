# Pacman

Helsingin yliopiston Ohjelmistotekniikan menetelmät -kurssia varten tehty sovellus.

### Dokumentaatio
[Vaatimusmäärittely](https://github.com/RoopeNiemi/OTMtyo/tree/master/dokumentaatio/Vaatimusmäärittely.md)

[Arkkitehtuuri](https://github.com/RoopeNiemi/OTMtyo/tree/master/dokumentaatio/arkkitehtuuri.md)

[Työaikakirjanpito](https://github.com/RoopeNiemi/OTMtyo/tree/master/dokumentaatio/työaikakirjanpito.md)


### Releaset
[Viikko 5](https://github.com/RoopeNiemi/OTMtyo/releases/tag/v1.0)

## Komentorivitoiminnot

### Testaus
Testit suoritetaan komennolla 

    mvn test
    
Testikattavuusraportti luodaan komennolla

    mvn jacoco:report
    
Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto target/site/jacoco/index.html

### Checkstyle
Tiedostoon checkstyle.xml määrittelemät tarkistukset suoritetaan komennolla

    mvn jxr:jxr checkstyle:checkstyle
    
    
### Suoritettavan jarin generointi
Komento

    mvn package

generoi hakemistoon target suoritettavan jar-tiedoston Pacman-1.0-SNAPSHOT.jar
