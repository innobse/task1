package com.innobse.task1;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static Pattern url = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\\.txt");
    private static volatile boolean run = true;


    /**
     * Read file for words and update statistic
     *
     */

    public static int analize(String resource){
        Reader fileReader = null;
        if (url.matcher(resource).matches()){
            URL resUrl = null;
            try{
                resUrl = new URL(resource);
                fileReader = new InputStreamReader(resUrl.openStream());
            } catch (MalformedURLException e){
                Main.getCurrentDisplay().printErr("Error! Can't generate URL for string: " + resource);
                run = false;
                return -1;
            } catch (IOException e){
                Main.getCurrentDisplay().printErr("Error! Can't open stream for text file on server.");
                run = false;
                return -1;
            }
        } else {
            try{
                fileReader = new FileReader(resource);
            } catch (FileNotFoundException e) {
                Main.getCurrentDisplay().printErr(e);
                return -1;
            }
        }
        try(BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while((line = br.readLine()) != null) {
                if (invalidSymbols.matcher(line).find()){
                    Main.getCurrentDisplay().printErr("Incorrect symbols in file: " + resource + "\nIn line: \'" + line + "\'");
                    return -1;
                }
                for(String tempString : line.split("[^А-Яа-я\\-]")){
                    if (word.matcher(tempString).matches()) StatData.update(tempString);
                }
            }
            Main.getCurrentDisplay().end(resource);
        } catch (IOException e) {
            Main.getCurrentDisplay().printErr(e);
            return -1;
        }
        return 0;
    }
}