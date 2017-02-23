package com.innobse.task1;

import static com.innobse.task1.Main.*;
import com.innobse.task1.Exceptions.GetStreamException;
import org.apache.log4j.Logger;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
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
                addWordsFromString(resource, line);
            }
            if (!isCancel) Main.getCurrentDisplay().end(resource);
        } catch (IOException | GetStreamException e) {
            logger.error("Ошибка парсинга файла", e);
            Main.getCurrentDisplay().printErr(e);
            return ERROR;
        }
        return COMPLETE;
    }

    public static int getPartFile(String filePath, int part, int capacity, byte[] file) {
        AsynchronousFileChannel channel = null;
        try {
            channel = Streamer.getChannel(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        Future future = channel.read(buffer, part * capacity);
        while (!future.isDone());

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());

        bais.read(file, capacity * part, capacity);

        cdlN.countDown();
        try {
            cdlN.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        CharBuffer cb = ByteBuffer.wrap(file, part*capacity, capacity-capacity%2).asCharBuffer();
//        char[] chars = cb.array();
//        int start = (part == 0) ? 0 : -1;
//
//        for (int i = 0; i < chars.length; i++) {
//            if (chars[i] == 10){
//                if (start != -1){
//                    addWordsFromString(filePath, new String(chars, start, i-start));
//                }
//                start = i + 1;
//            }
//        }

        if (part == 0) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file)));
            try {
                //synchronized (br) {
                    String line;
                    while((line = br.readLine()) != null) {
                        if (addWordsFromString(filePath, line) == ERROR) return ERROR;
                    }

                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return COMPLETE;
    }

    private static int addWordsFromString(String resource, String line){
        if (invalidSymbols.matcher(line).find()) {
            String str = "Incorrect symbols in file: " + resource + "\nIn line: \'" + line + "\'";
            logger.error(str);
            Main.getCurrentDisplay().printErr(str);
            return ERROR;
        }
        for (String tempString : line.split("[^А-Яа-я\\-]")) {
            if (word.matcher(tempString).matches()) data.update(tempString);
        }

        return COMPLETE;
    }
}