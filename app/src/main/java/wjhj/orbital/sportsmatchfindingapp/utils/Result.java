package wjhj.orbital.sportsmatchfindingapp.utils;

public class Result<T> {

    private T result;
    private Exception e;

    public Result(T result) {
        this.result = result;
    }

    public Result(Exception e) {
        this.e = e;
    }


    public T getResult() {
        return result;
    }

    public Exception getE() {
        return e;
    }

    public boolean isSuccessful() {
        return result != null;
    }
}
