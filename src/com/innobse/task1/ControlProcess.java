package com.innobse.task1;

/**
 * Class for control process
 *
 * Start parsing processes and kill them if error detected
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class ControlProcess extends Thread {
    private String[] res;

    ControlProcess(String[] res){
        this.res = res;
        start();
    }

    @Override
    public void run(){
        Thread[] threads = new Thread[res.length];
        for(int i = 0; i < res.length; i++){
            threads[i] = new AnalizatorProcess(res[i]);
        }
        try{
            for(Thread t : threads){
                if (isInterrupted()){
                    Main.getCurrentDisplay().stop();
                    Main.getCurrentDisplay().printErr("One file have incorrect symbols! The program will be closed.");      //  Q: не создавать же свой Exception?
                    return;
                }
                t.join();
            }
        } catch (InterruptedException e) {
            Main.getCurrentDisplay().printErr("ControlThread already stopped.");
        }


    }

}
