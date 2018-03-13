/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.unicafe.Kassapaate;
import com.mycompany.unicafe.Maksukortti;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class KassaPaateTest {

    Kassapaate kassa;
    Maksukortti kortti;

    public KassaPaateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void alussaOikeaMaaraRahaa() {
        assertEquals(100000, kassa.kassassaRahaa());
    }

    @Test
    public void alussaMyytyjaLounaitaOikeaMaara() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void VaihtorahaOikeaKateisellaMaksettaessaEdullinenLounas() {
        assertEquals(260, kassa.syoEdullisesti(500));
    }

    @Test
    public void VaihtorahaOikeaKateisellaMaksettaessaMaukasLounas() {
        assertEquals(100, kassa.syoMaukkaasti(500));
    }

    @Test
    public void kassanRahamaaraKasvaaEdullinenLounas() {
        kassa.syoEdullisesti(260);
        assertEquals(100240, kassa.kassassaRahaa());
    }

    @Test
    public void kassanRahamaaraKasvaaMaukasLounas() {
        kassa.syoMaukkaasti(400);
        assertEquals(100400, kassa.kassassaRahaa());
    }

    @Test
    public void myytyjenEdullistenLounaidenMaaraKasvaa() {
        kassa.syoEdullisesti(240);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void myytyjenMaukkaidenLounaidenMaaraKasvaa() {
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void josRahatEiRiitaToimiiOikein() {
        assertEquals(100, kassa.syoEdullisesti(100));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());

        assertEquals(100, kassa.syoMaukkaasti(100));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void korttiostoVeloittaaKortiltaJaPalauttaaTrue() {
        assertEquals(true, kassa.syoEdullisesti(kortti));
        assertEquals(760, kortti.saldo());

        assertEquals(true, kassa.syoMaukkaasti(kortti));
        assertEquals(360, kortti.saldo());
    }

    @Test
    public void kortillaOstettaessaMyytyjenLounaidenMaaraKasvaa() {
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);

        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void josKortillaEiRahaaToimiiOikein() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(false, kassa.syoMaukkaasti(kortti));

        assertEquals(200, kortti.saldo());

        kassa.syoEdullisesti(kortti);
        assertEquals(200, kortti.saldo());

        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(2, kassa.maukkaitaLounaitaMyyty());

    }

    @Test
    public void kassanRahamaaraEiMuutuKortillaOstettaessa() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());

        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }

    @Test
    public void kortilleLataaminenLisaaKortilleJaKassaan() {
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals(1500, kortti.saldo());
        assertEquals(100500, kassa.kassassaRahaa());
    }

    @Test
    public void kortilleLataaminenEiToimiNegatiivisellaArvolla() {
        kassa.lataaRahaaKortille(kortti, -10);
        assertEquals(1000, kortti.saldo());
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
