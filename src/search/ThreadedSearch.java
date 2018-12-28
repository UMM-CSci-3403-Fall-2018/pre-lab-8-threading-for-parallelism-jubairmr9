package search;

import java.util.List;

public class ThreadedSearch<T> implements Searcher<T>, Runnable {

    private int numThreads;
    private T target;
    private List<T> list;
    private int begin;
    private int end;
    private Answer answer;

    public ThreadedSearch(int numThreads) {
        this.numThreads = numThreads;
    }

    private ThreadedSearch(T target, List<T> list, int begin, int end, Answer answer) {
        this.target = target;
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.answer = answer;
    }

    public boolean search(T target, List<T> list) throws InterruptedException {
    
        Answer sharedAnswer = new Answer();

        Thread[] thread = new Thread[numThreads];

        for (int i=0; i<numThreads; i++){

            int begin = (int)Math.floor((list.size() * i) / numThreads);
            int end = (int)Math.floor(list.size() * (i+1) / numThreads);

            ThreadedSearch<T> ThreadSearch = new ThreadedSearch<>(target, list, begin, end);

            thread[i] = new Thread(ThreadSearch);

            thread[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            thread[i].join();
        }

        return sharedAnswer.getAnswer();
    }

    public void run() {

        for (int i = begin; i < end; i++) {
        
            if(answer.getAnswer()){
                break;
            }
            
            if (list.get(i).equals(target)){
                answer.setAnswer(true);
            }
        }
    }
}

private class Answer {
    
    private boolean answer = false;

    public boolean getAnswer() {
        return answer;
    }

    public synchronized void setAnswer(boolean newAnswer) {
        answer = newAnswer;
    }
}


