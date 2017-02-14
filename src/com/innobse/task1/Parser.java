package com.innobse.task1;

import com.innobse.task1.Exceptions.GetStreamException;
import static com.innobse.task1.Main.isCancel;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Class parser for analize text files. Open file and update information in StatData
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class Parser {
    private static Pattern invalidSymbols = Pattern.compile("[^0-9а-яА-ЯЁё\\.\\?\"\',:!;()\\-\\s]");
    private static Pattern word = Pattern.compile("^[А-Яа-яЁё]+\\-{0,1}[а-яё]*$");
    private static volatile boolean run = true;


    /**
     * Read file for words and update statistic
     *
     */

    public static int analize(String resource){
        try(BufferedReader br = new BufferedReader(Streamer.getStream(resource))) {
            String line;
            while (((line = br.readLine()) != null) && !isCancel) {
                if (invalidSymbols.matcher(line).find()) {
                    Main.getCurrentDisplay().printErr("Incorrect symbols in file: " + resource + "\nIn line: \'" + line + "\'");
                    return -1;
                }
                for (String tempString : line.split("[^А-Яа-я\\-]")) {
                    if (word.matcher(tempString).matches()) StatData.update(tempString);
                }
            }
            if (!isCancel) Main.getCurrentDisplay().end(resource);
        } catch (IOException | GetStreamException e) {
            Main.getCurrentDisplay().printErr(e);
            return -1;
        }
        return 0;
    }
}