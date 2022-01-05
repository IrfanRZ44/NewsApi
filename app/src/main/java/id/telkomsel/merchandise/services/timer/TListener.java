package id.telkomsel.merchandise.services.timer;

public interface TListener {
    String updateDataOnTick(long remainingTimeInMs);
    void onTimerFinished();
}
