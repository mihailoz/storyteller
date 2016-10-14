package rs.manhut.core;

import java.io.*;

/**
 * Created by mihailo on 30.9.16..
 */
public class GameHistoryController {

    final private String filePath = "/home/storywriters/Storyteller/game-history/";

    public void writeGameHistory(String gameId, String gameStory) {
        try {
            File f = new File(filePath + gameId + ".txt");

            if(!f.exists()) {
                f.createNewFile();
            } else {
                throw new IOException("Game file already exists");
            }

            FileOutputStream is = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(gameStory);
            w.close();
        } catch (IOException io) {
            System.out.println("Unable to save game: " + io.getMessage());
        }
    }

    public String readGameHistory(String gameId) {
        try(BufferedReader br = new BufferedReader(new FileReader(this.getFilePath() + gameId + ".txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String story = sb.toString();

            return story;
        } catch (IOException io) {
            return "Unable to fetch game story";
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
