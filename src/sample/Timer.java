package sample;

public class Timer extends Thread {

    //reference to the handler
    private QuizHandler handler;
    //for stoping timer in other circumstanced than time exceeded(from Handler)
    private boolean shouldExit = false;
    //setter
    public Timer(QuizHandler handler){
        this.handler = handler;
    }

    //overriding the base run method of thread so it will run with this code
    @Override
    public void run() {
        //using super.run() to call the supper class Thread function run()
        super.run();
        //get the time at the beginning of the timer
        long startTime = System.currentTimeMillis();

        while(true){
            //check if the timer should be stoped
            if(shouldExit){
                break;
            }
            //calculate the duration and transform it from milisecond to minutes
            long duration = (System.currentTimeMillis() - startTime)/60000;

            //when the quiz duration exceeds 30 minutes tell the handler to stop the quiz
            if(duration >= 30){
                handler.setOver();
                break;
            }
        }
    }


    public void stopTimer(){
        shouldExit = true;
    }
}