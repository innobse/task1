package com.innobse.task1;

import static com.innobse.task1.Main.*;
import com.innobse.task1.Exceptions.GetStreamException;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.regex.Pattern;

/**
 * Class parser for analize text files. Open file and update information in StatData
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class Parser {
    private static final Logger logger = Logger.getLogger(Parser.class);
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
                    String str = "Incorrect symbols in file: " + resource + "\nIn line: \'" + line + "\'";
                    logger.error(str);
                    Main.getCurrentDisplay().printErr(str);
                    return ERROR;
                }
                for (String tempString : line.split("[^А-Яа-я\\-]")) {
                    if (word.matcher(tempString).matches()) data.update(tempString);
                }
            }
            if (!isCancel) Main.getCurrentDisplay().end(resource);
        } catch (IOException | GetStreamException e) {
            logger.error("Ошибка парсинга файла", e);
            Main.getCurrentDisplay().printErr(e);
            return ERROR;
        }
        return COMPLETE;
    }
}