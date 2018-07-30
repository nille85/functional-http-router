package be.nille.http.router.exception;


import java.util.Optional;
import java.util.function.Function;

public class Result<T> {

    private static final String NOT_A_FAILURE = "NOT_A_FAILURE";

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

    public <R> R mapBoth(Function<? super T, ? extends R> successMapper, Function<Failure, ? extends R> failureMapper){
        R mappedSuccess = successMapper.apply(success.get().getValue());
        R mappedFailure = failureMapper.apply(failure.get());
        return isSuccess() ? mappedSuccess : mappedFailure;
    }

    public void fold(Function<? super T, Void> successCallback, Function<Failure, Void> failureCallback){
        if (isSuccess()){
            successCallback.apply(success.get().getValue());
        }else{
            failureCallback.apply(failure.get());
        }
    }

    public void ifFailure(Function<Failure, Void> failureCallback){
        if (isFailure()){
            failureCallback.apply(failure.get());
        }
    }

    public void ifSuccess(Function<? super T, Void> successCallback){
        if (isSuccess()){
            successCallback.apply(success.get().getValue());
        }
    }

}

