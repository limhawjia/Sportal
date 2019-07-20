package wjhj.orbital.sportsmatchfindingapp.utils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java9.util.function.Predicate;
import wjhj.orbital.sportsmatchfindingapp.BR;

public class ValidationInput<T> extends BaseObservable {

    private T input;
    private Predicate<T> validation;
    private String errMessage;
    private State state;

    public ValidationInput(Predicate<T> validation, String errMessage) {
        this.validation = validation;
        this.errMessage = errMessage;
        this.state = State.NOT_VALIDATED;
    }

    @Bindable
    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
        this.state = State.NOT_VALIDATED;
        notifyPropertyChanged(BR.input);
        notifyPropertyChanged(BR.errMessage);
    }

    @Bindable
    public String getErrMessage() {
        if (state == State.ERROR) {
            return errMessage;
        } else {
            return null;
        }
    }

    public State getState() {
        return state;
    }


    public void validate() {
        state = validation.test(input) ? State.VALIDATED : State.ERROR;
        notifyPropertyChanged(BR.errMessage);
    }

    public enum State {
        NOT_VALIDATED,
        ERROR,
        VALIDATED
    }
}
