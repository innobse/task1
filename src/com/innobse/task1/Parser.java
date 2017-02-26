package com.innobse.task1;

import static com.innobse.task1.Main.*;
import com.innobse.task1.Exceptions.GetStreamException;
import com.sun.org.apache.regexp.internal.CharacterArrayCharacterIterator;
import org.apache.log4j.Logger;
import sun.text.normalizer.UTF16;

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

    public static int getPartFile(String filePath, int num, long offset, int capacity, byte[][] common) {
        AsynchronousFileChannel channel = null;
        try {
            channel = Streamer.getChannel(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        Future future = channel.read(buffer, offset);
        while (!future.isDone());

        byte[] partOfFile = buffer.array();

//        cdlN.countDown();
//        try {
//            cdlN.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        //  ищем начало целой строки
        int start = 0;
        if (num != 0){
            while (partOfFile[start] != 10){
                start++;
            }
            start++;
        }

        //  сохраняем порваный кусок
        if(start != 0){
            common[num-1] = Arrays.copyOfRange(partOfFile, 0, start);
        }

        //  анализируем целые строки
        int i = start;
        for (; i < capacity; i++) {
            if (partOfFile[i] == 10) {
                if (addWordsFromString(filePath, new String(partOfFile, start, i-start)) == ERROR) return ERROR;
                start = i + 1;
            }
        }

        //  здесь поток, который отвечает за конец файла - анализирует конец, остальные собирают порваные строки
        if(num == common.length-1){
            if (addWordsFromString(filePath, new String(partOfFile, start, i-start)) == ERROR) return ERROR;
        } else{
            while (common[num] == null){
                try {
                    common[num].wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(start != capacity){
                int dif = capacity-start;
                byte[] temp = new byte[dif+common[num].length];
                System.arraycopy(partOfFile, start, temp, 0, dif);
                System.arraycopy(common[num], 0, temp, dif, common[num].length);
                logger.info("Собрано: " + new String(temp));
                if (addWordsFromString(filePath, new String(temp)) == ERROR) return ERROR;
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