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


    public List<String> loadMap() {
        String path = "map2.txt";
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        Scanner fileScanner = new Scanner(is);
        List<String> map = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            map.add(fileScanner.nextLine());
        }
        return map;
    }
}
