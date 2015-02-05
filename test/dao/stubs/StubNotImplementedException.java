package dao.stubs;

public class StubNotImplementedException extends RuntimeException {
    public StubNotImplementedException() {
    }
    public StubNotImplementedException(String message) {
        super(message);
    }
}
