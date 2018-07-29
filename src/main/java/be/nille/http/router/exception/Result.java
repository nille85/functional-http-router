package be.nille.http.router.exception;


import java.util.Optional;
import java.util.function.Function;

public class Result<T> {

    private final Optional<Success<T>> success;
    private final Optional<Failure> failure;

    private Result(Optional<Success<T>> success, Optional<Failure> failure) {
        this.success = success;
        this.failure = failure;
    }

    public static <T> Result<T> ofSuccess(T value){
        return new Result(Optional.of(new Success<T>(value)), Optional.empty());
    }

    public static <T> Result<T> ofFailure(ErrorMessage errorMessage){
        return new Result(Optional.empty(), Optional.of(new Failure(errorMessage)));
    }

    public boolean isSuccess(){
        return success.isPresent();
    }

    public Optional<T> getValue(){
        return success
                .map(Success::getValue);
    }

    public Optional<ErrorMessage> getErrorMessage(){
        return failure.map(Failure::getErrorMessage);
    }

    public boolean isFailure(){
        return failure.isPresent();
    }

    public <R> Result<R> map(Function<? super T, ? extends R> mapper) {
        return isSuccess() ?
                Result.ofSuccess(mapper.apply(success.get().getValue())) :
                Result.ofFailure(failure.get().getErrorMessage());
    }

    public <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
        return isSuccess() ?
                mapper.apply(success.get().getValue()):
                Result.ofFailure(failure.get().getErrorMessage());
    }

}

