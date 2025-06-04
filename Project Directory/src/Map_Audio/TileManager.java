package Map_Audio;

import main.MapPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TileManager {
    MapPanel MP;
    Tile[] tiles;

    public TileManager(MapPanel mp) {
        this.MP = mp;
        tiles = new Tile[10];
        getTitleImage();
    }
    public void getTitleImage() {
        try{
          tiles[0] = new Tile();
//          URL imgUrl = getClass().getClassLoader().getResource("grass.png");
          tiles[0].image = ImageIO.read(getClass().getClassLoader().getResource("grass.png"));
          tiles[1] = new Tile();
          tiles[1].image = ImageIO.read(getClass().getClassLoader().getResource("wall.png"));
          tiles[2] = new Tile();
          tiles[2].image = ImageIO.read(getClass().getClassLoader().getResource("water.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        while(col <MP.maxScreenCol && row <MP.maxScreenRow) {
            g2.drawImage(tiles[0].image,x,y,MP.tileSize,MP.tileSize,null);
            col++;
            x+=MP.tileSize;

            if(col == MP.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y+=MP.tileSize;
            }
        }
    }
}
