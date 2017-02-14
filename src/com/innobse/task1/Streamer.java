package com.innobse.task1;

import com.innobse.task1.Exceptions.GetStreamException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;


/**
 * Class for get Reader for file in local or network.
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class Streamer {
    static final Logger logger = Logger.getLogger(Parser.class);
    private static Pattern url = Pattern.compile(
            "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\\.txt");

    /**
     *
     * Get Reader for file.
     *
     * @param resource path to file or URL
     * @return Reader
     * @throws GetStreamException
     */

    public static Reader getStream(String resource) throws GetStreamException {
        Reader result = null;
        if (url.matcher(resource).matches()){
            URL resUrl = null;
            try{
                resUrl = new URL(resource);
                result = new InputStreamReader(resUrl.openStream());
            } catch (MalformedURLException e){
                logger.error("Ошибка формирования URL", e);
                throw new GetStreamException("Error! Can't generate URL for string: " + resource);
            } catch (IOException e){
                logger.error("Ошибка при попытке открыть поток", e);
                throw new GetStreamException("Error! Can't open stream for text file on server.");
            }
        } else {
            try{
                result = new FileReader(resource);
            } catch (FileNotFoundException e) {
                logger.error("Ошибка при открытии файла", e);
                throw new GetStreamException("Error! Can't open file. File not found!");
            }
        }
        return result;
    }
}
