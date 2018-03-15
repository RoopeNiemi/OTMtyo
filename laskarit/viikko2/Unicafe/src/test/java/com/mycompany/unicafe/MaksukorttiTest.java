package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void saldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void lisaaminenKasvattaaSaldoa() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }

    @Test
    public void saldoVaheneeOikein() {
        kortti.lataaRahaa(10);
        kortti.otaRahaa(10);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void saldoEiMuutuJosEiRahaaTarpeeksi() {
        kortti.otaRahaa(20);
        assertEquals(10, kortti.saldo());
    }

    @Test
    public void trueJosRahatRiittaaMuutenFalse() {
        assertTrue(kortti.otaRahaa(5) == true);
        assertTrue(kortti.otaRahaa(15) == false);
    }

    @Test
    public void toStringToimii() {
        kortti.lataaRahaa(990);
        assertEquals("saldo: 10.0", kortti.toString());
    }

}
