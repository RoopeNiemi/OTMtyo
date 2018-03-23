/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame.pacman.map;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nroope
 */
public class MapLoader {

    private int currentMapNumber = 0;
    private int numberOfMaps=1;

    private List<String> loadMap() {
        String path = "map" + currentMapNumber + ".txt";
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        Scanner fileScanner = new Scanner(is);
        List<String> map = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            map.add(fileScanner.nextLine());
        }
        return map;
    }

    public List<String> nextMap() {
        if (currentMapNumber < numberOfMaps) {
            currentMapNumber++;
            return loadMap();
        }
        return loadMap();
    }
}
